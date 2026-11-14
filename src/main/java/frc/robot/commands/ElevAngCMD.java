// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;


import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ElevAngSub;
import frc.robot.subsystems.ElevadorSub;

/** An example command that uses an example subsystem. */
public class ElevAngCMD extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final ElevAngSub elevAngSub;
  double graus;
  double erro;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ElevAngCMD(ElevAngSub elevAngSub, double graus) {
    this.elevAngSub = elevAngSub;
    this.graus = graus;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(elevAngSub);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    elevAngSub.getSetpoint(graus);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    erro = graus - elevAngSub.encoder.get() * 360.0;
    SmartDashboard.putNumber("erro", Math.abs(erro));
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("saiiiiii");
    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(erro) < 2.0;
  }
}
