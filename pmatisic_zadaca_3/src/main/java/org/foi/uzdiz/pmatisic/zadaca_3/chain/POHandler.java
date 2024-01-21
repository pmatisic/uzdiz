package org.foi.uzdiz.pmatisic.zadaca_3.chain;

import org.foi.uzdiz.pmatisic.zadaca_3.singleton.Tvrtka;

public class POHandler extends Handler {

  @Override
  public boolean handle(String komanda) {
    if (komanda.startsWith("PO")) {
      Tvrtka.getInstance().promijeniStatusObavijesti(komanda);
      return true;
    }
    return handleNext(komanda);
  }

}
