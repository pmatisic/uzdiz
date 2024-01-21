package org.foi.uzdiz.pmatisic.zadaca_3.chain;

import org.foi.uzdiz.pmatisic.zadaca_3.singleton.Tvrtka;

public class IPHandler extends Handler {

  @Override
  public boolean handle(String komanda) {
    if (komanda.equals("IP")) {
      Tvrtka.getInstance().uredZaPrijem.ispisTablicePrimljenihPaketa();
      return true;
    }
    if (komanda.equals("IP B")) {
      Tvrtka.getInstance().uredZaPrijem.ispisTablicePrimljenihPaketa();
      return true;
    }
    return handleNext(komanda);
  }

}
