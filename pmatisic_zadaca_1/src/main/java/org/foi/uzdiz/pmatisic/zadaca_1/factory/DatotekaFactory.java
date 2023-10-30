package org.foi.uzdiz.pmatisic.zadaca_1.factory;

import java.util.Map;

public class DatotekaFactory {
  public static Datoteka stvoriDatoteku(String kljuc, Map<String, String> argumenti) {
    Datoteka datoteka;
    switch (kljuc) {
      case "vp":
        datoteka = new VrstePaketaDatoteka();
        break;
      case "pv":
        datoteka = new VoziloDatoteka();
        break;
      case "pp":
        datoteka = new PrijemPaketaDatoteka();
        break;
      default:
        throw new IllegalArgumentException("Neispravan ključ za datoteku: " + kljuc);
    }
    datoteka.postaviPutanju(argumenti.get(kljuc));
    return datoteka;
  }
}
