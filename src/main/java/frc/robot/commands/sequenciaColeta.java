package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.subsystems.ElevAngSub;
import frc.robot.subsystems.ElevadorSub;
import frc.robot.subsystems.ManipuladorSub;
import frc.robot.subsystems.ManipuloSub;

public class sequenciaColeta extends SequentialCommandGroup{
    
    public sequenciaColeta(ManipuloSub manipuloSub, double setpointMA, double setpointMR, ManipuladorSub ManipuloSub, double velo, ElevAngSub elevAng, double elevangSet, ElevadorSub elev, double SetElev){
        addCommands(
            new ElevadorCMD(elev, SetElev),
            new ParallelCommandGroup(
                new ManipuloRotCMD(manipuloSub, setpointMR),
                new ElevAngCMD(elevAng, elevangSet)
            ),
            new Inclinacao(manipuloSub, setpointMA),
            new ManipuladorColetaCMD(ManipuloSub, velo),
            new WaitCommand(1),
            new ParallelCommandGroup(
                new ElevAngCMD(elevAng, Constants.Ang_Default),
                new Inclinacao(manipuloSub, Constants.Inc_Default)
            ),
            new ManipuloRotCMD(manipuloSub, Constants.Rot0)
            
        );
    }

    
}
