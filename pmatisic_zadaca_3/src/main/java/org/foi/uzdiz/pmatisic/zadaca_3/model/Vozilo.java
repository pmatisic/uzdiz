package org.foi.uzdiz.pmatisic.zadaca_3.model;

import java.time.LocalDateTime;
import org.foi.uzdiz.pmatisic.zadaca_3.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_3.state.AktivnoStanjeVozila;
import org.foi.uzdiz.pmatisic.zadaca_3.state.NeaktivnoStanjeVozila;
import org.foi.uzdiz.pmatisic.zadaca_3.state.NeispravnoStanjeVozila;
import org.foi.uzdiz.pmatisic.zadaca_3.state.StanjeVozila;
import org.foi.uzdiz.pmatisic.zadaca_3.visitor.Visitor;

public class Vozilo {

  private String registracija;
  private String opis;
  private double kapacitetTezine;
  private double kapacitetProstora;
  private int redoslijed;
  private double prosjecnaBrzina;
  private String podrucjaPoRangu;
  private StatusVozila status;
  private LocalDateTime vrijemeSljedeceDostave = null;
  private boolean slobodno = true;
  private StanjeVozila trenutnoStanje;
  private double ukupnoOdvozenihKm = 0;
  private int brojHitnihPaketa = 0;
  private int brojObicnihPaketa = 0;
  private int brojIsporucenihPaketa = 0;
  private int brojVoznji = 0;

  public Vozilo(String registracija, String opis, double kapacitetTezine, double kapacitetProstora,
      int redoslijed, double prosjecnaBrzina, String podrucjaPoRangu, StatusVozila status) {
    this.registracija = registracija;
    this.opis = opis;
    this.kapacitetTezine = kapacitetTezine;
    this.kapacitetProstora = kapacitetProstora;
    this.redoslijed = redoslijed;
    this.prosjecnaBrzina = prosjecnaBrzina;
    this.podrucjaPoRangu = podrucjaPoRangu;
    this.status = status;
    postaviStanje(status);
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

  public StanjeVozila getTrenutnoStanje() {
    return this.trenutnoStanje;
  }

  public double getUkupnoOdvozenihKm() {
    return ukupnoOdvozenihKm;
  }

  public int getBrojHitnihPaketa() {
    return brojHitnihPaketa;
  }

  public int getBrojObicnihPaketa() {
    return brojObicnihPaketa;
  }

  public int getBrojIsporucenihPaketa() {
    return brojIsporucenihPaketa;
  }

  public int getBrojVoznji() {
    return brojVoznji;
  }

  public boolean jeSlobodno() {
    return slobodno;
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

  public void setSlobodno(boolean slobodno) {
    this.slobodno = slobodno;
  }

  public void postaviStanje(StatusVozila status) {
    switch (status) {
      case A:
        this.trenutnoStanje = new AktivnoStanjeVozila();
        break;
      case NA:
        this.trenutnoStanje = new NeaktivnoStanjeVozila();
        break;
      case NI:
        this.trenutnoStanje = new NeispravnoStanjeVozila();
        break;
    }
  }

  public String dohvatiOpisStanja() {
    if (trenutnoStanje instanceof AktivnoStanjeVozila) {
      return "Aktivno";
    } else if (trenutnoStanje instanceof NeaktivnoStanjeVozila) {
      return "Neaktivno";
    } else if (trenutnoStanje instanceof NeispravnoStanjeVozila) {
      return "Neispravno";
    }
    return "Nepoznato";
  }

  public boolean ukrcajPaket(Paket paket) {
    return this.trenutnoStanje.ukrcajPaket(this, paket);
  }

  public void promijeniStanje(StanjeVozila novoStanje) {
    this.trenutnoStanje = novoStanje;
  }

  public void dodajOdvozeneKilometre(double km) {
    this.ukupnoOdvozenihKm += km;
  }

  public void povecajBrojPaketa(Paket paket) {
    if (paket.getUslugaDostave() == UslugaDostave.H) {
      brojHitnihPaketa++;
    } else {
      brojObicnihPaketa++;
    }
  }

  public void povecajBrojIsporucenihPaketa() {
    this.brojIsporucenihPaketa++;
  }

  public void povecajBrojVoznji() {
    this.brojVoznji++;
  }

  public void accept(Visitor visitor) {
    visitor.visitVozilo(this);
  }

}
