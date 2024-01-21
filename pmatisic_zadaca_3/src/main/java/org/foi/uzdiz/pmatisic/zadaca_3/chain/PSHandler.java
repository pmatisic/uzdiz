package org.foi.uzdiz.pmatisic.zadaca_3.chain;

import org.foi.uzdiz.pmatisic.zadaca_3.singleton.Tvrtka;

public class PSHandler extends Handler {

  @Override
  public boolean handle(String komanda) {
    if (komanda.startsWith("PS")) {
      Tvrtka.getInstance().promijeniStanjeVozila(komanda);
      return true;
    }
    return handleNext(komanda);
  }

}
