package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class ElevadorSub extends SubsystemBase{
public static SparkMax Elev1;
public static SparkMax Elev2;
PIDController pid;
double setpoint = Constants.Elev_Default;
SlewRateLimiter ramp;
    public ElevadorSub(){
        Elev1 = new SparkMax(Constants.Elev_E, MotorType.kBrushless);
        Elev2 = new SparkMax(Constants.Elev_D, MotorType.kBrushless);
        pid = new PIDController(0.03, 0, 0);
        ramp = new SlewRateLimiter(3);
    }

    public void getSetpoint(double setpoint){
        this.setpoint = setpoint;
    }

    @Override
    public void periodic() {

        //if (RobotContainer.joystick.start().getAsBoolean() && RobotContainer.joystick.leftBumper().getAsBoolean() == false) {

        //    setpoint = setpoint + 1;
        //}

        //else if (RobotContainer.joystick.back().getAsBoolean() && RobotContainer.joystick.leftBumper().getAsBoolean() == false) {

        //   setpoint = setpoint - 1;
        //}
        double encoder = Elev1.getEncoder().getPosition();
        pid.setSetpoint(setpoint);
        double calculoPID = pid.calculate(encoder);
        double calculoRamp = ramp.calculate(calculoPID);
        double position = MathUtil.clamp(calculoRamp, -0.4, 0.4);
        Elev1.set(position);
    }
    
}
