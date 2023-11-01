package org.foi.uzdiz.pmatisic.zadaca_1.pomagala;

import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.pmatisic.zadaca_1.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_1.model.Vozilo;

public class UredZaDostavu {

  private List<Vozilo> vozniPark;
  private int vrijemeIsporuke;

  public UredZaDostavu(int vrijemeIsporuke) {
    this.vozniPark = new ArrayList<>();
    this.vrijemeIsporuke = vrijemeIsporuke; // Vrijeme koje je potrebno za isporuku jednog paketa
  }

  public void dodajVozilo(Vozilo vozilo) {
    this.vozniPark.add(vozilo);
  }

  public void ukrcavanjePaketa(Paket paket) {
    for (Vozilo vozilo : this.vozniPark) {
      if (vozilo.jeSlobodno() && vozilo.mozeUkrcati(paket)) {
        vozilo.ukrcavanje(paket);
        if (paket.getUsluga().equals("H")) { // H za hitnu dostavu (pretpostavka)
          break; // Hitni paket je ukrcan, izađi iz petlje
        }
      }
    }
  }

  public void isporuciPakete() {
    for (Vozilo vozilo : this.vozniPark) {
      if (vozilo.jeZaIsporuku()) {
        vozilo.isporuci();
      }
    }
  }

}
