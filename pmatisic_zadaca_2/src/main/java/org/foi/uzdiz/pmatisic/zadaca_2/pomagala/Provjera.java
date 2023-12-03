package org.foi.uzdiz.pmatisic.zadaca_2.pomagala;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

public class Provjera {
  private String putanjaDatoteke;
  private Properties properties;
  private Set<String> ocekivaniKljucevi;

  public Provjera(String putanjaDatoteke) {
    this.putanjaDatoteke = putanjaDatoteke;
    this.properties = new Properties();
    this.ocekivaniKljucevi = new HashSet<>();
    dodajOcekivaneKljuceve();
  }

  private void dodajOcekivaneKljuceve() {
    ocekivaniKljucevi.add("vp");
    ocekivaniKljucevi.add("pv");
    ocekivaniKljucevi.add("pp");
    ocekivaniKljucevi.add("po");
    ocekivaniKljucevi.add("pm");
    ocekivaniKljucevi.add("pu");
    ocekivaniKljucevi.add("pmu");
    ocekivaniKljucevi.add("mt");
    ocekivaniKljucevi.add("vi");
    ocekivaniKljucevi.add("vs");
    ocekivaniKljucevi.add("ms");
    ocekivaniKljucevi.add("pr");
    ocekivaniKljucevi.add("kr");
    ocekivaniKljucevi.add("gps");
    ocekivaniKljucevi.add("isporuka");
  }

  public boolean provjeriDatoteku() {
    try {
      properties.load(new FileInputStream(putanjaDatoteke));
      for (String kljuc : ocekivaniKljucevi) {
        String vrijednost = properties.getProperty(kljuc);
        if (vrijednost != null) {
          vrijednost = vrijednost.trim();
        }
        if (vrijednost == null || !jeVrijednostValidna(kljuc, vrijednost)) {
          System.out.println("Nedostaje ključ ili neispravna vrijednost za ključ: " + kljuc);
          return false;
        }
      }
      for (Object k : properties.keySet()) {
        if (!ocekivaniKljucevi.contains(k.toString())) {
          System.out.println("Nepoznati ključ u datoteci: " + k);
          return false;
        }
      }
      return true;
    } catch (IOException e) {
      System.out.println("Greška pri učitavanju datoteke: " + e.getMessage());
      return false;
    }
  }

  private boolean jeVrijednostValidna(String kljuc, String vrijednost) {
    switch (kljuc) {
      case "vp":
        return Pattern.matches("^[^\\s]+\\.csv$", vrijednost);
      case "pv":
        return Pattern.matches("^[^\\s]+\\.csv$", vrijednost);
      case "pp":
        return Pattern.matches("^[^\\s]+\\.csv$", vrijednost);
      case "po":
        return Pattern.matches("^[^\\s]+\\.csv$", vrijednost);
      case "pm":
        return Pattern.matches("^[^\\s]+\\.csv$", vrijednost);
      case "pu":
        return Pattern.matches("^[^\\s]+\\.csv$", vrijednost);
      case "pmu":
        return Pattern.matches("^[^\\s]+\\.csv$", vrijednost);
      case "mt":
        return Pattern.matches("^\\d+(?=\\s|$)$", vrijednost);
      case "vi":
        return Pattern.matches("^\\d+(?=\\s|$)$", vrijednost);
      case "vs":
        return Pattern.matches(
            "(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.\\d{4}\\. (0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]",
            vrijednost);
      case "ms":
        return Pattern.matches("^\\d+(?=\\s|$)$", vrijednost);
      case "pr":
        return Pattern.matches("(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]", vrijednost);
      case "kr":
        return Pattern.matches("(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]", vrijednost);
      case "gps":
        return Pattern.matches("\\d+\\.\\d+, \\d+\\.\\d+", vrijednost);
      case "isporuka":
        return Pattern.matches("\\d+", vrijednost);
      default:
        return false;
    }
  }

  public Map<String, String> dohvatiPodatke() {
    Map<String, String> podatci = new HashMap<>();
    for (String kljuc : ocekivaniKljucevi) {
      podatci.put(kljuc, properties.getProperty(kljuc));
    }
    return podatci;
  }

}
