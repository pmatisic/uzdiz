package org.foi.uzdiz.pmatisic.zadaca_1.model;

public class VrstaPaketa {
  
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

  public VrstaPaketa(String oznaka, String opis, double visina, double sirina, double duzina,
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

  public String getOznaka() {
    return oznaka;
  }

  public String getOpis() {
    return opis;
  }

  public double getVisina() {
    return visina;
  }

  public double getSirina() {
    return sirina;
  }

  public double getDuzina() {
    return duzina;
  }

  public double getMaksimalnaTezina() {
    return maksimalnaTezina;
  }

  public double getCijena() {
    return cijena;
  }

  public double getCijenaHitno() {
    return cijenaHitno;
  }

  public double getCijenaP() {
    return cijenaP;
  }

  public double getCijenaT() {
    return cijenaT;
  }

  public void setOznaka(String oznaka) {
    this.oznaka = oznaka;
  }

  public void setOpis(String opis) {
    this.opis = opis;
  }

  public void setVisina(double visina) {
    this.visina = visina;
  }

  public void setSirina(double sirina) {
    this.sirina = sirina;
  }

  public void setDuzina(double duzina) {
    this.duzina = duzina;
  }

  public void setMaksimalnaTezina(double maksimalnaTezina) {
    this.maksimalnaTezina = maksimalnaTezina;
  }

  public void setCijena(double cijena) {
    this.cijena = cijena;
  }

  public void setCijenaHitno(double cijenaHitno) {
    this.cijenaHitno = cijenaHitno;
  }

  public void setCijenaP(double cijenaP) {
    this.cijenaP = cijenaP;
  }

  public void setCijenaT(double cijenaT) {
    this.cijenaT = cijenaT;
  }

  @Override
  public String toString() {
      return "VrstaPaketa{" +
              "oznaka='" + oznaka + '\'' +
              ", opis='" + opis + '\'' +
              ", visina=" + visina +
              ", sirina=" + sirina +
              ", duzina=" + duzina +
              ", maksimalnaTezina=" + maksimalnaTezina +
              ", cijena=" + cijena +
              ", cijenaHitno=" + cijenaHitno +
              ", cijenaP=" + cijenaP +
              ", cijenaT=" + cijenaT +
              '}';
  }

}
