package org.foi.uzdiz.pmatisic.zadaca_1.builder;

public interface Builder {
  Builder oznaka(String oznaka);

  Builder opis(String opis);

  Builder visina(double visina);

  Builder sirina(double sirina);

  Builder duzina(double duzina);

  Builder maksimalnaTezina(double tezina);

  Builder cijena(double cijena);

  Builder cijenaHitno(double cijenaHitno);

  Builder cijenaP(double cijenaP);

  Builder cijenaT(double cijenaT);
}
