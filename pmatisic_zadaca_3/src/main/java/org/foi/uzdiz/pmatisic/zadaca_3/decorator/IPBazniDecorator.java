package org.foi.uzdiz.pmatisic.zadaca_3.decorator;

public abstract class IPBazniDecorator implements Ispis {

  private final Ispis wrapped;

  IPBazniDecorator(Ispis wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public void ispisTablicePrimljenihPaketa() {
    wrapped.ispisTablicePrimljenihPaketa();
  }

}
