package org.foi.uzdiz.pmatisic.zadaca_2.observer;

public class SlusacPaketa implements Slusac {
  private final String imeOsobe;
  private final boolean jePosiljatelj;

  public SlusacPaketa(String imeOsobe, boolean jePosiljatelj) {
    this.imeOsobe = imeOsobe;
    this.jePosiljatelj = jePosiljatelj;
  }

  @Override
  public void update(String paketId, boolean statusObavijesti) {
    String statusPoruka = statusObavijesti ? "želi" : "ne želi";
    String uloga = jePosiljatelj ? "Pošiljatelj" : "Primatelj";

    System.out.println(uloga + " " + imeOsobe + " " + statusPoruka + " primati obavijesti za paket "
        + paketId + ".");
  }

  public String getImeOsobe() {
    return imeOsobe;
  }
}
