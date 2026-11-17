// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ManipuloSub;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ElevadorSub;
import frc.robot.subsystems.ManipuladorSub;


/** An example command that uses an example subsystem. */
public class ManipuladorColetaCMD extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final ManipuladorSub MCsub;
  double velo;
  Timer time;
  

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ManipuladorColetaCMD(ManipuladorSub MCsub, double velo) {
    this.velo = velo;
    this.MCsub = MCsub;
    time = new Timer();
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(MCsub);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    time.stop();
    time.reset();
    MCsub.coleta(velo);
    time.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
  }
    
  

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    MCsub.Metodoverica();
    time.stop();
    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return MCsub.Mdisp.getOutputCurrent() >= 39 && time.get() > 0.1;
  }
}
