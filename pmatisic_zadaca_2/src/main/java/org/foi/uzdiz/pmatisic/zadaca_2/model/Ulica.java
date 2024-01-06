package org.foi.uzdiz.pmatisic.zadaca_2.model;

import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.pmatisic.zadaca_2.composite.Prostor;

public class Ulica implements Prostor {

  private int id;
  private String naziv;
  private double gpsLat1;
  private double gpsLon1;
  private double gpsLat2;
  private double gpsLon2;
  private int najveciKucniBroj;

  public Ulica(int id, String naziv, double gpsLat1, double gpsLon1, double gpsLat2, double gpsLon2,
      int najveciKucniBroj) {
    this.id = id;
    this.naziv = naziv;
    this.gpsLat1 = gpsLat1;
    this.gpsLon1 = gpsLon1;
    this.gpsLat2 = gpsLat2;
    this.gpsLon2 = gpsLon2;
    this.najveciKucniBroj = najveciKucniBroj;
  }

  public int getId() {
    return id;
  }

  public String getNaziv() {
    return naziv;
  }

  public double getGpsLat1() {
    return gpsLat1;
  }

  public double getGpsLon1() {
    return gpsLon1;
  }

  public double getGpsLat2() {
    return gpsLat2;
  }

  public double getGpsLon2() {
    return gpsLon2;
  }

  public int getNajveciKucniBroj() {
    return najveciKucniBroj;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setNaziv(String naziv) {
    this.naziv = naziv;
  }

  public void setGpsLat1(double gpsLat1) {
    this.gpsLat1 = gpsLat1;
  }

  public void setGpsLon1(double gpsLon1) {
    this.gpsLon1 = gpsLon1;
  }

  public void setGpsLat2(double gpsLat2) {
    this.gpsLat2 = gpsLat2;
  }

  public void setGpsLon2(double gpsLon2) {
    this.gpsLon2 = gpsLon2;
  }

  public void setNajveciKucniBroj(int najveciKucniBroj) {
    this.najveciKucniBroj = najveciKucniBroj;
  }

  @Override
  public int dohvatiId() {
    return this.id;
  }

  @Override
  public List<Prostor> dohvatiDjecu() {
    return new ArrayList<>();
  }

}
