// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.FollowPathCommand;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.commands.sequenciaPontua;
import frc.robot.commands.ElevAngCMD;
import frc.robot.commands.ElevadorCMD;
import frc.robot.commands.Inclinacao;
import frc.robot.commands.ManipuloRotCMD;
import frc.robot.commands.ManipuladorColetaCMD;
import frc.robot.commands.ManipuladorDispCMD;
import frc.robot.commands.sequenciaColeta;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ElevAngSub;
import frc.robot.subsystems.ManipuloSub;
import frc.robot.subsystems.ElevadorSub;
import frc.robot.subsystems.ManipuladorSub;

public class RobotContainer {

    private double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top
                                                                                        // speed
    private double MediumSpeed = 1.0 * MetersPerSecond.of(2.5).in(MetersPerSecond);
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second
                                                                                      // max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric fieldDrive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1)
            .withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors


    private final SwerveRequest.FieldCentricFacingAngle headingController = new SwerveRequest.FieldCentricFacingAngle()
            .withDeadband(MaxSpeed * 0.1)
            .withHeadingPID(0.6, 0.0, 0.13);

    public Rotation2d headingDesejado = new Rotation2d();

    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    public static final CommandXboxController joystick = new CommandXboxController(0);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    private final ElevadorSub elevSub = new ElevadorSub();

    public static final ManipuloSub manipuloSub = new ManipuloSub();

    private final ElevAngSub elevAngSub = new ElevAngSub();

    private final ManipuladorSub Mcoleta = new ManipuladorSub();

    Trigger left_Bumper = joystick.leftBumper();

    public RobotContainer() {
        configureBindings();
        
    }

    private void configureBindings() {

        drivetrain.setDefaultCommand(
                
                drivetrain.applyRequest(() -> {
                    double rightX = -joystick.getRightX();
                    Rotation2d currentHeading = drivetrain.getState().Pose.getRotation();

                    if (Math.abs(rightX) > 0.05) {
                        headingDesejado = currentHeading;

                        return fieldDrive.withVelocityX(-joystick.getLeftY() * MediumSpeed)
                                .withVelocityY(-joystick.getLeftX() * MediumSpeed)
                                .withRotationalRate(rightX * MaxAngularRate);

                    } else {

                        var alliance = DriverStation.getAlliance();
                        Rotation2d targetHeading = headingDesejado;

                        if (alliance.isPresent() && alliance.get() == DriverStation.Alliance.Red) {

                            targetHeading = headingDesejado.plus(Rotation2d.fromDegrees(180));
                        }

                        return headingController.withVelocityX(-joystick.getLeftY() * MediumSpeed)
                                .withVelocityY(-joystick.getLeftX() * MediumSpeed)
                                .withTargetDirection(targetHeading);

                    }

                }));

        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
                drivetrain.applyRequest(() -> idle).ignoringDisable(true));


    // pontuação na Matriz
        
        //pontua lvl 1M
        joystick.a().onTrue(
            new sequenciaPontua(
                manipuloSub, Constants.Inc_Mat1, Constants.Rot180,
                Mcoleta, Constants.Speed_Outtake,
                elevAngSub, Constants.Ang_Mat1,
                elevSub, Constants.Elev_Mat1));

        //pontua lvl 2M
        joystick.x().onTrue(
            new sequenciaPontua(
                manipuloSub, Constants.Inc_Mat2,Constants.Rot180,
                Mcoleta, Constants.Speed_Outtake,
                elevAngSub,Constants.Ang_Mat2,
                elevSub, Constants.Elev_Mat2
            )
        );

        //pontua lvl 3M
        joystick.y().onTrue(
            new sequenciaPontua(
                manipuloSub, Constants.Inc_Mat3 ,Constants.Rot180,
                Mcoleta, Constants.Speed_Outtake,
                elevAngSub, Constants.Ang_Mat3,
                elevSub, Constants.Elev_Mat3));

        // Coleta
        joystick.b().onTrue(
            new sequenciaColeta(
                manipuloSub, Constants.Inc_Intake, Constants.Rot180,
                Mcoleta, Constants.Speed_Intake,
                elevAngSub, Constants.Ang_Intake));

        //botão para pontuar fora do seçuencial
        joystick.start().onTrue(
            new ManipuladorDispCMD(
                Mcoleta, Constants.Speed_Outtake));

    // pontuação na torre

        //pontua lvl 1T
        joystick.a().and(left_Bumper).onTrue(
            new sequenciaPontua(
                manipuloSub, Constants.Inc_Tower1, Constants.Rot0,
                Mcoleta, Constants.Speed_OuttakeT,
                elevAngSub, Constants.Ang_Tower1,
                elevSub, Constants.Elev_Tower1));
        
        //pontua lvl 2T
        joystick.x().and(left_Bumper).onTrue(
            new sequenciaPontua(
                manipuloSub, Constants.Inc_Tower2,Constants.Rot180,
                Mcoleta, Constants.Speed_OuttakeT,
                elevAngSub,Constants.Ang_Tower2,
                elevSub, Constants.Elev_Tower2
            )
        );

        //pontua lvl 3T
        joystick.y().and(left_Bumper).onTrue(
            new sequenciaPontua(
                manipuloSub, Constants.Inc_Tower3 ,Constants.Rot180,
                Mcoleta, Constants.Speed_OuttakeT,
                elevAngSub, Constants.Ang_Tower3,
                elevSub, Constants.Elev_Tower3));
     
        

        


        // drivetrain.registerTelemetry(logger::telemeterize);
        drivetrain.registerTelemetry(state -> {
            logger.telemeterize(state);

            double yaw = drivetrain.getPigeon2()
                    .getYaw()
                    .getValueAsDouble();

            logger.telemeterizePigeonYaw(yaw);
        });
    }

    public Command getAutonomousCommand() {
        // Simple drive forward auton
        final var idle = new SwerveRequest.Idle();
        return Commands.sequence(
                // Reset our field centric heading to match the robot
                // facing away from our alliance station wall (0 deg).
                drivetrain.runOnce(() -> drivetrain.seedFieldCentric(Rotation2d.kZero)),
                // Then slowly drive forward (away from us) for 5 seconds.
                drivetrain.applyRequest(() -> fieldDrive.withVelocityX(0.5)
                        .withVelocityY(0)
                        .withRotationalRate(0))
                        .withTimeout(5.0),
                // Finally idle for the rest of auton
                drivetrain.applyRequest(() -> idle));

    }
}
