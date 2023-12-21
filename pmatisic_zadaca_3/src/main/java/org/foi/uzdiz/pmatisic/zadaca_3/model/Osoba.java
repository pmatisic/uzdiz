package org.foi.uzdiz.pmatisic.zadaca_3.model;

public class Osoba {

  private String ime;
  private int gradId;
  private int ulicaId;
  private int kucniBroj;

  public Osoba(String ime, int gradId, int ulicaId, int kucniBroj) {
    this.ime = ime;
    this.gradId = gradId;
    this.ulicaId = ulicaId;
    this.kucniBroj = kucniBroj;
  }

  public String getIme() {
    return ime;
  }

  public int getGradId() {
    return gradId;
  }

  public int getUlicaId() {
    return ulicaId;
  }

  public int getKucniBroj() {
    return kucniBroj;
  }

  public void setIme(String ime) {
    this.ime = ime;
  }

  public void setGradId(int gradId) {
    this.gradId = gradId;
  }

  public void setUlicaId(int ulicaId) {
    this.ulicaId = ulicaId;
  }

  public void setKucniBroj(int kucniBroj) {
    this.kucniBroj = kucniBroj;
  }

}
