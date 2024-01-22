package org.foi.uzdiz.pmatisic.zadaca_3.visitor;

import org.foi.uzdiz.pmatisic.zadaca_3.model.Vozilo;
import org.foi.uzdiz.pmatisic.zadaca_3.singleton.Tvrtka;

public class IspisVisitor implements Visitor {

  @Override
  public void visitVozilo(Vozilo vozilo) {
    double postotakZauzecaProstora =
        (Tvrtka.getInstance().uredZaDostavu.dohvatiTrenutniVolumenVozila(vozilo)
            / vozilo.getKapacitetProstora()) * 100;
    double postotakZauzecaTezine =
        (Tvrtka.getInstance().uredZaDostavu.dohvatiTrenutnuTezinuVozila(vozilo)
            / vozilo.getKapacitetTezine()) * 100;
    String opisStanja = vozilo.dohvatiOpisStanja();

    System.out.printf(
        "| %-15s | %-30s | %-12s | %-14.2f | %-12d | %-13d | %-17d | %-13.2f | %-12.2f | %-10d |\n",
        vozilo.getRegistracija(), vozilo.getOpis(), opisStanja, vozilo.getUkupnoOdvozenihKm(),
        vozilo.getBrojHitnihPaketa(), vozilo.getBrojObicnihPaketa(),
        vozilo.getBrojIsporucenihPaketa(), postotakZauzecaProstora, postotakZauzecaTezine,
        vozilo.getBrojVoznji());
  }

}
