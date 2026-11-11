// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ManipuloSub;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ElevadorSub;
import frc.robot.subsystems.ManipuladorSub;


/** An example command that uses an example subsystem. */
public class ManipuladorCMD extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final ManipuladorSub MCsub;
  double velo;
  

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ManipuladorCMD(ManipuladorSub MCsub, double velo) {
    this.velo = velo;
    this.MCsub = MCsub;
  
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(MCsub);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    MCsub.coleta(velo);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    MCsub.Metodoverica();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    MCsub.verifica = true;
    System.out.println("manipulo");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return MCsub.Mdisp.getOutputCurrent() >= 36; //|| RobotContainer.joystick.button(5).getAsBoolean();
  }
}
