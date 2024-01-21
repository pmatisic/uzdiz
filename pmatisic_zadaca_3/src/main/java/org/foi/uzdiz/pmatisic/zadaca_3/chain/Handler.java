package org.foi.uzdiz.pmatisic.zadaca_3.chain;

public abstract class Handler {

  private Handler next;

  public Handler setNextHandler(Handler next) {
    this.next = next;
    return next;
  }

  public abstract boolean handle(String komanda);

  protected boolean handleNext(String komanda) {
    if (next == null) {
      return true;
    }
    return next.handle(komanda);
  }

}
