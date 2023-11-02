package org.foi.uzdiz.pmatisic.zadaca_1.pomagala;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.foi.uzdiz.pmatisic.zadaca_1.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_1.builder.PaketBuilder;
import org.foi.uzdiz.pmatisic.zadaca_1.model.PrijemPaketa;
import org.foi.uzdiz.pmatisic.zadaca_1.model.UslugaDostave;
import org.foi.uzdiz.pmatisic.zadaca_1.model.VrstaPaketa;

public class UredZaPrijem {

  private List<Paket> primljeniPaketi = new ArrayList<>();
  private Map<String, VrstaPaketa> mapaVrstaPaketa = new HashMap<>();
  private Map<Paket, Double> mapaCijenaDostave = new HashMap<>();

  public UredZaPrijem(List<VrstaPaketa> vrstePaketa) {
    for (VrstaPaketa vrsta : vrstePaketa) {
      mapaVrstaPaketa.put(vrsta.getOznaka(), vrsta);
    }
  }

  public void preuzmiPodatkeIzPrijema(List<PrijemPaketa> prijemi) {
    for (PrijemPaketa prijem : prijemi) {
      Paket paket = new PaketBuilder().oznaka(prijem.getOznaka())
          .vrijemePrijema(prijem.getVrijemePrijema()).posiljatelj(prijem.getPosiljatelj())
          .primatelj(prijem.getPrimatelj()).vrstaPaketa(prijem.getVrstaPaketa())
          .visina(prijem.getVisina()).sirina(prijem.getSirina()).duzina(prijem.getDuzina())
          .tezina(prijem.getTezina()).uslugaDostave(prijem.getUslugaDostave())
          .iznosPouzeca(prijem.getIznosPouzeca()).build();

      dodajPaket(paket);
    }
  }

  public void dodajPaket(Paket paket) {
    primljeniPaketi.add(paket);
    if (paket.getUslugaDostave() != UslugaDostave.P) {
      double cijena = izracunajCijenuDostave(paket);
      mapaCijenaDostave.put(paket, cijena);
    }
  }

  public Double dohvatiCijenuDostaveZaPaket(Paket paket) {
    return mapaCijenaDostave.getOrDefault(paket, null);
  }

  public List<Paket> dohvatiPrimljenePakete() {
    return new ArrayList<>(primljeniPaketi);
  }

  public double izracunajCijenuDostave(Paket paket) {
    VrstaPaketa vrsta = mapaVrstaPaketa.get(paket.getVrstaPaketa());

    double osnovnaCijena = 0;

    if (paket.getVrstaPaketa().equals("X")) {
      double volumen = paket.getVisina() * paket.getSirina() * paket.getDuzina();
      osnovnaCijena = vrsta.getCijena() + (volumen * vrsta.getCijenaP())
          + (paket.getTezina() * vrsta.getCijenaT());
    } else {
      osnovnaCijena = vrsta.getCijena();
    }

    if (paket.getUslugaDostave() == UslugaDostave.H) {
      osnovnaCijena = vrsta.getCijenaHitno();
    }

    return osnovnaCijena;
  }

}
