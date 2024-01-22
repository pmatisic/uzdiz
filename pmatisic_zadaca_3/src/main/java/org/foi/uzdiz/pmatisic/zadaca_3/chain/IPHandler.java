package org.foi.uzdiz.pmatisic.zadaca_3.chain;

import org.foi.uzdiz.pmatisic.zadaca_3.decorator.IPProsireniDecorator;
import org.foi.uzdiz.pmatisic.zadaca_3.decorator.Ispis;
import org.foi.uzdiz.pmatisic.zadaca_3.decorator.IspisPaketa;

public class IPHandler extends Handler {

  @Override
  public boolean handle(String komanda) {
    if (komanda.matches("^IP( B)?$")) {
      Ispis ispis = new IspisPaketa();
      if (komanda.equals("IP B")) {
        ispis = new IPProsireniDecorator(ispis);
      }
      ispis.ispisTablicePrimljenihPaketa();
      return true;
    }
    return handleNext(komanda);
  }

}
