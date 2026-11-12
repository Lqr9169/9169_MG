package frc.robot.commands;

import frc.robot.subsystems.ManipuloSub;

/** Gira so a ROTACAO para um angulo absoluto (graus), mantendo o pivo atual. */
public class ManipuloRotCMD extends ManipuloPosCMD {
  public ManipuloRotCMD(ManipuloSub manipuloSub, double rotacaoGraus) {
    super(manipuloSub, Double.NaN, rotacaoGraus);
  }
}
