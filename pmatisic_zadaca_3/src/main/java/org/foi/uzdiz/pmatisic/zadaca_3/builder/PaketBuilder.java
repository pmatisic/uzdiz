package org.foi.uzdiz.pmatisic.zadaca_3.builder;

import java.time.LocalDateTime;
import org.foi.uzdiz.pmatisic.zadaca_3.model.UslugaDostave;

public class PaketBuilder implements Builder {

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

  @Override
  public PaketBuilder oznaka(String oznaka) {
    this.oznaka = oznaka;
    return this;
  }

  @Override
  public PaketBuilder vrijemePrijema(LocalDateTime vrijemePrijema) {
    this.vrijemePrijema = vrijemePrijema;
    return this;
  }

  public PaketBuilder posiljatelj(String posiljatelj) {
    this.posiljatelj = posiljatelj;
    return this;
  }

  public PaketBuilder primatelj(String primatelj) {
    this.primatelj = primatelj;
    return this;
  }

  public PaketBuilder vrstaPaketa(String vrstaPaketa) {
    this.vrstaPaketa = vrstaPaketa;
    return this;
  }

  public PaketBuilder visina(double visina) {
    this.visina = visina;
    return this;
  }

  public PaketBuilder sirina(double sirina) {
    this.sirina = sirina;
    return this;
  }

  public PaketBuilder duzina(double duzina) {
    this.duzina = duzina;
    return this;
  }

  public PaketBuilder tezina(double tezina) {
    this.tezina = tezina;
    return this;
  }

  public PaketBuilder uslugaDostave(UslugaDostave uslugaDostave) {
    this.uslugaDostave = uslugaDostave;
    return this;
  }

  public PaketBuilder iznosPouzeca(double iznosPouzeca) {
    this.iznosPouzeca = iznosPouzeca;
    return this;
  }

  public Paket build() {
    Paket paket = new Paket(oznaka, vrijemePrijema, posiljatelj, primatelj, vrstaPaketa, visina,
        sirina, duzina, tezina, uslugaDostave, iznosPouzeca);

    return paket;
  }

}
