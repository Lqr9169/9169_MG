package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ElevAngSub;
import frc.robot.subsystems.ElevadorSub;
import frc.robot.subsystems.ManipuladorSub;
import frc.robot.subsystems.ManipuloSub;

public class sequenciaPontua extends SequentialCommandGroup{
    
    public sequenciaPontua(ManipuloSub manipuloSub, double setpointMA, double setpointMR, ManipuladorSub ManipuloSub, double velo, ElevAngSub elevAng, double elevangSet, ElevadorSub elevSub, double elevSet){
        addCommands(
            new ParallelCommandGroup(
                new ElevAngCMD(elevAng, elevangSet),
                new ManipuloRotCMD(manipuloSub, setpointMR)),
            new ManipuloAngCMD(manipuloSub, setpointMA),
            new ElevadorCMD(elevSub, 0),
            new WaitUntilCommand(RobotContainer.joystick.button(5)),
            new ManipuladorCMD(ManipuloSub, velo),
            new ElevadorCMD(elevSub, 0),
            new ParallelCommandGroup(
                new ElevAngCMD(elevAng, Constants.ElevAng_default),
                new ManipuloAngCMD(manipuloSub, -1.27)),
            new ManipuloRotCMD(manipuloSub, setpointMR),
            new ManipuladorCMD(ManipuloSub, velo)
                
        );
        
        
    }

    
}
