package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ElevAngSub;
import frc.robot.subsystems.ManipuladorSub;
import frc.robot.subsystems.ManipuloSub;

public class sequenciaColeta extends SequentialCommandGroup{
    
    public sequenciaColeta(ManipuloSub manipuloSub, double setpointMA, double setpointMR, ManipuladorSub ManipuloSub, double velo, ElevAngSub elevAng, double elevangSet){
        addCommands(
            new ManipuloRotCMD(manipuloSub, setpointMR),
            new ParallelDeadlineGroup(
                new ElevAngCMD(elevAng, elevangSet),
                new ManipuloAngCMD(manipuloSub, setpointMA)),
            new ManipuladorCMD(ManipuloSub, velo),
            new ParallelDeadlineGroup(
                new ElevAngCMD(elevAng, 0),
                new ManipuloRotCMD(manipuloSub, -setpointMR)),
            new ManipuloAngCMD(manipuloSub, -setpointMA)
        );
    }

    
}
