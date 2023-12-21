package org.foi.uzdiz.pmatisic.zadaca_3.pomagala;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Greske {

  private static int redniBrojGreske = 0;

  public static void logirajGresku(int redniBroj, String red, String opisGreske) {
    redniBrojGreske = redniBroj;
    Logger.getGlobal().log(Level.WARNING,
        "Greška br.: " + redniBrojGreske + ": " + red + " - " + opisGreske);
  }

  public static int getRedniBrojGreske() {
    return redniBrojGreske;
  }

  public static void setRedniBrojGreske(int redniBroj) {
    redniBrojGreske = redniBroj;
  }

}
