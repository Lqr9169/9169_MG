package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.ElevAngSub;
import frc.robot.subsystems.ElevadorSub;
import frc.robot.subsystems.ManipuladorSub;
import frc.robot.subsystems.ManipuloSub;

/**
 * Pivo/rotacao agora sao ANGULOS ABSOLUTOS em graus (0/0 = posicao inicial).
 */
public class sequenciaPontua extends SequentialCommandGroup{

    public sequenciaPontua(ManipuloSub manipuloSub, double pivoGraus, double rotGraus, ManipuladorSub ManipuloSub, double velo, ElevAngSub elevAng, double elevangSet, ElevadorSub elevSub, double elevSet){
        addCommands(
            new ParallelCommandGroup(
                new ElevAngCMD(elevAng, elevangSet),
                new ManipuloPosCMD(manipuloSub, pivoGraus, rotGraus)),
            new ElevadorCMD(elevSub, elevSet),
            new WaitUntilCommand(RobotContainer.joystick.button(5)),
            new ManipuladorCMD(ManipuloSub, velo),
            new ElevadorCMD(elevSub, 0),
            new ParallelCommandGroup(
                new ElevAngCMD(elevAng, Constants.ElevAng_default),
                new ManipuloPosCMD(manipuloSub, Constants.Manipulo.INICIAL_PIVO, Constants.Manipulo.INICIAL_ROT))
        );
    }
}
