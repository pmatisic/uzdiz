package org.foi.uzdiz.pmatisic.zadaca_1.builder;

public class PaketBuilder implements Builder {

  private String oznaka;
  private String opis;
  private double visina;
  private double sirina;
  private double duzina;
  private double maksimalnaTezina;
  private double cijena;
  private double cijenaHitno;
  private double cijenaP;
  private double cijenaT;

  @Override
  public PaketBuilder oznaka(String oznaka) {
    this.oznaka = oznaka;
    return this;
  }

  @Override
  public PaketBuilder opis(String opis) {
    this.opis = opis;
    return this;
  }

  @Override
  public PaketBuilder visina(double visina) {
    this.visina = visina;
    return this;
  }

  @Override
  public PaketBuilder sirina(double sirina) {
    this.sirina = sirina;
    return this;
  }

  @Override
  public PaketBuilder duzina(double duzina) {
    this.duzina = duzina;
    return this;
  }

  @Override
  public PaketBuilder maksimalnaTezina(double tezina) {
    this.maksimalnaTezina = tezina;
    return this;
  }

  @Override
  public PaketBuilder cijena(double cijena) {
    this.cijena = cijena;
    return this;
  }

  @Override
  public PaketBuilder cijenaHitno(double cijenaHitno) {
    this.cijenaHitno = cijenaHitno;
    return this;
  }

  @Override
  public PaketBuilder cijenaP(double cijenaP) {
    this.cijenaP = cijenaP;
    return this;
  }

  @Override
  public PaketBuilder cijenaT(double cijenaT) {
    this.cijenaT = cijenaT;
    return this;
  }

  public Paket build() {
    return new Paket(oznaka, opis, visina, sirina, duzina, maksimalnaTezina, cijena, cijenaHitno,
        cijenaP, cijenaT);
  }

}
