package org.foi.uzdiz.pmatisic.zadaca_1.builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.foi.uzdiz.pmatisic.zadaca_1.model.UslugaDostave;

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

  public static LocalDateTime konvertirajVrijeme(String vrijeme) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
    return LocalDateTime.parse(vrijeme, formatter);
  }
  
  @Override
  public String toString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
    return "Paket[" +
           "Oznaka='" + oznaka + '\'' +
           ", VrijemePrijema=" + vrijemePrijema.format(formatter) +
           ", Posiljatelj='" + posiljatelj + '\'' +
           ", Primatelj='" + primatelj + '\'' +
           ", VrstaPaketa='" + vrstaPaketa + '\'' +
           ", Dimenzije=" + visina + "x" + sirina + "x" + duzina +
           ", Tezina=" + tezina +
           ", UslugaDostave=" + (uslugaDostave != null ? uslugaDostave.toString() : "N/A") +
           ", IznosPouzeca=" + iznosPouzeca +
           ']';
  }

}
