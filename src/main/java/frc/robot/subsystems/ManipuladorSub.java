package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ManipuladorSub extends SubsystemBase{
    public static SparkMax Mdisp;
    double velo;
    PIDController pid;
    double setpoint = -0.5;
    double position;
    public static boolean verifica = false;
    public ManipuladorSub(){
        Mdisp = new SparkMax(Constants.Manipulador, MotorType.kBrushless);
        pid = new PIDController(0.03, 0.0, 0.0);
    }

    public void coleta(double velo){
        this.velo = velo;
        Mdisp.set(velo);
    }

    @Override
    public void periodic() {
        double encoder = Mdisp.getEncoder().getPosition();
        pid.setSetpoint(setpoint);
        double calculoPID = pid.calculate(encoder);
        position = MathUtil.clamp(calculoPID, -0.5, 0.5);
        
        
    }

    public void Metodoverica(){
        if (verifica == true) {
            Mdisp.getEncoder().setPosition(0);
            Mdisp.set(position);
        }
    }

}
