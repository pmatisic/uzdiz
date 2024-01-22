package org.foi.uzdiz.pmatisic.zadaca_3.decorator;

import org.foi.uzdiz.pmatisic.zadaca_3.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_3.pomagala.UredZaDostavu;
import org.foi.uzdiz.pmatisic.zadaca_3.singleton.Tvrtka;

public class IPProsireniDecorator extends IPBazniDecorator {

  public IPProsireniDecorator(Ispis wrapped) {
    super(wrapped);
  }

  @Override
  public void ispisTablicePrimljenihPaketa() {
    super.ispisTablicePrimljenihPaketa();
    dodajFinancijskuAnalizu();
  }

  private void dodajFinancijskuAnalizu() {
    double ukupanIznosDostave = 0.0;
    double ukupanIznosPouzeca = 0.0;
    int brojDostavljenihPaketa = 0;

    for (Paket paket : Tvrtka.getInstance().uredZaPrijem.zaprimljeniPaketi) {
      if (UredZaDostavu.statusPaketa.getOrDefault(paket.getOznaka(), "Na čekanju")
          .equals("Dostavljeno")) {
        ukupanIznosDostave +=
            Tvrtka.getInstance().uredZaPrijem.mapaCijenaDostave.getOrDefault(paket, 0.0);
        ukupanIznosPouzeca += paket.getIznosPouzeca();
        brojDostavljenihPaketa++;
      }
    }

    double prosjekIznosaPoPaketu = (brojDostavljenihPaketa > 0)
        ? (ukupanIznosDostave + ukupanIznosPouzeca) / brojDostavljenihPaketa
        : 0;

    String prihodi = String.format("\u001B[32mUkupni prihodi: %.2f\u001B[0m", ukupanIznosDostave);
    String pouzeca = String.format("\u001B[32mUkupna pouzeća: %.2f\u001B[0m", ukupanIznosPouzeca);
    String prosjek =
        String.format("\u001B[32mProsjek po paketu: %.2f\u001B[0m", prosjekIznosaPoPaketu);

    System.out.println(prihodi);
    System.out.println(pouzeca);
    System.out.println(prosjek);
  }

}
