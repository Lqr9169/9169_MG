package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.DutyCycle;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ElevAngSub extends SubsystemBase{
public static SparkMax ElevAngMotorE;
public static SparkMax ElevAngMotorD;
PIDController pid;
double setpoint = Constants.ElevAng_Default;//275;
SlewRateLimiter ramp;
public static DutyCycleEncoder encoder;
    public ElevAngSub(){
        ElevAngMotorE = new SparkMax(Constants.ElevAng_E, MotorType.kBrushless);
        ElevAngMotorD = new SparkMax(Constants.ElevAng_D, MotorType.kBrushless);
        pid = new PIDController(0.009, 0, 0);
        pid.enableContinuousInput(0, 360);
        ramp = new SlewRateLimiter(4);
        encoder = new DutyCycleEncoder(0, 1.0, 0.68);//0.31/0.68 & 0.78
        encoder.setInverted(true);
    }

    public void getSetpoint(double setpoint){
        this.setpoint = setpoint;
        
    }

    @Override
    public void periodic() {
        
        double calculoPID = pid.calculate(encoder.get() * 360.0, setpoint);
        double calculoRamp = ramp.calculate(calculoPID);
        double position = MathUtil.clamp(calculoRamp, -0.75, 0.75);
        ElevAngMotorE.set(position);
    }
    
    
}
