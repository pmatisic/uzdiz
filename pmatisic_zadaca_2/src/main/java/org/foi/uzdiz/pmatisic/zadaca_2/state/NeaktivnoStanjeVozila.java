package org.foi.uzdiz.pmatisic.zadaca_2.state;

import org.foi.uzdiz.pmatisic.zadaca_2.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_2.model.Vozilo;

public class NeaktivnoStanjeVozila implements StanjeVozila {

  @Override
  public boolean ukrcajPaket(Vozilo vozilo, Paket paket) {
    return false;
  }

}
