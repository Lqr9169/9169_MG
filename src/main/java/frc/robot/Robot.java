// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.HootAutoReplay;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ElevAngSub;
import frc.robot.subsystems.ElevadorSub;
import frc.robot.subsystems.ManipuladorSub;


public class Robot extends TimedRobot {
    private Command m_autonomousCommand;
    SparkMaxConfig globalConfig;
    SparkMaxConfig seguidorElv;
    SparkMaxConfig seguidorElvAng;
    

    public static boolean verifica = false;

    private final RobotContainer m_robotContainer;

    /* log and replay timestamp and joystick data */
    private final HootAutoReplay m_timeAndJoystickReplay = new HootAutoReplay()
        .withTimestampReplay()
        .withJoystickReplay();

    public Robot() {
        m_robotContainer = new RobotContainer();
        
    }

    @Override
    public void robotInit() {
        
        globalConfig = new SparkMaxConfig();
        seguidorElv = new SparkMaxConfig();
        seguidorElvAng = new SparkMaxConfig();

        globalConfig
        .smartCurrentLimit(40)
        .idleMode(IdleMode.kBrake);

        seguidorElv
        .apply(globalConfig)
        .follow(Constants.Elev_E, true);

        seguidorElvAng
        .apply(globalConfig)
        .follow(Constants.ElevAng_E, true);


        ElevadorSub.Elev1.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        ElevadorSub.Elev2.configure(seguidorElv, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        // Motores do manipulador diferencial sao configurados dentro do ManipuloSub.
        ElevAngSub.ElevAngMotorE.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        ElevAngSub.ElevAngMotorD.configure(seguidorElvAng, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        ManipuladorSub.Mdisp.configure(globalConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void robotPeriodic() {
        m_timeAndJoystickReplay.update();
        CommandScheduler.getInstance().run();
        
        
        SmartDashboard.putNumber("encoderElev1", ElevadorSub.Elev1.getEncoder().getPosition());
        SmartDashboard.putNumber("encoderManipulador", ManipuladorSub.Mdisp.getEncoder().getPosition());
        SmartDashboard.putNumber("corrente_manipulo", ManipuladorSub.Mdisp.getOutputCurrent());
        SmartDashboard.putNumber("ang_elev", ElevAngSub.ElevAngMotorE.getEncoder().getPosition());
        
       
    
    }

    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}

    @Override
    public void disabledExit() {}

    @Override
    public void autonomousInit() {
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();

        if (m_autonomousCommand != null) {
            CommandScheduler.getInstance().schedule(m_autonomousCommand);
        }
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void autonomousExit() {}

    @Override
    public void teleopInit() {
        if (m_autonomousCommand != null) {
            CommandScheduler.getInstance().cancel(m_autonomousCommand);
        }

        
    }

    @Override
    public void teleopPeriodic() {}

    @Override
    public void teleopExit() {}

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {}

    @Override
    public void testExit() {}

    @Override
    public void simulationPeriodic() {}
}
