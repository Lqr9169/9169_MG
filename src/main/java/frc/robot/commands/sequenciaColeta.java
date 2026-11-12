package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.ManipuladorSub;
import frc.robot.subsystems.ManipuloSub;

/**
 * 1. Leva o manipulador para a posicao de coleta (pivo + rotacao em graus)
 * 2. Liga a coleta ate a peca ser detectada (pico de corrente no ManipuladorCMD)
 * 3. Volta para a posicao inicial
 */
public class sequenciaColeta extends SequentialCommandGroup {

    public sequenciaColeta(ManipuloSub manipuloSub, ManipuladorSub coletor,
                           double pivoColeta, double rotColeta, double veloColeta) {
        addCommands(
            new ManipuloPosCMD(manipuloSub, pivoColeta, rotColeta),
            new ManipuladorCMD(coletor, veloColeta),
            new ManipuloPosCMD(manipuloSub, Constants.Manipulo.INICIAL_PIVO, Constants.Manipulo.INICIAL_ROT)
        );
    }

    /** Usa as posicoes padrao de Constants.Manipulo. */
    public sequenciaColeta(ManipuloSub manipuloSub, ManipuladorSub coletor) {
        this(manipuloSub, coletor, Constants.Manipulo.COLETA_PIVO, Constants.Manipulo.COLETA_ROT,
             Constants.Velo_Coleta);
    }
}
