package org.foi.uzdiz.pmatisic.zadaca_3.composite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Mjesto;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Ulica;

public class CompositeProstor implements Prostor {

  private int id;
  private final List<Prostor> prostori;

  public CompositeProstor(int id) {
    this.id = id;
    this.prostori = new ArrayList<>();
  }

  @Override
  public int dohvatiId() {
    return this.id;
  }

  @Override
  public List<Prostor> dohvatiDjecu() {
    return prostori;
  }

  public void dodajPodrucje(Prostor podrucje) {
    prostori.add(podrucje);
  }

  public int dohvatiPodrucjePremaUlici(int ulicaId) {
    for (Prostor child : prostori) {
      if (child instanceof Mjesto) {
        Mjesto mjesto = (Mjesto) child;
        for (int idUlice : mjesto.getUlice()) {
          if (idUlice == ulicaId) {
            return this.id;
          }
        }
      } else if (child instanceof CompositeProstor) {
        int result = ((CompositeProstor) child).dohvatiPodrucjePremaUlici(ulicaId);
        if (result != 0)
          return result;
      }
    }
    return 0;
  }

  public Map<String, Double> dohvatiGPSzaNajveciKBR(int ulicaId, List<Ulica> sveUlice) {
    for (Prostor child : prostori) {
      if (child instanceof Mjesto) {
        Mjesto mjesto = (Mjesto) child;
        for (int idUlice : mjesto.getUlice()) {
          if (idUlice == ulicaId) {
            for (Ulica ulica : sveUlice) {
              if (ulica.getId() == ulicaId) {
                Map<String, Double> podatci = new HashMap<>();
                podatci.put("lat1", ulica.getGpsLat1());
                podatci.put("lon1", ulica.getGpsLon1());
                podatci.put("lat2", ulica.getGpsLat2());
                podatci.put("lon2", ulica.getGpsLon2());
                podatci.put("nkb", (double) ulica.getNajveciKucniBroj());
                return podatci;
              }
            }
          }
        }
      } else if (child instanceof CompositeProstor) {
        Map<String, Double> result =
            ((CompositeProstor) child).dohvatiGPSzaNajveciKBR(ulicaId, sveUlice);
        if (result != null)
          return result;
      }
    }
    return null;
  }

}
