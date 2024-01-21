package org.foi.uzdiz.pmatisic.zadaca_3.chain;

public class QHandler extends Handler {

  @Override
  public boolean handle(String komanda) {
    if (komanda.equals("Q")) {
      System.out.println("Izlazak iz programa.");
      return false;
    }
    return handleNext(komanda);
  }

}
