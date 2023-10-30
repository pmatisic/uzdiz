package org.foi.uzdiz.pmatisic.zadaca_1.builder;

public class Paket {

  private final String oznaka;
  private final String opis;
  private final double visina;
  private final double sirina;
  private final double duzina;
  private final double maksimalnaTezina;
  private final double cijena;
  private final double cijenaHitno;
  private final double cijenaP;
  private final double cijenaT;

  Paket(String oznaka, String opis, double visina, double sirina, double duzina,
      double maksimalnaTezina, double cijena, double cijenaHitno, double cijenaP, double cijenaT) {
    this.oznaka = oznaka;
    this.opis = opis;
    this.visina = visina;
    this.sirina = sirina;
    this.duzina = duzina;
    this.maksimalnaTezina = maksimalnaTezina;
    this.cijena = cijena;
    this.cijenaHitno = cijenaHitno;
    this.cijenaP = cijenaP;
    this.cijenaT = cijenaT;
  }

  @Override
  public String toString() {
    return """
        Paket {
            oznaka=%s,
            opis=%s,
            visina=%s,
            sirina=%s,
            duzina=%s,
            maksimalnaTezina=%s,
            cijena=%s,
            cijenaHitno=%s,
            cijenaP=%s,
            cijenaT=%s
        }""".formatted(oznaka, opis, visina, sirina, duzina, maksimalnaTezina, cijena, cijenaHitno,
        cijenaP, cijenaT);
  }

}
