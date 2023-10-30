package org.foi.uzdiz.pmatisic.zadaca_1.factory;

import java.util.Map;

public class DatotekaFactory {
  public static Datoteka stvoriDatoteku(String kljuc, Map<String, String> argumenti) {
    Datoteka datoteka;
    switch (kljuc) {
      case "vp":
        datoteka = new VrsteDatoteka();
        break;
      case "pv":
        datoteka = new VozilaDatoteka();
        break;
      case "pp":
        datoteka = new PaketiDatoteka();
        break;
      default:
        throw new IllegalArgumentException("Neispravan ključ za datoteku: " + kljuc);
    }
    datoteka.postaviPutanju(argumenti.get(kljuc));
    return datoteka;
  }
}
