package org.foi.uzdiz.pmatisic.zadaca_2.model;

import java.time.LocalDateTime;

public class Vozilo {

  private String registracija;
  private String opis;
  private double kapacitetTezine;
  private double kapacitetProstora;
  private int redoslijed;
  private double prosjecnaBrzina;
  private String podrucjaPoRangu;
  private StatusVozila status;
  private LocalDateTime vrijemeSljedeceDostave;

  public Vozilo(String registracija, String opis, double kapacitetTezine, double kapacitetProstora,
      int redoslijed, double prosjecnaBrzina, String podrucjaPoRangu, StatusVozila status,
      LocalDateTime vrijemeSljedeceDostave) {
    this.registracija = registracija;
    this.opis = opis;
    this.kapacitetTezine = kapacitetTezine;
    this.kapacitetProstora = kapacitetProstora;
    this.redoslijed = redoslijed;
    this.prosjecnaBrzina = prosjecnaBrzina;
    this.podrucjaPoRangu = podrucjaPoRangu;
    this.status = status;
    this.vrijemeSljedeceDostave = vrijemeSljedeceDostave;
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

  public double getProsjecnaBrzina() {
    return prosjecnaBrzina;
  }

  public String getPodrucjaPoRangu() {
    return podrucjaPoRangu;
  }

  public StatusVozila getStatus() {
    return status;
  }
  
  public LocalDateTime getVrijemeSljedeceDostave() {
    return vrijemeSljedeceDostave;
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

  public void setProsjecnaBrzina(double prosjecnaBrzina) {
    this.prosjecnaBrzina = prosjecnaBrzina;
  }

  public void setPodrucjaPoRangu(String podrucjaPoRangu) {
    this.podrucjaPoRangu = podrucjaPoRangu;
  }

  public void setStatus(StatusVozila status) {
    this.status = status;
  }
  
  public void setVrijemeSljedeceDostave(LocalDateTime vrijemeSljedeceDostave) {
    this.vrijemeSljedeceDostave = vrijemeSljedeceDostave;
  }

  @Override
  public String toString() {
      return "Vozilo{" +
              "registracija='" + registracija + '\'' +
              ", opis='" + opis + '\'' +
              ", kapacitetTezine=" + kapacitetTezine +
              ", kapacitetProstora=" + kapacitetProstora +
              ", redoslijed=" + redoslijed +
              ", prosjecnaBrzina=" + prosjecnaBrzina +
              ", podrucjaPoRangu='" + podrucjaPoRangu + '\'' +
              ", status=" + status +
              ", vrijemeSljedeceDostave=" + vrijemeSljedeceDostave +
              '}';
  }

}
