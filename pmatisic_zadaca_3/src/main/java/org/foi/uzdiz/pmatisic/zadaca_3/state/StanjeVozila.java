package org.foi.uzdiz.pmatisic.zadaca_3.state;

import org.foi.uzdiz.pmatisic.zadaca_3.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Vozilo;

public abstract class StanjeVozila {
  public abstract boolean ukrcajPaket(Vozilo vozilo, Paket paket);
}
