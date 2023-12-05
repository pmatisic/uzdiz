package org.foi.uzdiz.pmatisic.zadaca_2.pomagala;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

  public static Map<String, String> statusPaketa = new HashMap<>();
  private int vrijemeIsporuke;
  private LocalDateTime trenutnoVirtualnoVrijeme;
  private Queue<Paket> paketiZaDostavu;
  private List<Vozilo> vozila;
  private Map<Paket, Long> vrijemeUkrcavanja = new HashMap<>();
  private Map<Paket, Double> cijeneDostave = new HashMap<>();
  private Map<Vozilo, Double> kapacitetTezineVozila = new HashMap<>();
  private Map<Vozilo, Double> kapacitetVolumenaVozila = new HashMap<>();
  private Map<Vozilo, Double> prikupljeniNovacPoVozilu = new HashMap<>();
  private Map<Vozilo, List<Paket>> ukrcaniPaketi = new HashMap<>();
  private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");

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

  public double dohvatiTrenutnuTezinuVozila(Vozilo vozilo) {
    return kapacitetTezineVozila.getOrDefault(vozilo, 0.0);
  }

  public double dohvatiTrenutniVolumenVozila(Vozilo vozilo) {
    return kapacitetVolumenaVozila.getOrDefault(vozilo, 0.0);
  }

  public double dohvatiPrikupljeniNovacZaVozilo(Vozilo vozilo) {
    return prikupljeniNovacPoVozilu.getOrDefault(vozilo, 0.0);
  }

  public void postaviTrenutnoVirtualnoVrijeme(LocalDateTime vrijeme) {
    this.trenutnoVirtualnoVrijeme = vrijeme;
  }

  public void ukrcavanjePaketa() {
    Queue<Paket> preostaliPaketi = new LinkedList<>(paketiZaDostavu);

    for (Vozilo vozilo : vozila) {
      double trenutnaTezina = 0;
      double trenutniVolumen = 0;

      while (!preostaliPaketi.isEmpty()) {
        Paket paket = preostaliPaketi.peek();

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
          System.out.println("U " + trenutnoVirtualnoVrijeme.format(dateTimeFormatter) + " paket "
              + paket.getOznaka() + " je ukrcan u vozilo " + vozilo.getRegistracija());
          preostaliPaketi.poll();
        } else {
          break;
        }
      }
    }
    paketiZaDostavu = preostaliPaketi;
  }

  public void isporukaPaketa() {
    for (Map.Entry<Vozilo, List<Paket>> entry : ukrcaniPaketi.entrySet()) {
      Vozilo vozilo = entry.getKey();
      List<Paket> paketi = new ArrayList<>(entry.getValue());

      if (paketi.isEmpty()) {
        continue;
      }

      System.out.println("U " + trenutnoVirtualnoVrijeme.format(dateTimeFormatter)
          + " dostava je pokrenuta za vozilo " + vozilo.getRegistracija());

      List<Paket> paketiZaUklanjanje = new ArrayList<>();

      double ukupnaTezina = 0;
      double ukupniVolumen = 0;
      for (Paket paket : paketi) {
        ukupnaTezina += paket.getTezina();
        ukupniVolumen += paket.getVisina() * paket.getSirina() * paket.getDuzina();
      }
      double popunjenostTezine = ukupnaTezina / vozilo.getKapacitetTezine();
      double popunjenostProstora = ukupniVolumen / vozilo.getKapacitetProstora();

      for (Paket paket : paketi) {
        long vrijemeKadaJeUkrcan = vrijemeUkrcavanja.getOrDefault(paket, 0L);
        long razlika =
            trenutnoVirtualnoVrijeme.toEpochSecond(java.time.ZoneOffset.UTC) - vrijemeKadaJeUkrcan;

        if (paket.getUslugaDostave() == UslugaDostave.H || razlika >= vrijemeIsporuke * 60
            || popunjenostTezine >= 0.5 || popunjenostProstora >= 0.5) {
          isporuci(paket, vozilo);
          paketiZaUklanjanje.add(paket);
        }
      }

      paketi.removeAll(paketiZaUklanjanje);
      entry.setValue(paketi);
    }
  }

  private void isporuci(Paket paket, Vozilo vozilo) {
    System.out.println("U " + trenutnoVirtualnoVrijeme.format(dateTimeFormatter) + " paket "
        + paket.getOznaka() + " isporučen primatelju " + paket.getPrimatelj() + " pomoću vozila: "
        + vozilo.getRegistracija());

    if (paket.getUslugaDostave() == UslugaDostave.P) {
      double trenutniNovac = prikupljeniNovacPoVozilu.getOrDefault(vozilo, 0.0);
      double noviIznos = trenutniNovac + cijeneDostave.getOrDefault(paket, 0.0);
      prikupljeniNovacPoVozilu.put(vozilo, noviIznos);
    }

    statusPaketa.put(paket.getOznaka(), "Dostavljeno");

    double trenutnaTezina =
        kapacitetTezineVozila.getOrDefault(vozilo, vozilo.getKapacitetTezine()) - paket.getTezina();
    double trenutniVolumen =
        kapacitetVolumenaVozila.getOrDefault(vozilo, vozilo.getKapacitetProstora())
            - paket.getVisina() * paket.getSirina() * paket.getDuzina();
    kapacitetTezineVozila.put(vozilo, trenutnaTezina);
    kapacitetVolumenaVozila.put(vozilo, trenutniVolumen);
  }

}
