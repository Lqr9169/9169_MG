package frc.robot;



public class Constants
{
  public static final int Elev_E = 13;
  public static final int Elev_D = 14;
  public static final int Manipulo_AngE = 17;
  public static final int Manipulo_AngD = 18;
  public static final int Manipulador = 19;
  public static final int ElevAng_E = 15;
  public static final int ElevAng_D = 16;


  public static final int EL0 = 0;
  public static final int M1 = 0;
  public static final int M2 = 0;
  public static final int M3 = 0;




  public static final double ElevAng0 = 0;
  public static final double ElevAngP1 = -77;
  public static final double ElevAngC = -81;
  public static final double ElevAng_default = -20;
//-81

  public static final double Velo_Coleta = -0.1;
  public static final double Velo_Cospe = 0.1;
  

  /**
   * Manipulador diferencial (2 NEO + SparkMax, IDs Manipulo_AngE e Manipulo_AngD).
   *
   * Convencao: PIVO = os dois motores giram no mesmo sentido fisico;
   *            ROTACAO = os motores giram em sentidos opostos.
   * Todos os angulos em GRAUS. 0/0 = posicao inicial (onde o robo liga).
   */
  public static final class Manipulo {
    // ---------- CALIBRAR NO ROBO ----------
    // Quantos graus o mecanismo anda por rotacao do motor.
    // Como medir: zere os encoders, gire o mecanismo 90 graus na mao e leia no
    // SmartDashboard "Manipulo/raw_pivo" (ou "raw_rot"). Valor = 90 / leitura.
    public static final double GRAUS_POR_ROT_MOTOR_PIVO = 360.0 / 25.0;    // TODO calibrar
    public static final double GRAUS_POR_ROT_MOTOR_ROTACAO = 360.0 / 25.0; // TODO calibrar

    // Se o pivo/rotacao andar para o lado errado, troque estes sinais.
    public static final boolean INVERTER_MOTOR_E = false;
    public static final boolean INVERTER_MOTOR_D = true; // motores espelhados
    public static final double SINAL_ROTACAO = 1.0;      // troque para -1.0 se a rotacao inverter

    // ---------- Limites de seguranca (graus) ----------
    public static final double PIVO_MIN = -10.0;
    public static final double PIVO_MAX = 120.0;
    public static final double ROT_MIN = -200.0;
    public static final double ROT_MAX = 200.0;

    // ---------- Controle (saida em volts) ----------
    public static final double PIVO_KP = 0.08;   // volts por grau de erro
    public static final double PIVO_KD = 0.002;
    public static final double PIVO_KG = 0.0;    // volts p/ segurar contra gravidade (com pivo na horizontal)
    public static final double PIVO_OFFSET_HORIZONTAL = 0.0; // angulo do pivo quando esta na horizontal
    public static final double PIVO_VEL_MAX = 180.0;  // graus/s
    public static final double PIVO_ACEL_MAX = 360.0; // graus/s^2

    public static final double ROT_KP = 0.05;
    public static final double ROT_KD = 0.001;
    public static final double ROT_VEL_MAX = 360.0;
    public static final double ROT_ACEL_MAX = 720.0;

    public static final double TENSAO_MAX = 6.0;  // limite por motor (aumente depois de testar)
    public static final int LIMITE_CORRENTE = 40;
    public static final double TOLERANCIA_GRAUS = 2.0;

    // ---------- Posicoes (pivo, rotacao) em graus ----------  TODO ajustar no robo
    public static final double INICIAL_PIVO = 0.0;
    public static final double INICIAL_ROT = 0.0;

    public static final double COLETA_PIVO = 90.0;
    public static final double COLETA_ROT = 0.0;

    public static final double PONTUA_PIVO = 45.0;
    public static final double PONTUA_ROT = 90.0;
  }
}
