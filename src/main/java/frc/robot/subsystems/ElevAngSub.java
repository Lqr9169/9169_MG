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
double setpoint = -15;//-20;
SlewRateLimiter ramp;
public static double angulo;
    public ElevAngSub(){
        ElevAngMotorE = new SparkMax(Constants.ElevAng_E, MotorType.kBrushless);
        ElevAngMotorD = new SparkMax(Constants.ElevAng_D, MotorType.kBrushless);
        pid = new PIDController(0.05, 0, 0);
        ramp = new SlewRateLimiter(3);
    }

    public void getSetpoint(double setpoint){
        this.setpoint = setpoint;
        
    }

    @Override
    public void periodic() {
        double encoder = ElevAngMotorE.getEncoder().getPosition();
        angulo = (encoder/ 416) * 360;
        //pid.setSetpoint(setpoint);
        double calculoPID = pid.calculate(angulo, setpoint);
        double calculoRamp = ramp.calculate(calculoPID);
        double position = MathUtil.clamp(calculoRamp, -0.5, 0.5);
        ElevAngMotorE.set(position);
    }
    
    
}
