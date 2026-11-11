package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ManipuloSub extends SubsystemBase{
public static SparkMax ManipuloMotor1;
public static SparkMax ManipuloMotor2;
PIDController pid;
double setpoint = 0.0;
SlewRateLimiter ramp;
    public ManipuloSub(){
        ManipuloMotor1 = new SparkMax(Constants.Manipulo_AngE, MotorType.kBrushless);
        ManipuloMotor2 = new SparkMax(Constants.Manipulo_AngD, MotorType.kBrushless);
        pid = new PIDController(0.07, 0, 0);
        ramp = new SlewRateLimiter(3);
    }

    public void getSetpoint(double setpoint){
        this.setpoint = setpoint;
    }

    @Override
    public void periodic() {
        double encoder = ManipuloMotor1.getEncoder().getPosition();
        pid.setSetpoint(setpoint);
        double calculoPID = pid.calculate(encoder);
        double calculoRamp = ramp.calculate(calculoPID);
        double position = MathUtil.clamp(calculoRamp, -0.5, 0.5);
        ManipuloMotor1.set(position);
    }
    
}
