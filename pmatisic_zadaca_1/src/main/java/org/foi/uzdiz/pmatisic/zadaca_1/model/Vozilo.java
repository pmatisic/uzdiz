package org.foi.uzdiz.pmatisic.zadaca_1.model;

public class Vozilo {
  
  private String registracija;
  private String opis;
  private double kapacitetTezine;
  private double kapacitetProstora;
  private int redoslijed;

  public Vozilo(String registracija, String opis, double kapacitetTezine, double kapacitetProstora,
      int redoslijed) {
    this.registracija = registracija;
    this.opis = opis;
    this.kapacitetTezine = kapacitetTezine;
    this.kapacitetProstora = kapacitetProstora;
    this.redoslijed = redoslijed;
  }

  public String getRegistracija() {
    return registracija;
  }

  public String getOpis() {
    return opis;
  }

  public double getKapacitetTezine() {
    return kapacitetTezine;
  }

  public double getKapacitetProstora() {
    return kapacitetProstora;
  }

  public int getRedoslijed() {
    return redoslijed;
  }

  public void setRegistracija(String registracija) {
    this.registracija = registracija;
  }

  public void setOpis(String opis) {
    this.opis = opis;
  }

  public void setKapacitetTezine(double kapacitetTezine) {
    this.kapacitetTezine = kapacitetTezine;
  }

  public void setKapacitetProstora(double kapacitetProstora) {
    this.kapacitetProstora = kapacitetProstora;
  }

  public void setRedoslijed(int redoslijed) {
    this.redoslijed = redoslijed;
  }

  @Override
  public String toString() {
      return "Vozilo{" +
              "registracija='" + registracija + '\'' +
              ", opis='" + opis + '\'' +
              ", kapacitetTezine=" + kapacitetTezine +
              ", kapacitetProstora=" + kapacitetProstora +
              ", redoslijed=" + redoslijed +
              '}';
  }

}
