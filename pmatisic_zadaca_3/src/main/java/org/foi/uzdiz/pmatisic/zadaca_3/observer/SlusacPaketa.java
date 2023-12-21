package org.foi.uzdiz.pmatisic.zadaca_3.observer;

public class SlusacPaketa implements Slusac {
  private final String imeOsobe;
  private final boolean jePosiljatelj;

  public SlusacPaketa(String imeOsobe, boolean jePosiljatelj) {
    this.imeOsobe = imeOsobe;
    this.jePosiljatelj = jePosiljatelj;
  }

  @Override
  public void update(String oznaka, boolean status) {
    String statusPoruka = status ? "želi" : "ne želi";
    String uloga = jePosiljatelj ? "Pošiljatelj" : "Primatelj";

    System.out.println(uloga + " " + imeOsobe + " " + statusPoruka + " primati obavijesti za paket "
        + oznaka + ".");
  }

  public String getImeOsobe() {
    return imeOsobe;
  }
}
