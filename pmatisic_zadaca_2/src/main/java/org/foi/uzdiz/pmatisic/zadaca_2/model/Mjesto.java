package org.foi.uzdiz.pmatisic.zadaca_2.model;

import java.util.ArrayList;
import java.util.List;

public class Mjesto {

  private int id;
  private String naziv;
  private List<Integer> ulice;

  public Mjesto(int id, String naziv, List<Integer> ulice) {
    this.id = id;
    this.naziv = naziv;
    this.ulice = new ArrayList<>(ulice);
  }

  public int getId() {
    return id;
  }

  public String getNaziv() {
    return naziv;
  }

  public List<Integer> getUlice() {
    return new ArrayList<>(ulice);
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setNaziv(String naziv) {
    this.naziv = naziv;
  }

  public void setUlice(List<Integer> ulice) {
    this.ulice = new ArrayList<>(ulice);
  }

}
