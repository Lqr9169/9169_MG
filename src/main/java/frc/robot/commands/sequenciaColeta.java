package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.ElevAngSub;
import frc.robot.subsystems.ManipuladorSub;
import frc.robot.subsystems.ManipuloSub;

public class sequenciaColeta extends SequentialCommandGroup{
    
    public sequenciaColeta(ManipuloSub manipuloSub, double setpointMA, double setpointMR, ManipuladorSub ManipuloSub, double velo, ElevAngSub elevAng, double elevangSet){
        addCommands(
            new ParallelCommandGroup(
                new ManipuladorColetaCMD(ManipuloSub, velo),
                new SequentialCommandGroup(
                    new Inclinacao(manipuloSub, setpointMA),
                    new ElevAngCMD(elevAng, elevangSet),
                    new ElevAngCMD(elevAng, 0),
                    new ManipuloRotCMD(manipuloSub, setpointMR)
                )
            )
        );
    }

    
}
