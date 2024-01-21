package org.foi.uzdiz.pmatisic.zadaca_3.chain;

import org.foi.uzdiz.pmatisic.zadaca_3.singleton.Tvrtka;

public class PPHandler extends Handler {

  @Override
  public boolean handle(String komanda) {
    if (komanda.equals("PP")) {
      Tvrtka.getInstance().uredZaDostavu.ispisiTablicu();
      return true;
    }
    return handleNext(komanda);
  }

}
