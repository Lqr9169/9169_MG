package frc.robot.commands;

import frc.robot.subsystems.ManipuloSub;

/** Move so o PIVO para um angulo absoluto (graus), mantendo a rotacao atual. */
public class ManipuloAngCMD extends ManipuloPosCMD {
  public ManipuloAngCMD(ManipuloSub manipuloSub, double pivoGraus) {
    super(manipuloSub, pivoGraus, Double.NaN);
  }
}
