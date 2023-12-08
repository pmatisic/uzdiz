package org.foi.uzdiz.pmatisic.zadaca_2.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.foi.uzdiz.pmatisic.zadaca_2.builder.Paket;

public class ServisObavijesti {
  private final Map<String, List<Slusac>> slusaciPoPaketima;
  private final Map<String, Slusac> slusaciPoImenu;

  public ServisObavijesti() {
    slusaciPoPaketima = new HashMap<>();
    slusaciPoImenu = new HashMap<>();
  }

  public void automatskaPretplata(List<Paket> paketi) {
    for (Paket paket : paketi) {
      String posiljatelj = paket.getPosiljatelj();
      String primatelj = paket.getPrimatelj();
      String oznaka = paket.getOznaka();
      SlusacPaketa slusac = (SlusacPaketa) getOrCreateSlusac(oznaka, posiljatelj, primatelj);
      subscribe(oznaka, slusac);
    }
  }

  public SlusacPaketa getOrCreateSlusac(String oznakaPaketa, String posiljatelj, String primatelj) {
    if (!slusaciPoImenu.containsKey(oznakaPaketa)) {
      slusaciPoImenu.put(oznakaPaketa, new SlusacPaketa(oznakaPaketa, posiljatelj, primatelj));
    }
    return (SlusacPaketa) slusaciPoImenu.get(oznakaPaketa);
  }

  public void subscribe(String oznaka, Slusac slusac) {
    slusaciPoPaketima.computeIfAbsent(oznaka, k -> new ArrayList<>()).add(slusac);
  }

  public void unsubscribe(String oznaka, Slusac slusac) {
    List<Slusac> slusaci = slusaciPoPaketima.get(oznaka);
    if (slusaci != null) {
      slusaci.remove(slusac);
    }
  }

  public void notifyObservers(String oznaka, boolean statusObavijesti) {
    if (slusaciPoPaketima.containsKey(oznaka)) {
      slusaciPoPaketima.get(oznaka).forEach(slusac -> slusac.update(oznaka, statusObavijesti));
    }
  }
}
