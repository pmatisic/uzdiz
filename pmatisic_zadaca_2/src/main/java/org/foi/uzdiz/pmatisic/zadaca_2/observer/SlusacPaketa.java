package org.foi.uzdiz.pmatisic.zadaca_2.observer;

public class SlusacPaketa implements Slusac {
  private String oznaka;
  private String posiljatelj;
  private String primatelj;

  public SlusacPaketa(String oznaka, String posiljatelj, String primatelj) {
    this.oznaka = oznaka;
    this.posiljatelj = posiljatelj;
    this.primatelj = primatelj;
  }

  @Override
  public void update(String oznaka, boolean statusObavijesti) {
    if (this.oznaka.equals(oznaka)) {
      String statusPorukaPosiljatelja = statusObavijesti ? "želi" : "ne želi";
      String statusPorukaPrimatelja = statusObavijesti ? "želi" : "ne želi";

      System.out.println("Pošiljatelj " + posiljatelj + " " + statusPorukaPosiljatelja
          + " primati obavijesti za paket " + oznaka + ".");
      System.out.println("Primatelj " + primatelj + " " + statusPorukaPrimatelja
          + " primati obavijesti za paket " + oznaka + ".");
    }
  }
}
