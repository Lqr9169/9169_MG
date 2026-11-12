package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ManipuloSub;

/**
 * Leva o manipulador diferencial para uma posicao absoluta (graus).
 * Termina quando pivo e rotacao estiverem dentro da tolerancia.
 * O subsystem continua segurando a posicao depois que o command termina.
 *
 * Passe Double.NaN em um dos eixos para manter o alvo atual daquele eixo.
 */
public class ManipuloPosCMD extends Command {
  private final ManipuloSub manipuloSub;
  private final double pivoGraus;
  private final double rotacaoGraus;

  public ManipuloPosCMD(ManipuloSub manipuloSub, double pivoGraus, double rotacaoGraus) {
    this.manipuloSub = manipuloSub;
    this.pivoGraus = pivoGraus;
    this.rotacaoGraus = rotacaoGraus;
    addRequirements(manipuloSub);
  }

  @Override
  public void initialize() {
    double pivo = Double.isNaN(pivoGraus) ? manipuloSub.getObjetivoPivo() : pivoGraus;
    double rot = Double.isNaN(rotacaoGraus) ? manipuloSub.getObjetivoRotacao() : rotacaoGraus;
    manipuloSub.setObjetivo(pivo, rot);
  }

  @Override
  public boolean isFinished() {
    return manipuloSub.noObjetivo();
  }
}
