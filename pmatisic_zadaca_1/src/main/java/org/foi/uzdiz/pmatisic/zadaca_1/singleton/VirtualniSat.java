package org.foi.uzdiz.pmatisic.zadaca_1.singleton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VirtualniSat {

  private Date trenutnoVrijeme;
  private int mnoziteljSekunde;
  private static volatile VirtualniSat instance;

  private VirtualniSat(String pocetnoVrijeme, int mnoziteljSekunde) throws Exception {
    this.trenutnoVrijeme = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss").parse(pocetnoVrijeme);
    this.mnoziteljSekunde = mnoziteljSekunde;
  }

  public static VirtualniSat getInstance(String pocetnoVrijeme, int mnoziteljSekunde)
      throws Exception {
    VirtualniSat result = instance;
    if (result == null) {
      synchronized (VirtualniSat.class) {
        result = instance;
        if (result == null) {
          instance = result = new VirtualniSat(pocetnoVrijeme, mnoziteljSekunde);
        }
      }
    }
    return result;
  }

  public void povecajVrijeme() {
    long novoVrijeme = trenutnoVrijeme.getTime() + mnoziteljSekunde * 1000L;
    trenutnoVrijeme = new Date(novoVrijeme);
  }

  public String dohvatiTrenutnoVrijeme() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
    return sdf.format(trenutnoVrijeme);
  }

}
