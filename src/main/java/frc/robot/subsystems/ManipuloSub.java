package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Manipulo;

/**
 * Manipulador diferencial com dois NEO (SparkMax).
 *
 * Os dois motores trabalham juntos o tempo todo:
 *   motorE = pivo + rotacao
 *   motorD = pivo - rotacao
 * e, ao contrario, a leitura dos encoders da:
 *   pivo    = (motorE + motorD) / 2
 *   rotacao = (motorE - motorD) / 2
 *
 * O controle (PID com perfil trapezoidal) roda em GRAUS do mecanismo, um para
 * o pivo e outro para a rotacao. As saidas sao somadas/subtraidas para cada motor.
 */
public class ManipuloSub extends SubsystemBase {

    private final SparkMax motorE;
    private final SparkMax motorD;
    private final RelativeEncoder encoderE;
    private final RelativeEncoder encoderD;

    private final ProfiledPIDController pidPivo = new ProfiledPIDController(
            Manipulo.PIVO_KP, 0, Manipulo.PIVO_KD,
            new TrapezoidProfile.Constraints(Manipulo.PIVO_VEL_MAX, Manipulo.PIVO_ACEL_MAX));

    private final ProfiledPIDController pidRot = new ProfiledPIDController(
            Manipulo.ROT_KP, 0, Manipulo.ROT_KD,
            new TrapezoidProfile.Constraints(Manipulo.ROT_VEL_MAX, Manipulo.ROT_ACEL_MAX));

    private double objetivoPivo = Manipulo.INICIAL_PIVO;
    private double objetivoRot = Manipulo.INICIAL_ROT;

    public ManipuloSub() {
        motorE = new SparkMax(Constants.Manipulo_AngE, MotorType.kBrushless);
        motorD = new SparkMax(Constants.Manipulo_AngD, MotorType.kBrushless);

        SparkMaxConfig configE = new SparkMaxConfig();
        configE.smartCurrentLimit(Manipulo.LIMITE_CORRENTE)
               .idleMode(IdleMode.kBrake)
               .inverted(Manipulo.INVERTER_MOTOR_E);

        SparkMaxConfig configD = new SparkMaxConfig();
        configD.smartCurrentLimit(Manipulo.LIMITE_CORRENTE)
               .idleMode(IdleMode.kBrake)
               .inverted(Manipulo.INVERTER_MOTOR_D);

        // Sem "follow": cada motor e comandado individualmente.
        motorE.configure(configE, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        motorD.configure(configD, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        encoderE = motorE.getEncoder();
        encoderD = motorD.getEncoder();

        // O robo deve ser ligado com o manipulador na posicao inicial.
        zerarEncoders();
    }

    // ------------------------------------------------------------------ leitura

    /** Media das rotacoes dos motores (sem conversao) - usado para calibrar. */
    public double getRawPivo() {
        return (encoderE.getPosition() + encoderD.getPosition()) / 2.0;
    }

    /** Meia diferenca das rotacoes dos motores (sem conversao) - usado para calibrar. */
    public double getRawRotacao() {
        return (encoderE.getPosition() - encoderD.getPosition()) / 2.0;
    }

    public double getPivoGraus() {
        return getRawPivo() * Manipulo.GRAUS_POR_ROT_MOTOR_PIVO;
    }

    public double getRotacaoGraus() {
        return getRawRotacao() * Manipulo.GRAUS_POR_ROT_MOTOR_ROTACAO * Manipulo.SINAL_ROTACAO;
    }

    public double getObjetivoPivo() {
        return objetivoPivo;
    }

    public double getObjetivoRotacao() {
        return objetivoRot;
    }

    public boolean noObjetivo() {
        return Math.abs(objetivoPivo - getPivoGraus()) <= Manipulo.TOLERANCIA_GRAUS
            && Math.abs(objetivoRot - getRotacaoGraus()) <= Manipulo.TOLERANCIA_GRAUS;
    }

    // ------------------------------------------------------------------ comandos basicos

    /** Define o alvo do pivo e da rotacao (graus). Valores sao limitados aos limites de seguranca. */
    public void setObjetivo(double pivoGraus, double rotacaoGraus) {
        objetivoPivo = MathUtil.clamp(pivoGraus, Manipulo.PIVO_MIN, Manipulo.PIVO_MAX);
        objetivoRot = MathUtil.clamp(rotacaoGraus, Manipulo.ROT_MIN, Manipulo.ROT_MAX);
    }

    /** Considera a posicao atual como 0/0 (posicao inicial). */
    public void zerarEncoders() {
        encoderE.setPosition(0);
        encoderD.setPosition(0);
        objetivoPivo = 0;
        objetivoRot = 0;
        pidPivo.reset(0);
        pidRot.reset(0);
    }

    public void parar() {
        motorE.stopMotor();
        motorD.stopMotor();
    }

    // ------------------------------------------------------------------ fabricas de Command

    /** Vai para (pivo, rotacao) em graus e termina quando chegar. */
    public Command irPara(double pivoGraus, double rotacaoGraus) {
        return Commands.runOnce(() -> setObjetivo(pivoGraus, rotacaoGraus), this)
                .andThen(Commands.waitUntil(this::noObjetivo))
                .withName("Manipulo irPara(" + pivoGraus + ", " + rotacaoGraus + ")");
    }

    public Command posicaoInicial() {
        return irPara(Manipulo.INICIAL_PIVO, Manipulo.INICIAL_ROT);
    }

    public Command zerar() {
        return Commands.runOnce(this::zerarEncoders, this).ignoringDisable(true);
    }

    // ------------------------------------------------------------------ loop

    @Override
    public void periodic() {
        double pivo = getPivoGraus();
        double rot = getRotacaoGraus();

        if (DriverStation.isDisabled()) {
            // Ao habilitar, segura onde esta em vez de dar um tranco ate o alvo antigo.
            objetivoPivo = MathUtil.clamp(pivo, Manipulo.PIVO_MIN, Manipulo.PIVO_MAX);
            objetivoRot = MathUtil.clamp(rot, Manipulo.ROT_MIN, Manipulo.ROT_MAX);
            pidPivo.reset(pivo);
            pidRot.reset(rot);
        } else {
            double vPivo = pidPivo.calculate(pivo, objetivoPivo)
                    + Manipulo.PIVO_KG * Math.cos(Math.toRadians(pivo - Manipulo.PIVO_OFFSET_HORIZONTAL));
            double vRot = pidRot.calculate(rot, objetivoRot) * Manipulo.SINAL_ROTACAO;

            // Mistura do diferencial
            double vE = vPivo + vRot;
            double vD = vPivo - vRot;

            // Se um motor saturar, reduz os dois na mesma proporcao para manter a direcao do movimento.
            double maior = Math.max(Math.abs(vE), Math.abs(vD));
            if (maior > Manipulo.TENSAO_MAX) {
                vE *= Manipulo.TENSAO_MAX / maior;
                vD *= Manipulo.TENSAO_MAX / maior;
            }

            motorE.setVoltage(vE);
            motorD.setVoltage(vD);
        }

        SmartDashboard.putNumber("Manipulo/pivo_graus", pivo);
        SmartDashboard.putNumber("Manipulo/rot_graus", rot);
        SmartDashboard.putNumber("Manipulo/objetivo_pivo", objetivoPivo);
        SmartDashboard.putNumber("Manipulo/objetivo_rot", objetivoRot);
        SmartDashboard.putNumber("Manipulo/raw_pivo", getRawPivo());
        SmartDashboard.putNumber("Manipulo/raw_rot", getRawRotacao());
        SmartDashboard.putNumber("Manipulo/corrente_E", motorE.getOutputCurrent());
        SmartDashboard.putNumber("Manipulo/corrente_D", motorD.getOutputCurrent());
        SmartDashboard.putBoolean("Manipulo/no_objetivo", noObjetivo());
    }
}
