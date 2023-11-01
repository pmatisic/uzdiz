package org.foi.uzdiz.pmatisic.zadaca_1.pomagala;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.pmatisic.zadaca_1.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_1.model.VrstaPaketa;

public class UredZaPrijem {

  private List<Paket> primljeniPaketi = new ArrayList<>();
  private List<VrstaPaketa> vrstePaketa = new ArrayList<>();

  public UredZaPrijem(List<VrstaPaketa> vrstePaketa) {
    this.vrstePaketa = vrstePaketa;
  }

  public void dodajPaket(Paket paket) {
    paket.setVrijemePrijema(LocalDateTime.now());
    this.primljeniPaketi.add(paket);
    if (!paket.getVrstaUsluge().equals("P")) {
      izracunajCijenuDostave(paket);
    }
  }

  private double izracunajCijenuDostave(Paket paket) {
    return paket.izracunajCijenuDostave(vrstePaketa);
  }

}
