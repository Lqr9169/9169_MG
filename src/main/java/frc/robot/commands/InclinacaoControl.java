package frc.robot.commands;


import edu.wpi.first.units.measure.MutResistance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ManipuloSub;


public class InclinacaoControl extends Command {

  private final ManipuloSub MRsub;
  double setpoint;
  double erro;

  public InclinacaoControl(ManipuloSub MRsub, double graus) {
    setpoint = graus;
    this.MRsub = MRsub;

    addRequirements(MRsub);
  }

  @Override
  public void initialize() {
    
  }

  @Override
  public void execute() {
    if (setpoint == setpoint * 1.0) {
      setpoint = setpoint + 0.5;
      MRsub.setInclinacao(setpoint);
    }
    else if (setpoint == setpoint * -1.0) {
      setpoint = setpoint - 0.5;
      MRsub.setInclinacao(setpoint);
    }
    
    
    erro = setpoint - MRsub.incAtual;
    
    //if (MRsub.incAtual >= 140.0) {
    //    
    //  MRsub.setRotacao(180.0);
    //}
    
  }

  @Override
  public void end(boolean interrupted) {
    
  }

  @Override
  public boolean isFinished() {

    return Math.abs(erro) <= 7.5;
  }
}
