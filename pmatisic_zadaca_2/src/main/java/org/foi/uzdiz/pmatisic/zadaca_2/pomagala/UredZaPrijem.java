package org.foi.uzdiz.pmatisic.zadaca_2.pomagala;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.foi.uzdiz.pmatisic.zadaca_2.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_2.builder.PaketBuilder;
import org.foi.uzdiz.pmatisic.zadaca_2.model.PrijemPaketa;
import org.foi.uzdiz.pmatisic.zadaca_2.model.UslugaDostave;
import org.foi.uzdiz.pmatisic.zadaca_2.model.VrstaPaketa;

public class UredZaPrijem {

  private List<Paket> primljeniPaketi = new ArrayList<>();
  private Map<String, VrstaPaketa> mapaVrstaPaketa = new HashMap<>();
  private Map<Paket, Double> mapaCijenaDostave = new HashMap<>();
  private LocalDateTime trenutnoVirtualnoVrijeme;
  private int maxTezina;

  public UredZaPrijem(List<VrstaPaketa> vrstePaketa, int maxTezina) {
    this.maxTezina = maxTezina;
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

      if (paket.getTezina() > this.maxTezina) {
        Greske.logirajGresku(Greske.getRedniBrojGreske() + 1,
            "Paket s oznakom " + paket.getOznaka(), "Paket prelazi maksimalnu dopuštenu težinu.");
        continue;
      }
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

  public void postaviTrenutnoVirtualnoVrijeme(LocalDateTime vrijeme) {
    this.trenutnoVirtualnoVrijeme = vrijeme;
  }

  public List<Paket> dohvatiPrimljenePakete() {
    List<Paket> filtriraniPaketi = new ArrayList<>();
    for (Paket paket : primljeniPaketi) {
      if (!paket.getVrijemePrijema().isAfter(trenutnoVirtualnoVrijeme)) {
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
        filtriraniPaketi.add(paket);
      }
    }
    return filtriraniPaketi;
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

  public void ispisTablicePrimljenihPaketa(UredZaDostavu uredZaDostavu) {
    System.out.println(
        "+---------------+-------------+-------------+----------------+----------------+--------------+--------------+");
    System.out.println(
        "| Vrijeme prij. | Vrsta pak.  | Vrsta usl.  | Status ispor.  | Vrijeme preuz. | Iznos dost.  | Iznos pouz.  |");
    System.out.println(
        "+---------------+-------------+-------------+----------------+----------------+--------------+--------------+");

    for (Paket paket : dohvatiPrimljenePakete()) {
      String statusIsporuke = (uredZaDostavu.jeIsporucen(paket)) ? "Dostavljeno" : "Na čekanju";
      String vrijemePreuzimanja =
          (uredZaDostavu.jeIsporucen(paket)) ? paket.getVrijemePrijema().toString() : "-";
      Double iznosDostave = mapaCijenaDostave.getOrDefault(paket, 0.0);
      System.out.printf("| %13s | %11s | %11s | %14s | %14s | %12.2f | %12.2f |\n",
          paket.getVrijemePrijema(), paket.getVrstaPaketa(), paket.getUslugaDostave(),
          statusIsporuke, vrijemePreuzimanja, iznosDostave, paket.getIznosPouzeca());
    }
    System.out.println(
        "+---------------+-------------+-------------+----------------+----------------+--------------+--------------+");
  }

}