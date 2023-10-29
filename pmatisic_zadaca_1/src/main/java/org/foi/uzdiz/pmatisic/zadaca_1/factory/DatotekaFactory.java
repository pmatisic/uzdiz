package org.foi.uzdiz.pmatisic.zadaca_1.factory;

public class DatotekaFactory {
  public static Datoteka stvoriDatoteku(String kljuc) {
    switch (kljuc) {
      case "vp":
        return new VrsteDatoteka();
      case "pv":
        return new VozilaDatoteka();
      case "pp":
        return new PaketiDatoteka();
      default:
        throw new IllegalArgumentException("Neispravan ključ za datoteku: " + kljuc);
    }
  }
}
