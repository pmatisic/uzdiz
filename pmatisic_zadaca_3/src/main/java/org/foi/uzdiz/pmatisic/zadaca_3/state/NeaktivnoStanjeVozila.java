package org.foi.uzdiz.pmatisic.zadaca_3.state;

import org.foi.uzdiz.pmatisic.zadaca_3.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Vozilo;

public class NeaktivnoStanjeVozila extends StanjeVozila {

  @Override
  public boolean ukrcajPaket(Vozilo vozilo, Paket paket) {
    return false;
  }

}
