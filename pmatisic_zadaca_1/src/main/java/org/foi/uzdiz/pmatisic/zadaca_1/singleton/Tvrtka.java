package org.foi.uzdiz.pmatisic.zadaca_1.singleton;

import java.util.Map;
import org.foi.uzdiz.pmatisic.zadaca_1.pomagala.UredZaDostavu;
import org.foi.uzdiz.pmatisic.zadaca_1.pomagala.UredZaPrijem;

public class Tvrtka {

  private Map<String, String> podatci;
  private static volatile Tvrtka instance;
  private UredZaPrijem uredZaPrijem;
  private UredZaDostavu uredZaDostavu;
  private VirtualniSat virtualniSat;

  private Tvrtka(Map<String, String> podatci) {
    this.podatci = podatci;
  }

  public static Tvrtka getInstance(Map<String, String> noviPodatci) {
    Tvrtka result = instance;
    if (result == null) {
      synchronized (Tvrtka.class) {
        result = instance;
        if (result == null) {
          instance = result = new Tvrtka(noviPodatci);
        }
      }
    } else if (!result.podatci.equals(noviPodatci)) {
      result.updatePodatci(noviPodatci);
    }
    return result;
  }

  private void updatePodatci(Map<String, String> noviPodatci) {
    this.podatci = noviPodatci;
  }

  public Map<String, String> getPodatci() {
    return podatci;
  }

  public UredZaPrijem getUredZaPrijem() {
    return uredZaPrijem;
  }

  public UredZaDostavu getUredZaDostavu() {
    return uredZaDostavu;
  }

  public String getVirtualnoVrijeme() {
    return virtualniSat.dohvatiTrenutnoVrijeme();
  }

}
