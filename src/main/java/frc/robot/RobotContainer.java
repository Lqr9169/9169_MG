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
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.commands.ManipuloAngCMD;
import frc.robot.commands.ManipuloRotCMD;
import frc.robot.commands.ManipuloPosCMD;
import frc.robot.commands.sequenciaColeta;
import frc.robot.commands.sequenciaPontua;
import frc.robot.commands.ElevAngCMD;
import frc.robot.commands.ElevadorCMD;
import frc.robot.commands.ManipuladorCMD;

import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ElevAngSub;
import frc.robot.subsystems.ManipuloSub;
import frc.robot.subsystems.ElevadorSub;
import frc.robot.subsystems.ManipuladorSub;


public class RobotContainer {

    

    private double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MediumSpeed = 1.0 * MetersPerSecond.of(2.5).in(MetersPerSecond);
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
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
    
    private final ManipuloSub manipuloSub = new ManipuloSub();

    private final ElevAngSub elevAngSub = new ElevAngSub();



    private final ManipuladorSub Mcoleta = new ManipuladorSub();
    

    public RobotContainer() {  
        configureBindings();
        
  }

    private void configureBindings() {
        

        drivetrain.setDefaultCommand(
             //Drivetrain will execute this command periodically
            //drivetrain.applyRequest(() ->
            //    drive.withVelocityX(-joystick.getLeftY() * MediumSpeed) // Drive forward with negative Y (forward)
            //        .withVelocityY(-joystick.getLeftX() * MediumSpeed) // Drive left with negative X (left)
            //        .withRotationalRate(-joystick.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            //)
            drivetrain.applyRequest(() -> {
                double rightX = -joystick.getRightX();
                Rotation2d currentHeading = drivetrain.getState().Pose.getRotation();

                if (Math.abs(rightX) > 0.05) {
                    headingDesejado = currentHeading;
                    

                    return drive.withVelocityX(-joystick.getLeftY() * MediumSpeed)
                                .withVelocityY(-joystick.getLeftX() * MediumSpeed)
                                .withRotationalRate(rightX * MaxAngularRate);

                                
                }
                else{   

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
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );

       // joystick.a().whileTrue(drivetrain.applyRequest(() -> brake));
       // joystick.b().whileTrue(drivetrain.applyRequest(() ->
       //     point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
       // ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        //joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        //joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        //joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        //joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));
        
        

        joystick.a().onTrue(new ManipuladorCMD(Mcoleta, -0.5));
        joystick.b().onTrue(new ManipuladorCMD(Mcoleta, 0.5));
        //joystick.b().onTrue(new ElevadorCMD(elevSub, 148));
        //joystick.a().onTrue(new ElevadorCMD(elevSub, 0));

        //joystick.x().onTrue(new sequenciaPontua(manipuloSub, Constants.MangP2, Constants.RotP1, Mcoleta, Constants.Velo_Coleta, elevAngSub, Constants.ElevAngC));
        //joystick.x().onTrue(new sequenciaPontua(manipuloSub, Constants.MangP2, Constants.RotP1, Mcoleta, Constants.Velo_Coleta, elevAngSub, Constants.ElevAngC));

        joystick.y().onTrue(new sequenciaPontua(manipuloSub, Constants.Manipulo.PONTUA_PIVO, Constants.Manipulo.PONTUA_ROT, Mcoleta, Constants.Velo_Cospe, elevAngSub, Constants.ElevAng_default, elevSub, Constants.EL0));
        joystick.x().onTrue(new ElevadorCMD(elevSub, 0));

        // ---------------- Manipulador diferencial (angulos em graus) ----------------
        // D-pad cima: vai para a coleta, coleta ate pegar a peca e volta para a posicao inicial
        joystick.povUp().onTrue(new sequenciaColeta(manipuloSub, Mcoleta));
        // D-pad baixo: volta para a posicao inicial (cancela a coleta se estiver rodando)
        joystick.povDown().onTrue(manipuloSub.posicaoInicial());
        // D-pad direita / esquerda: gira o manipulador +90 / -90 graus (pivo mantido)
        joystick.povRight().onTrue(new ManipuloPosCMD(manipuloSub, Double.NaN, 90));
        joystick.povLeft().onTrue(new ManipuloPosCMD(manipuloSub, Double.NaN, -90));
        // Back: considera a posicao atual como 0/0 (use com o manipulador na posicao inicial)
        joystick.back().onTrue(manipuloSub.zerar());

        //joystick.button(6).whileTrue(new ManipuladorCMD(Mcoleta, Constants.Velo_Coleta));
        //joystick.button(5).whileTrue(new ManipuladorCMD(Mcoleta, Constants.Velo_Cospe));




        //joystick.back().whileTrue(new ElevAngCMD(elevAngSub, 0.1));
        //joystick.start().whileTrue(new ElevAngCMD(elevAngSub, -0.1));
        



        
        //joystick.y().onFalse(new ManipuladorCMD(Mcoleta, 0.1));
        //joystick.back().onFalse(new ManipuladorCMD(Mcoleta, -0.1));

        // Reset the field-centric heading on left bumper press.
        //joystick.leftBumper().onTrue(drivetrain.runOnce(drivetrain::seedFieldCentric));

        //drivetrain.registerTelemetry(logger::telemeterize);
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
            drivetrain.applyRequest(() ->
                drive.withVelocityX(0.5)
                    .withVelocityY(0)
                    .withRotationalRate(0)
            )
            .withTimeout(5.0),
            // Finally idle for the rest of auton
            drivetrain.applyRequest(() -> idle)
        );
        
        
    }
}
