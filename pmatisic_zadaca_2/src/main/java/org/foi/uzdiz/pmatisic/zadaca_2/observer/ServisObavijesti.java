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
      pretplatiKorisnikaNaPaket(paket.getPosiljatelj(), paket.getOznaka(), true);
      pretplatiKorisnikaNaPaket(paket.getPrimatelj(), paket.getOznaka(), false);
    }
  }

  private void pretplatiKorisnikaNaPaket(String osoba, String oznaka, boolean jePosiljatelj) {
    getOrCreateSlusac(osoba, jePosiljatelj);
    subscribe(osoba, oznaka);
  }

  public boolean jePretplacen(String osoba, String oznaka) {
    if (!slusaciPoPaketima.containsKey(oznaka)) {
      return false;
    }
    for (Slusac slusac : slusaciPoPaketima.get(oznaka)) {
      if (slusac instanceof SlusacPaketa && ((SlusacPaketa) slusac).getImeOsobe().equals(osoba)) {
        return true;
      }
    }
    return false;
  }

  public Slusac getOrCreateSlusac(String osoba, boolean jePosiljatelj) {
    return slusaciPoImenu.computeIfAbsent(osoba, k -> new SlusacPaketa(osoba, jePosiljatelj));
  }

  public void subscribe(String osoba, String oznaka) {
    Slusac slusac = slusaciPoImenu.get(osoba);
    if (slusac != null) {
      List<Slusac> slusaciPaketa =
          slusaciPoPaketima.computeIfAbsent(oznaka, k -> new ArrayList<>());
      if (!slusaciPaketa.contains(slusac)) {
        slusaciPaketa.add(slusac);
      }
    }
  }

  public void unsubscribe(String osoba, String oznaka) {
    Slusac slusac = slusaciPoImenu.get(osoba);
    if (slusac != null) {
      List<Slusac> slusaciPaketa = slusaciPoPaketima.get(oznaka);
      if (slusaciPaketa != null) {
        slusaciPaketa.remove(slusac);
      }
    }
  }

  public void notifyObservers(String oznaka, String osoba, boolean status) {
    List<Slusac> slusaci = slusaciPoPaketima.getOrDefault(oznaka, new ArrayList<>());
    for (Slusac slusac : slusaci) {
      if (slusac instanceof SlusacPaketa) {
        SlusacPaketa slusacPaketa = (SlusacPaketa) slusac;
        if (slusacPaketa.getImeOsobe().equals(osoba)) {
          slusacPaketa.update(oznaka, status);
        }
      }
    }
  }
}
