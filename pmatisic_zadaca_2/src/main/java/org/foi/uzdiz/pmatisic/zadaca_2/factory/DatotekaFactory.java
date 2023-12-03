package org.foi.uzdiz.pmatisic.zadaca_2.factory;

import java.util.Map;

public class DatotekaFactory {

  @SuppressWarnings("unchecked")
  public static <T> Datoteka<T> stvoriDatoteku(String kljuc, Map<String, String> argumenti) {
    Datoteka<T> datoteka;
    switch (kljuc) {
      case "vp":
        datoteka = (Datoteka<T>) new VrstaPaketaDatoteka();
        break;
      case "pv":
        datoteka = (Datoteka<T>) new VoziloDatoteka();
        break;
      case "pp":
        datoteka = (Datoteka<T>) new PrijemPaketaDatoteka();
        break;
      default:
        throw new IllegalArgumentException("Neispravan ključ za datoteku: " + kljuc);
    }
    datoteka.postaviPutanju(argumenti.get(kljuc));
    return datoteka;
  }

}
