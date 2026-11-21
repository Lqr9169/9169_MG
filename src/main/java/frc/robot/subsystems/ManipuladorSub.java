package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ManipuladorSub extends SubsystemBase{
    public static SparkMax Mdisp;
    double velo;
    SlewRateLimiter rampa;
    public ManipuladorSub(){
        Mdisp = new SparkMax(Constants.Manipulador, MotorType.kBrushless);
        rampa = new SlewRateLimiter(3);
    }

    public void coleta(double velo){
        this.velo = velo;
        Mdisp.set(velo);

    }

    public void Metodoverica(){
        Mdisp.set(0.15);
    }

    

}
