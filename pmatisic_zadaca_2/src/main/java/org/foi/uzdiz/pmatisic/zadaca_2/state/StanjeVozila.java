package org.foi.uzdiz.pmatisic.zadaca_2.state;

import org.foi.uzdiz.pmatisic.zadaca_2.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_2.model.Vozilo;

public interface StanjeVozila {
  public boolean ukrcajPaket(Vozilo vozilo, Paket paket);
}
