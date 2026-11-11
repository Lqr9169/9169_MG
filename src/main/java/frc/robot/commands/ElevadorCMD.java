// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevadorSub;

/** An example command that uses an example subsystem. */
public class ElevadorCMD extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final ElevadorSub elevSub;
  double setpoint;
  double erro;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ElevadorCMD(ElevadorSub elevSub, double setpoint) {
    this.elevSub = elevSub;
    this.setpoint = setpoint;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(elevSub);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    elevSub.getSetpoint(setpoint);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    erro = setpoint - elevSub.Elev1.getEncoder().getPosition();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(erro) <= 0.5;
  }
}
