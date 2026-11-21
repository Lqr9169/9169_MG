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
        public static double rotAtual, incAtual;
        double speed = 0.5;
    
        private PIDController pidRot, pidInc;
    
        private SlewRateLimiter rampA, rampB;
        double Kp1 = 0.001, Kp2 = 0.001;
    
    public ManipuloSub() {
    
        Mrot1 = new SparkMax(Constants.Manipulo_AngE, MotorType.kBrushless);
        Mrot2 = new SparkMax(Constants.Manipulo_AngD, MotorType.kBrushless);

        
        

        rampA = new SlewRateLimiter(3);
        rampB = new SlewRateLimiter(3);
    }

    public void setRotacao(double graus) {
        speed = 0.5;
        Kp1 = 0.0018;
        Kp2 = 0.0018;
        alvoRot = graus;
    }
    public void setInclinacao(double graus) {
        speed = 1;
        Kp1 = 0.0029;
        Kp2 = 0.0029;
        alvoInc = graus;
    }

    @Override
    public void periodic() {

        pidRot = new PIDController(Kp1, 0.0, 0.0);
        pidInc = new PIDController(Kp2, 0.0, 0.0);

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


        if (RobotContainer.joystick.getLeftTriggerAxis() > 0.05) {

            alvoInc = alvoInc + 1;
        }

        else if (RobotContainer.joystick.getRightTriggerAxis() > 0.05) {

           alvoInc = alvoInc - 1;
        }

        double outputRot = pidRot.calculate(rotAtual, alvoRot);
        double outputInc = pidInc.calculate(incAtual, alvoInc);

        double outputA = outputRot - outputInc;
        double outputB = outputRot + outputInc;

        outputA = MathUtil.clamp(outputA, -speed, speed);
        outputB = MathUtil.clamp(outputB, -speed, speed);

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
