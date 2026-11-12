package frc.robot.subsystems;


import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.units.VoltageUnit;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotContainer;



public class ManipuloSub extends SubsystemBase{

        public static  SparkMax Mrot1;
        public static  SparkMax Mrot2;
        public double encoderA, encoderB; 
        public double AnguloA, AnguloB;
        public double alvoRot = 0.0, alvoInc = 0.0;
        public double rotAtual, incAtual;
    
        private PIDController pidRot, pidInc;
    
        private SlewRateLimiter rampA, rampB;
    
    public ManipuloSub() {
    
        Mrot1 = new SparkMax(Constants.Manipulo_AngE, MotorType.kBrushless);
        Mrot2 = new SparkMax(Constants.Manipulo_AngD, MotorType.kBrushless);

        pidRot = new PIDController(0.001, 0.0, 0.0);
        pidInc = new PIDController(0.001, 0.0, 0.0);

        rampA = new SlewRateLimiter(1);
        rampB = new SlewRateLimiter(1);
    }

    public void setRotacao(double graus) {

        alvoRot = graus;
    }
    public void setInclinacao(double graus) {

        alvoInc = graus;
    }

    @Override
    public void periodic() {

        //Motor 1
            encoderA = Mrot1.getEncoder().getPosition();
        
            AnguloA = 
                (encoderA/Constants.GearBoxA) * 360;


        //Motor 2
            encoderB = Mrot2.getEncoder().getPosition();

            AnguloB = 
                (encoderB/Constants.GearBoxB) * 360;


        rotAtual = (AnguloA + AnguloB) / 2.0;
        incAtual = (AnguloB - AnguloA) / 2.0;

        double outputRot = pidRot.calculate(rotAtual, alvoRot);
        double outputInc = pidInc.calculate(incAtual, alvoInc);

        double outputA = outputRot - outputInc;
        double outputB = outputRot + outputInc;

        outputA = MathUtil.clamp(outputA, -0.1, 0.1);
        outputB = MathUtil.clamp(outputB, -0.1, 0.1);

        outputA = rampA.calculate(outputA);
        outputB = rampB.calculate(outputB);

        Mrot1.set(outputA);
        Mrot2.set(outputB);

        SmartDashboard.putNumber(
            "Robo/Manipulador/EncoderA", encoderA
        );

        SmartDashboard.putNumber(
            "Robo/Manipulador/EncoderB", encoderB
        );

        SmartDashboard.putNumber(
            "Robo/Manipulador/AnguloA", AnguloA
        );

        SmartDashboard.putNumber(
            "Robo/Manipulador/AnguloB", AnguloB
        );

        SmartDashboard.putNumber(
            "Robo/Manipulador/RotacaoAtual", rotAtual
        );

        SmartDashboard.putNumber(
            "Robo/Manipulador/InclinacaoAtual", incAtual
        );

        SmartDashboard.putNumber(
            "Robo/Manipulador/OutputRotacao", outputRot
        );

        SmartDashboard.putNumber(
            "Robo/Manipulador/OutputInclinacao", outputInc
        );

        SmartDashboard.putNumber(
            "Robo/Manipulador/OutputA", outputA
        );

        SmartDashboard.putNumber(
            "Robo/Manipulador/OutputB", outputB
        );
    }
}
