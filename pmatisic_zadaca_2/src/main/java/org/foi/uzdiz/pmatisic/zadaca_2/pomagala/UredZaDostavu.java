package org.foi.uzdiz.pmatisic.zadaca_2.pomagala;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.foi.uzdiz.pmatisic.zadaca_2.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_2.model.UslugaDostave;
import org.foi.uzdiz.pmatisic.zadaca_2.model.Vozilo;

public class UredZaDostavu {

  private int vrijemeIsporuke;
  private List<Vozilo> vozila;
  private Queue<Paket> paketiZaDostavu;
  private LocalDateTime trenutnoVirtualnoVrijeme;
  private List<Paket> isporuceniPaketi = new ArrayList<>();
  private Map<Paket, Double> cijeneDostave = new HashMap<>();
  private Map<Paket, Long> vrijemeUkrcavanja = new HashMap<>();
  private Map<Vozilo, List<Paket>> ukrcaniPaketi = new HashMap<>();
  private Map<Vozilo, Double> kapacitetTezineVozila = new HashMap<>();
  private Map<Vozilo, Double> kapacitetVolumenaVozila = new HashMap<>();
  private Map<Vozilo, Double> prikupljeniNovacPoVozilu = new HashMap<>();

  public UredZaDostavu(List<Vozilo> vozila, int vrijemeIsporuke, List<Paket> paketi,
      Map<Paket, Double> cijene) {
    if (vozila != null) {
      this.vozila = vozila;
      Collections.sort(this.vozila, Comparator.comparing(Vozilo::getRedoslijed));
    } else {
      this.vozila = new ArrayList<>();
    }

    if (paketi != null) {
      List<Paket> sortiranaLista = new ArrayList<>(paketi);
      Collections.sort(sortiranaLista, new Comparator<Paket>() {
        @Override
        public int compare(Paket p1, Paket p2) {
          boolean p1Hitno = p1.getUslugaDostave() == UslugaDostave.H;
          boolean p2Hitno = p2.getUslugaDostave() == UslugaDostave.H;
          return Boolean.compare(p2Hitno, p1Hitno);
        }
      });
      this.paketiZaDostavu = new LinkedList<>(sortiranaLista);
    } else {
      this.paketiZaDostavu = new LinkedList<>();
    }

    this.vrijemeIsporuke = vrijemeIsporuke;
    this.cijeneDostave = (cijene != null) ? new HashMap<>(cijene) : new HashMap<>();
  }


  public void postaviTrenutnoVirtualnoVrijeme(LocalDateTime vrijeme) {
    this.trenutnoVirtualnoVrijeme = vrijeme;
  }

  public void ukrcavanjePaketa() {
    for (Vozilo vozilo : vozila) {
      double trenutnaTezina = 0;
      double trenutniVolumen = 0;

      while (!paketiZaDostavu.isEmpty()) {
        Paket paket = paketiZaDostavu.peek();

        if (isporuceniPaketi.contains(paket)) {
          paketiZaDostavu.poll();
          continue;
        }

        double novaTezina = trenutnaTezina + paket.getTezina();
        double noviVolumen =
            trenutniVolumen + paket.getVisina() * paket.getSirina() * paket.getDuzina();

        if (novaTezina <= vozilo.getKapacitetTezine()
            && noviVolumen <= vozilo.getKapacitetProstora()) {
          trenutnaTezina = novaTezina;
          trenutniVolumen = noviVolumen;
          ukrcaniPaketi.computeIfAbsent(vozilo, k -> new ArrayList<>()).add(paket);
          vrijemeUkrcavanja.put(paket,
              trenutnoVirtualnoVrijeme.toEpochSecond(java.time.ZoneOffset.UTC));
          System.out.println("U " + trenutnoVirtualnoVrijeme + " paket " + paket.getOznaka()
              + " je ukrcan u vozilo " + vozilo.getRegistracija());
          paketiZaDostavu.poll();
        } else {
          break;
        }
      }
    }
  }

  public void isporukaPaketa() {
    for (Map.Entry<Vozilo, List<Paket>> entry : ukrcaniPaketi.entrySet()) {
      Vozilo vozilo = entry.getKey();
      List<Paket> paketi = entry.getValue();

      if (paketi.isEmpty()) {
        continue;
      }

      System.out.println("U " + trenutnoVirtualnoVrijeme + " dostava je pokrenuta za vozilo "
          + vozilo.getRegistracija());

      boolean hitnaDostava = false;
      double ukupnaTezina = 0;
      double ukupniVolumen = 0;
      for (Paket paket : paketi) {
        if (paket.getUslugaDostave() == UslugaDostave.H) {
          hitnaDostava = true;
        }
        ukupnaTezina += paket.getTezina();
        ukupniVolumen += paket.getVisina() * paket.getSirina() * paket.getDuzina();
      }
      double popunjenostTezine = ukupnaTezina / vozilo.getKapacitetTezine();
      double popunjenostProstora = ukupniVolumen / vozilo.getKapacitetProstora();

      for (Paket paket : new ArrayList<>(paketi)) {
        long vrijemeKadaJeUkrcan = vrijemeUkrcavanja.getOrDefault(paket, 0L);
        long razlika =
            trenutnoVirtualnoVrijeme.toEpochSecond(java.time.ZoneOffset.UTC) - vrijemeKadaJeUkrcan;

        if (hitnaDostava || razlika >= vrijemeIsporuke * 60 || popunjenostTezine >= 0.5
            || popunjenostProstora >= 0.5) {
          isporuci(paket, vozilo);
          paketi.remove(paket);
        }
      }
      entry.getValue().clear();
    }
  }

  private void isporuci(Paket paket, Vozilo vozilo) {
    System.out.println(
        "U " + trenutnoVirtualnoVrijeme + " paket " + paket.getOznaka() + " isporučen primatelju "
            + paket.getPrimatelj() + " pomoću vozila: " + vozilo.getRegistracija());

    if (paket.getUslugaDostave() == UslugaDostave.P) {
      double trenutniNovac = prikupljeniNovacPoVozilu.getOrDefault(vozilo, 0.0);
      double noviIznos = trenutniNovac + cijeneDostave.getOrDefault(paket, 0.0);
      prikupljeniNovacPoVozilu.put(vozilo, noviIznos);
    }

    isporuceniPaketi.add(paket);
    double trenutnaTezina = kapacitetTezineVozila.getOrDefault(vozilo, 0.0);
    double trenutniVolumen = kapacitetVolumenaVozila.getOrDefault(vozilo, 0.0);
    kapacitetTezineVozila.put(vozilo, trenutnaTezina + paket.getTezina());
    kapacitetVolumenaVozila.put(vozilo,
        trenutniVolumen + paket.getVisina() * paket.getSirina() * paket.getDuzina());
    ukrcaniPaketi.get(vozilo).remove(paket);
  }

  public List<Paket> dohvatiIsporucenePaketeZaVozilo(Vozilo vozilo) {
    List<Paket> isporuceniPaketiZaVozilo = new ArrayList<>();
    List<Paket> ukrcaniPaketiVozila = ukrcaniPaketi.getOrDefault(vozilo, new ArrayList<>());

    for (Paket paket : isporuceniPaketi) {
      if (ukrcaniPaketiVozila.contains(paket)) {
        isporuceniPaketiZaVozilo.add(paket);
      }
    }

    return isporuceniPaketiZaVozilo;
  }

  public double dohvatiTrenutnuTezinuVozila(Vozilo vozilo) {
    return kapacitetTezineVozila.getOrDefault(vozilo, 0.0);
  }

  public double dohvatiTrenutniVolumenVozila(Vozilo vozilo) {
    return kapacitetVolumenaVozila.getOrDefault(vozilo, 0.0);
  }

  public boolean jeIsporucen(Paket paket) {
    for (List<Paket> paketi : ukrcaniPaketi.values()) {
      if (paketi.contains(paket)) {
        return true;
      }
    }
    return false;
  }

  public double dohvatiPrikupljeniNovacZaVozilo(Vozilo vozilo) {
    return prikupljeniNovacPoVozilu.getOrDefault(vozilo, 0.0);
  }

}
