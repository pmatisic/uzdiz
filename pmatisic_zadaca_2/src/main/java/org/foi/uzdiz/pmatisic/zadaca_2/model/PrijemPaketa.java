package org.foi.uzdiz.pmatisic.zadaca_2.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PrijemPaketa {
  
  private String oznaka;
  private LocalDateTime vrijemePrijema;
  private String posiljatelj;
  private String primatelj;
  private String vrstaPaketa;
  private double visina;
  private double sirina;
  private double duzina;
  private double tezina;
  private UslugaDostave uslugaDostave;
  private double iznosPouzeca;

  public PrijemPaketa(String oznaka, LocalDateTime vrijemePrijema, String posiljatelj, String primatelj,
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

  public void setOznaka(String oznaka) {
    this.oznaka = oznaka;
  }

  public void setVrijemePrijema(LocalDateTime vrijemePrijema) {
    this.vrijemePrijema = vrijemePrijema;
  }

  public void setPosiljatelj(String posiljatelj) {
    this.posiljatelj = posiljatelj;
  }

  public void setPrimatelj(String primatelj) {
    this.primatelj = primatelj;
  }

  public void setVrstaPaketa(String vrstaPaketa) {
    this.vrstaPaketa = vrstaPaketa;
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

  public void setTezina(double tezina) {
    this.tezina = tezina;
  }

  public void setUslugaDostave(UslugaDostave uslugaDostave) {
    this.uslugaDostave = uslugaDostave;
  }

  public void setIznosPouzeca(double iznosPouzeca) {
    this.iznosPouzeca = iznosPouzeca;
  }

  public static LocalDateTime konvertirajVrijeme(String vrijeme) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
    return LocalDateTime.parse(vrijeme, formatter);
  }

  @Override
  public String toString() {
      return "PrijemPaketa{" +
              "oznaka='" + oznaka + '\'' +
              ", vrijemePrijema=" + vrijemePrijema +
              ", posiljatelj='" + posiljatelj + '\'' +
              ", primatelj='" + primatelj + '\'' +
              ", vrstaPaketa='" + vrstaPaketa + '\'' +
              ", visina=" + visina +
              ", sirina=" + sirina +
              ", duzina=" + duzina +
              ", tezina=" + tezina +
              ", uslugaDostave=" + uslugaDostave +
              ", iznosPouzeca=" + iznosPouzeca +
              '}';
  }

}
