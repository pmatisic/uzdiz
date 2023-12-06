package org.foi.uzdiz.pmatisic.zadaca_2.pomagala;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.foi.uzdiz.pmatisic.zadaca_2.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_2.model.UslugaDostave;
import org.foi.uzdiz.pmatisic.zadaca_2.model.Vozilo;

public class UredZaDostavu {

  public static Map<String, String> statusPaketa = new HashMap<>();
  private Queue<Paket> paketiZaDostavu = new LinkedList<>();
  private Map<Vozilo, List<Paket>> ukrcaniPaketi = new HashMap<>();
  private Map<String, Boolean> isporuceniPaketi = new HashMap<>();
  private List<Vozilo> vozila = new LinkedList<>();;
  private Map<Vozilo, Double> tezinaVozila = new HashMap<>();
  private Map<Vozilo, Double> volumenVozila = new HashMap<>();
  private Map<Paket, Double> cijeneDostave = new HashMap<>();
  private Map<Vozilo, Double> prikupljeniNovac = new HashMap<>();
  private int vrijemeIsporuke;
  private LocalDateTime trenutnoVirtualnoVrijeme;
  private Map<Paket, LocalDateTime> vrijemeUkrcavanja = new HashMap<>();
  public static Map<String, LocalDateTime> vrijemePreuzimanjaPaketa = new HashMap<>();
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

  public double dohvatiPrikupljeniNovacZaVozilo(Vozilo vozilo) {
    return prikupljeniNovac.getOrDefault(vozilo, 0.0);
  }

  public void postaviTrenutnoVirtualnoVrijeme(LocalDateTime vrijeme) {
    this.trenutnoVirtualnoVrijeme = vrijeme;
  }

  public void azurirajPaketeIZaDostavu(List<Paket> paketi, Map<Paket, Double> cijene) {
    if (paketi != null) {
      paketi.sort((p1, p2) -> {
        boolean p1Hitno = p1.getUslugaDostave() == UslugaDostave.H;
        boolean p2Hitno = p2.getUslugaDostave() == UslugaDostave.H;
        return Boolean.compare(p2Hitno, p1Hitno);
      });
      this.paketiZaDostavu.addAll(paketi);
    }
    this.cijeneDostave.putAll(cijene);
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
          vrijemeUkrcavanja.put(paket, trenutnoVirtualnoVrijeme);
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

    if (paketiZaIsporuku.isEmpty())
      return;

    System.out.println("U " + trenutnoVirtualnoVrijeme.format(dateTimeFormatter)
        + " dostava je pokrenuta za vozilo " + vozilo.getRegistracija());

    Iterator<Paket> iterator = paketiZaIsporuku.iterator();
    while (iterator.hasNext()) {
      Paket paket = iterator.next();
      LocalDateTime vrijemeUkrcavanjaPaketa = vrijemeUkrcavanja.get(paket);
      LocalDateTime vrijemeIsporukePaketa = vrijemeUkrcavanjaPaketa.plusMinutes(vrijemeIsporuke);

      if (Boolean.TRUE.equals(isporuceniPaketi.get(paket.getOznaka())))
        continue;

      if (trenutnoVirtualnoVrijeme.isBefore(vrijemeIsporukePaketa))
        continue;

      System.out.println("U " + vrijemeIsporukePaketa.format(dateTimeFormatter) + " paket "
          + paket.getOznaka() + " isporučen primatelju " + paket.getPrimatelj() + " pomoću vozila: "
          + vozilo.getRegistracija());

      vrijemePreuzimanjaPaketa.put(paket.getOznaka(), trenutnoVirtualnoVrijeme);

      if (paket.getUslugaDostave() == UslugaDostave.P) {
        double trenutniNovac = prikupljeniNovac.getOrDefault(vozilo, 0.0);
        trenutniNovac += paket.getIznosPouzeca();
        prikupljeniNovac.put(vozilo, trenutniNovac);
      }

      statusPaketa.put(paket.getOznaka(), "Dostavljeno");
      isporuceniPaketi.put(paket.getOznaka(), true);

      double trenutnaTezina =
          tezinaVozila.getOrDefault(vozilo, vozilo.getKapacitetTezine()) - paket.getTezina();
      double trenutniVolumen = volumenVozila.getOrDefault(vozilo, vozilo.getKapacitetProstora())
          - (paket.getVisina() * paket.getSirina() * paket.getDuzina());

      tezinaVozila.put(vozilo, trenutnaTezina);
      volumenVozila.put(vozilo, trenutniVolumen);

      ukrcaniPaketi.get(vozilo).remove(paket);
    }

    if (ukrcaniPaketi.get(vozilo).isEmpty()) {
      ukrcaniPaketi.remove(vozilo);
    }
  }

}
