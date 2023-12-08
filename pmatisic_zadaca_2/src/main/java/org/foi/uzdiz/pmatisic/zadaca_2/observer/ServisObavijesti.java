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

  private void pretplatiKorisnikaNaPaket(String korisnik, String paketId, boolean jePosiljatelj) {
    getOrCreateSlusac(korisnik, jePosiljatelj);
    subscribe(korisnik, paketId);
  }

  public boolean jePretplacen(String osoba, String paketId) {
    if (!slusaciPoPaketima.containsKey(paketId)) {
      return false;
    }

    for (Slusac slusac : slusaciPoPaketima.get(paketId)) {
      if (slusac instanceof SlusacPaketa && ((SlusacPaketa) slusac).getImeOsobe().equals(osoba)) {
        return true;
      }
    }

    return false;
  }

  public Slusac getOrCreateSlusac(String imeOsobe, boolean jePosiljatelj) {
    return slusaciPoImenu.computeIfAbsent(imeOsobe, k -> new SlusacPaketa(imeOsobe, jePosiljatelj));
  }

  public void subscribe(String osoba, String paketId) {
    Slusac slusac = slusaciPoImenu.get(osoba);
    if (slusac != null) {
      List<Slusac> slusaciPaketa =
          slusaciPoPaketima.computeIfAbsent(paketId, k -> new ArrayList<>());
      if (!slusaciPaketa.contains(slusac)) {
        slusaciPaketa.add(slusac);
      }
    }
  }

  public void unsubscribe(String osoba, String paketId) {
    Slusac slusac = slusaciPoImenu.get(osoba);
    if (slusac != null) {
      List<Slusac> slusaciPaketa = slusaciPoPaketima.get(paketId);
      if (slusaciPaketa != null) {
        slusaciPaketa.remove(slusac);
      }
    }
  }

  public void notifyObservers(String paketId, boolean statusObavijesti) {
    List<Slusac> slusaci = slusaciPoPaketima.getOrDefault(paketId, new ArrayList<>());
    for (Slusac slusac : slusaci) {
      slusac.update(paketId, statusObavijesti);
    }
  }

  public void ispisiSveSlusace() {
    System.out.println("Registrirani slušači:");
    for (String imeOsobe : slusaciPoImenu.keySet()) {
      System.out.println("Ime osobe: " + imeOsobe);
    }
  }
}
