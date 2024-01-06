package org.foi.uzdiz.pmatisic.zadaca_2.model;

import java.util.ArrayList;
import java.util.List;

public class Podrucje {

  private int id;
  private List<Par<Integer, String>> gradUlicaParovi;

  public Podrucje(int id, List<Par<Integer, String>> gradUlicaParovi) {
    this.id = id;
    this.gradUlicaParovi = new ArrayList<>(gradUlicaParovi);
  }

  public int getId() {
    return id;
  }

  public List<Par<Integer, String>> getGradUlicaParovi() {
    return new ArrayList<>(gradUlicaParovi);
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setGradUlicaParovi(List<Par<Integer, String>> gradUlicaParovi) {
    this.gradUlicaParovi = new ArrayList<>(gradUlicaParovi);
  }

  public static class Par<T1, T2> {

    private T1 prvi;
    private T2 drugi;

    public Par(T1 prvi, T2 drugi) {
      this.prvi = prvi;
      this.drugi = drugi;
    }

    public T1 getPrvi() {
      return prvi;
    }

    public T2 getDrugi() {
      return drugi;
    }

  }

}
