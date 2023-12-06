package org.foi.uzdiz.pmatisic.zadaca_2.pomagala;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.foi.uzdiz.pmatisic.zadaca_2.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_2.model.UslugaDostave;
import org.foi.uzdiz.pmatisic.zadaca_2.model.Vozilo;

public class UredZaDostavu {

  public static Map<String, LocalDateTime> vrijemePreuzimanjaPaketa = new HashMap<>();
  public static Map<String, String> statusPaketa = new HashMap<>();
  private Queue<Paket> paketiZaDostavu = new LinkedList<>();
  private Map<Vozilo, List<Paket>> ukrcaniPaketi = new HashMap<>();
  private Map<String, Boolean> isporuceniPaketi = new HashMap<>();
  private List<Vozilo> vozila = new LinkedList<>();
  private Map<Vozilo, Double> tezinaVozila = new HashMap<>();
  private Map<Vozilo, Double> volumenVozila = new HashMap<>();
  private int vrijemeIsporuke;
  private LocalDateTime trenutnoVirtualnoVrijeme;
  private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");

  public UredZaDostavu(List<Vozilo> vozila, int vrijemeIsporuke) {
    this.vozila = vozila;
    Collections.sort(this.vozila, Comparator.comparing(Vozilo::getRedoslijed));
    this.vrijemeIsporuke = vrijemeIsporuke;
  }

  public double dohvatiTrenutnuTezinuVozila(Vozilo vozilo) {
    return tezinaVozila.getOrDefault(vozilo, 0.0);
  }

  public double dohvatiTrenutniVolumenVozila(Vozilo vozilo) {
    return volumenVozila.getOrDefault(vozilo, 0.0);
  }

  public void postaviTrenutnoVirtualnoVrijeme(LocalDateTime vrijeme) {
    this.trenutnoVirtualnoVrijeme = vrijeme;
  }

  public void azurirajPaketeZaDostavu(List<Paket> paketi) {
    if (paketi != null) {
      paketi.sort((p1, p2) -> {
        boolean p1Hitno = p1.getUslugaDostave() == UslugaDostave.H;
        boolean p2Hitno = p2.getUslugaDostave() == UslugaDostave.H;
        return Boolean.compare(p2Hitno, p1Hitno);
      });
      this.paketiZaDostavu.addAll(paketi);
    }
  }

  public void ukrcajPaket() {
    Queue<Paket> preostaliPaketi = new LinkedList<>(paketiZaDostavu);
    LocalDateTime vrijemePrvogUkrcaja = null;

    for (Vozilo vozilo : vozila) {
      double trenutnaTezina = dohvatiTrenutnuTezinuVozila(vozilo);
      double trenutniVolumen = dohvatiTrenutniVolumenVozila(vozilo);
      boolean voziloAktivirano = false;

      Iterator<Paket> iterator = preostaliPaketi.iterator();
      while (iterator.hasNext()) {
        Paket paket = iterator.next();
        double novaTezina = trenutnaTezina + paket.getTezina();
        double noviVolumen =
            trenutniVolumen + paket.getVisina() * paket.getSirina() * paket.getDuzina();

        if (novaTezina <= vozilo.getKapacitetTezine()
            && noviVolumen <= vozilo.getKapacitetProstora()) {
          if (vrijemePrvogUkrcaja == null) {
            vrijemePrvogUkrcaja = trenutnoVirtualnoVrijeme;
          }

          trenutnaTezina = novaTezina;
          trenutniVolumen = noviVolumen;
          ukrcaniPaketi.computeIfAbsent(vozilo, k -> new ArrayList<>()).add(paket);
          tezinaVozila.put(vozilo, trenutnaTezina);
          volumenVozila.put(vozilo, trenutniVolumen);
          statusPaketa.put(paket.getOznaka(), "Ukrcano");
          isporuceniPaketi.put(paket.getOznaka(), false);
          iterator.remove();
          voziloAktivirano = true;
        }
      }

      boolean ispunjenKapacitet = (trenutnaTezina >= vozilo.getKapacitetTezine() * 0.5)
          || (trenutniVolumen >= vozilo.getKapacitetProstora() * 0.5);
      boolean protekaoSat = vrijemePrvogUkrcaja != null
          && trenutnoVirtualnoVrijeme.isAfter(vrijemePrvogUkrcaja.plusHours(1));

      if (voziloAktivirano && (ispunjenKapacitet || protekaoSat)) {
        isporuciPakete(vozilo);
      }
    }

    paketiZaDostavu = preostaliPaketi;
  }

  public void isporuciPakete(Vozilo vozilo) {
    List<Paket> paketiZaIsporuku =
        new ArrayList<>(ukrcaniPaketi.getOrDefault(vozilo, new ArrayList<>()));
    Set<Paket> zaUklanjanje = new HashSet<>();
    LocalDateTime vrijemeSljedeceDostave = vozilo.getVrijemeSljedeceDostave();

    if (paketiZaIsporuku.isEmpty())
      return;

    if (vrijemeSljedeceDostave == null
        || trenutnoVirtualnoVrijeme.isAfter(vrijemeSljedeceDostave)) {
      vrijemeSljedeceDostave = trenutnoVirtualnoVrijeme;
    }

    System.out.println("U " + trenutnoVirtualnoVrijeme.format(dateTimeFormatter)
        + " dostava je pokrenuta za vozilo " + vozilo.getRegistracija());

    for (Paket paket : paketiZaIsporuku) {
      String status = statusPaketa.getOrDefault(paket.getOznaka(), "");

      if (Boolean.TRUE.equals(isporuceniPaketi.get(paket.getOznaka())))
        continue;

      if (!"Ukrcano".equals(status))
        continue;

      if (trenutnoVirtualnoVrijeme.isBefore(vrijemeSljedeceDostave))
        continue;

      System.out.println("U " + vrijemeSljedeceDostave.format(dateTimeFormatter) + " paket "
          + paket.getOznaka() + " isporučen primatelju " + paket.getPrimatelj() + " pomoću vozila: "
          + vozilo.getRegistracija());

      vrijemePreuzimanjaPaketa.put(paket.getOznaka(), trenutnoVirtualnoVrijeme);
      statusPaketa.put(paket.getOznaka(), "Dostavljeno");
      isporuceniPaketi.put(paket.getOznaka(), true);

      double trenutnaTezina =
          tezinaVozila.getOrDefault(vozilo, vozilo.getKapacitetTezine()) - paket.getTezina();
      double trenutniVolumen = volumenVozila.getOrDefault(vozilo, vozilo.getKapacitetProstora())
          - (paket.getVisina() * paket.getSirina() * paket.getDuzina());

      tezinaVozila.put(vozilo, trenutnaTezina);
      volumenVozila.put(vozilo, trenutniVolumen);

      vrijemeSljedeceDostave = vrijemeSljedeceDostave.plusMinutes(vrijemeIsporuke);
      vozilo.setVrijemeSljedeceDostave(vrijemeSljedeceDostave);
      zaUklanjanje.add(paket);
    }

    List<Paket> paketiUvozilu = ukrcaniPaketi.getOrDefault(vozilo, new ArrayList<>());
    if (paketiUvozilu.isEmpty()) {
      System.out.println("Vozilo je prazno.");
    } else {
      for (Paket paket : paketiUvozilu) {
        System.out.println(
            "Paket: " + paket.getOznaka() + " - Težina: " + paket.getTezina() + " - Dimenzije: "
                + paket.getVisina() + "x" + paket.getSirina() + "x" + paket.getDuzina());
      }
    }

    for (Paket paket : zaUklanjanje) {
      ukrcaniPaketi.get(vozilo).remove(paket);
    }

    if (ukrcaniPaketi.get(vozilo).isEmpty()) {
      ukrcaniPaketi.remove(vozilo);
    }
  }

}
