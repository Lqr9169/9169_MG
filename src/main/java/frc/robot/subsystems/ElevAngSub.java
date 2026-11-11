package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ElevAngSub extends SubsystemBase{
public static SparkMax ElevAngMotorE;
public static SparkMax ElevAngMotorD;
PIDController pid;
double setpoint = -20;
SlewRateLimiter ramp;
    public ElevAngSub(){
        ElevAngMotorE = new SparkMax(Constants.ElevAng_E, MotorType.kBrushless);
        ElevAngMotorD = new SparkMax(Constants.ElevAng_D, MotorType.kBrushless);
        pid = new PIDController(0.03, 0, 0);
        ramp = new SlewRateLimiter(5);
    }

    public void getSetpoint(double setpoint){
        this.setpoint = setpoint;
        
    }

    @Override
    public void periodic() {
        double encoder = ElevAngMotorE.getEncoder().getPosition();
        pid.setSetpoint(setpoint);
        double calculoPID = pid.calculate(encoder);
        double calculoRamp = ramp.calculate(calculoPID);
        double position = MathUtil.clamp(calculoRamp, -0.3, 0.3);
        ElevAngMotorE.set(position);
    }
    
    public void pararelevdorAng() {

        ElevAngMotorE.stopMotor();
    }
}
