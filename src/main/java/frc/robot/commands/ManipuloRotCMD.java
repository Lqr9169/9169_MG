// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;


import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ManipuloSub;
import frc.robot.Robot;

/** An example command that uses an example subsystem. */
public class ManipuloRotCMD extends Command {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final ManipuloSub manipuloSub;
  double setpoint;
  double erro;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ManipuloRotCMD(ManipuloSub manipuloSub, double setpoint) {
    this.manipuloSub = manipuloSub;
    this.setpoint = setpoint;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(manipuloSub);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
    manipuloSub.ManipuloMotor2.configure(Robot.seguidorManipulo2, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
    manipuloSub.getSetpoint(setpoint);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    erro = setpoint - manipuloSub.ManipuloMotor1.getEncoder().getPosition();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    manipuloSub.ManipuloMotor1.getEncoder().setPosition(0);
    manipuloSub.getSetpoint(0);
    System.out.println("rot");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(erro) <= 0.15
    ;
  }
}
