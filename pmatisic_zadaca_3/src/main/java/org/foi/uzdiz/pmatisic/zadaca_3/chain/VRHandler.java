package org.foi.uzdiz.pmatisic.zadaca_3.chain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.uzdiz.pmatisic.zadaca_3.singleton.Tvrtka;

public class VRHandler extends Handler {

  @Override
  public boolean handle(String komanda) {
    Pattern pattern = Pattern.compile("^VR\\s+\\d+$");
    Matcher matcher = pattern.matcher(komanda);

    if (matcher.matches()) {
      Tvrtka.getInstance().izvrsiVirtualnoVrijeme(komanda);
      return true;
    }
    return handleNext(komanda);
  }

}
