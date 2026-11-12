package frc.robot.commands;


import edu.wpi.first.units.measure.MutResistance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ManipuloSub;



public class ManipuloRotCMD extends Command {

  private final ManipuloSub MRsub;
  double setpoint;
  double erro;

  public ManipuloRotCMD(ManipuloSub MRsub, double angulo) {
    setpoint = angulo;
    this.MRsub = MRsub;

    addRequirements(MRsub);
  }

  @Override
  public void initialize() {
    MRsub.setRotacao(setpoint);
  }

  @Override
  public void execute() {
      
    erro = setpoint - MRsub.rotAtual;
    SmartDashboard.putNumber("erro", erro);
    //if (MRsub.rotAtual <= 35.0) {
    //    
    //  MRsub.setInclinacao(0.0);
    //}
  }

  @Override
  public void end(boolean interrupted) {
    System.out.println("scared é gay");
  }

  @Override
  public boolean isFinished() {

    return Math.abs(erro) < 5;
  }
}
