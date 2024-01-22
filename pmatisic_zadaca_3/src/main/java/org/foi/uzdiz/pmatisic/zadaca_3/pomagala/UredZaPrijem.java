package org.foi.uzdiz.pmatisic.zadaca_3.pomagala;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.foi.uzdiz.pmatisic.zadaca_3.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_3.builder.PaketBuilder;
import org.foi.uzdiz.pmatisic.zadaca_3.model.PrijemPaketa;
import org.foi.uzdiz.pmatisic.zadaca_3.model.UslugaDostave;
import org.foi.uzdiz.pmatisic.zadaca_3.model.VrstaPaketa;

public class UredZaPrijem {

  private Map<String, VrstaPaketa> mapaVrstaPaketa = new HashMap<>();
  public Map<Paket, Double> mapaCijenaDostave = new HashMap<>();
  private List<Paket> primljeniPaketi = new ArrayList<>();
  public List<Paket> zaprimljeniPaketi = new ArrayList<>();
  private LocalDateTime trenutnoVirtualnoVrijeme;
  private int maxTezina;

  public UredZaPrijem(List<VrstaPaketa> vrstePaketa, int maxTezina) {
    this.maxTezina = maxTezina;
    for (VrstaPaketa vrsta : vrstePaketa) {
      this.mapaVrstaPaketa.put(vrsta.getOznaka(), vrsta);
    }
  }

  public void postaviVirtualnoVrijeme(LocalDateTime vrijeme) {
    this.trenutnoVirtualnoVrijeme = vrijeme;
  }

  public void preuzmiPodatkeIzPrijema(List<PrijemPaketa> prijemi) {
    for (PrijemPaketa prijem : prijemi) {
      Paket paket = new PaketBuilder().oznaka(prijem.getOznaka())
          .vrijemePrijema(prijem.getVrijemePrijema()).posiljatelj(prijem.getPosiljatelj())
          .primatelj(prijem.getPrimatelj()).vrstaPaketa(prijem.getVrstaPaketa())
          .visina(prijem.getVisina()).sirina(prijem.getSirina()).duzina(prijem.getDuzina())
          .tezina(prijem.getTezina()).uslugaDostave(prijem.getUslugaDostave())
          .iznosPouzeca(prijem.getIznosPouzeca()).build();

      if (paket.getTezina() > this.maxTezina) {
        Greske.logirajGresku(Greske.getRedniBrojGreske() + 1,
            "Paket s oznakom " + paket.getOznaka(), "Paket prelazi maksimalnu dopuštenu težinu.");
        continue;
      }

      if (paket.getVrstaPaketa().equals("X")) {
        VrstaPaketa vrsta = mapaVrstaPaketa.get(paket.getVrstaPaketa());
        if (paket.getVisina() > vrsta.getVisina() || paket.getSirina() > vrsta.getSirina()
            || paket.getDuzina() > vrsta.getDuzina()) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1,
              "Paket s oznakom " + paket.getOznaka(),
              "Paket prelazi maksimalne dimenzije za vrstu X.");
          continue;
        }
      }

      primljeniPaketi.add(paket);

      if (paket.getUslugaDostave() != UslugaDostave.P) {
        double cijena = izracunajCijenuDostave(paket);
        mapaCijenaDostave.put(paket, cijena);
      }
    }
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

  public List<Paket> dohvatiPrimljenePakete() {
    List<Paket> filtriraniPaketi = new ArrayList<>();
    for (Paket paket : primljeniPaketi) {
      if (paket.getVrijemePrijema().isBefore(trenutnoVirtualnoVrijeme)
          && !paket.jePoslanZaDostavu()) {
        filtriraniPaketi.add(paket);
        zaprimljeniPaketi.add(paket);
      }
    }
    return filtriraniPaketi;
  }

  public List<Paket> dohvatiPaketeZaObavijesti() {
    List<Paket> filtriraniPaketi = new ArrayList<>();
    for (Paket paket : primljeniPaketi) {
      filtriraniPaketi.add(paket);
    }
    return filtriraniPaketi;
  }

}
