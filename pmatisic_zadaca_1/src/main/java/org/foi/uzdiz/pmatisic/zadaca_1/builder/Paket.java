package org.foi.uzdiz.pmatisic.zadaca_1.builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.foi.uzdiz.pmatisic.zadaca_1.model.UslugaDostave;
import org.foi.uzdiz.pmatisic.zadaca_1.model.VrstaPaketa;

public class Paket {

  private final String oznaka;
  private final LocalDateTime vrijemePrijema;
  private final String posiljatelj;
  private final String primatelj;
  private final String vrstaPaketa;
  private final double visina;
  private final double sirina;
  private final double duzina;
  private final double tezina;
  private final UslugaDostave uslugaDostave;
  private final double iznosPouzeca;

  Paket(String oznaka, LocalDateTime vrijemePrijema, String posiljatelj, String primatelj,
      String vrstaPaketa, double visina, double sirina, double duzina, double tezina,
      UslugaDostave uslugaDostave, double iznosPouzeca) {
    this.oznaka = oznaka;
    this.vrijemePrijema = vrijemePrijema;
    this.posiljatelj = posiljatelj;
    this.primatelj = primatelj;
    this.vrstaPaketa = vrstaPaketa;
    this.visina = visina;
    this.sirina = sirina;
    this.duzina = duzina;
    this.tezina = tezina;
    this.uslugaDostave = uslugaDostave;
    this.iznosPouzeca = iznosPouzeca;
  }

  public String getOznaka() {
    return oznaka;
  }

  public LocalDateTime getVrijemePrijema() {
    return vrijemePrijema;
  }

  public String getPosiljatelj() {
    return posiljatelj;
  }

  public String getPrimatelj() {
    return primatelj;
  }

  public String getVrstaPaketa() {
    return vrstaPaketa;
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

  public double getTezina() {
    return tezina;
  }

  public UslugaDostave getUslugaDostave() {
    return uslugaDostave;
  }

  public double getIznosPouzeca() {
    return iznosPouzeca;
  }

  @Override
  public String toString() {
    return String.format("PrijemPaketa {\noznaka='%s',\nvrijemePrijema='%s',\n...}", oznaka,
        vrijemePrijema);
  }

  public static LocalDateTime konvertirajVrijeme(String vrijeme) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
    return LocalDateTime.parse(vrijeme, formatter);
  }

  public double izracunajCijenuDostave(List<VrstaPaketa> vrstePaketa) {
    for (VrstaPaketa vrsta : vrstePaketa) {
      if (this.vrstaPaketa.equals(vrsta.getOznaka())) {
        if (this.vrstaPaketa.equals("X")) {
          double volumen = this.visina * this.sirina * this.duzina;
          return vrsta.getCijena() + (volumen * vrsta.getCijenaP())
              + (this.tezina * vrsta.getCijenaT());
        } else {
          return vrsta.getCijena();
        }
      }
    }
    return 0.0; // Ako vrsta paketa nije pronađena, vrati 0.0
  }

}
