package org.foi.uzdiz.pmatisic.zadaca_2.pomagala;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    this.vrijemeIsporuke = vrijemeIsporuke;
  }

  public void postaviTrenutnoVirtualnoVrijeme(LocalDateTime vrijeme) {
    this.trenutnoVirtualnoVrijeme = vrijeme;
  }

  public double dohvatiTrenutnuTezinuVozila(Vozilo vozilo) {
    return tezinaVozila.getOrDefault(vozilo, 0.0);
  }

  public double dohvatiTrenutniVolumenVozila(Vozilo vozilo) {
    return volumenVozila.getOrDefault(vozilo, 0.0);
  }

  public void azurirajPaketeZaDostavu(List<Paket> paketi) {
    if (paketi != null) {
      paketi.sort((p1, p2) -> {
        boolean p1Hitno = p1.getUslugaDostave() == UslugaDostave.H;
        boolean p2Hitno = p2.getUslugaDostave() == UslugaDostave.H;
        return Boolean.compare(p2Hitno, p1Hitno);
      });

      for (Paket paket : paketi) {
        if (!paket.isPoslanZaDostavu()) {
          this.paketiZaDostavu.add(paket);
          paket.setPoslanZaDostavu(true);
        }
      }
    }
  }

  public void ukrcajPaket() {
    for (Vozilo vozilo : vozila) {
      if (!vozilo.jeSlobodno()) {
        continue;
      }
      double trenutnaTezina = dohvatiTrenutnuTezinuVozila(vozilo);
      double trenutniVolumen = dohvatiTrenutniVolumenVozila(vozilo);
      boolean imaHitnihPaketa = false;
      LocalDateTime vrijemePrvogUkrcaja = null;

      Iterator<Paket> iterator = paketiZaDostavu.iterator();
      while (iterator.hasNext()) {
        Paket paket = iterator.next();
        if (paket.getUslugaDostave() == UslugaDostave.H) {
          imaHitnihPaketa = true;
        }

        trenutnaTezina += paket.getTezina();
        trenutniVolumen += paket.getVisina() * paket.getSirina() * paket.getDuzina();
        if (trenutnaTezina <= vozilo.getKapacitetTezine()
            && trenutniVolumen <= vozilo.getKapacitetProstora()) {
          if (vrijemePrvogUkrcaja == null) {
            vrijemePrvogUkrcaja = trenutnoVirtualnoVrijeme;
          }

          tezinaVozila.put(vozilo, trenutnaTezina);
          volumenVozila.put(vozilo, trenutniVolumen);

          ukrcaniPaketi.computeIfAbsent(vozilo, k -> new ArrayList<>()).add(paket);
          statusPaketa.put(paket.getOznaka(), "Ukrcano");
          System.out
              .println("Paket " + paket.getOznaka() + " je ukrcan na " + vozilo.getRegistracija());
          isporuceniPaketi.put(paket.getOznaka(), false);
          iterator.remove();
        }
      }

      boolean ispunjenKapacitet = (trenutnaTezina >= vozilo.getKapacitetTezine() * 0.5)
          || (trenutniVolumen >= vozilo.getKapacitetProstora() * 0.5);
      boolean protekaoSat = vrijemePrvogUkrcaja != null
          && trenutnoVirtualnoVrijeme.isAfter(vrijemePrvogUkrcaja.plusHours(1));

      if ((imaHitnihPaketa || ispunjenKapacitet || protekaoSat)
          && !ukrcaniPaketi.getOrDefault(vozilo, new ArrayList<>()).isEmpty()) {
        vozilo.setSlobodno(false);
        isporuciPaket(vozilo);
        vrijemePrvogUkrcaja = null;
        tezinaVozila.put(vozilo, 0.0);
        volumenVozila.put(vozilo, 0.0);
      }
    }
  }

  public void isporuciPaket(Vozilo vozilo) {
    List<Paket> paketiZaIsporuku = ukrcaniPaketi.getOrDefault(vozilo, new ArrayList<>());
    if (paketiZaIsporuku.isEmpty())
      return;

    LocalDateTime vrijemeSljedeceDostave = vozilo.getVrijemeSljedeceDostave();
    vrijemeSljedeceDostave = trenutnoVirtualnoVrijeme.plusMinutes(vrijemeIsporuke);

    System.out.println("U " + trenutnoVirtualnoVrijeme.format(dateTimeFormatter)
        + " dostava je pokrenuta za vozilo " + vozilo.getRegistracija());

    Iterator<Paket> iterator = paketiZaIsporuku.iterator();
    while (iterator.hasNext()) {
      Paket paket = iterator.next();
      String status = statusPaketa.getOrDefault(paket.getOznaka(), "");

      if (Boolean.TRUE.equals(isporuceniPaketi.get(paket.getOznaka()))
          || !"Ukrcano".equals(status)) {
        continue;
      }

      System.out.println("U " + vrijemeSljedeceDostave.format(dateTimeFormatter) + " paket "
          + paket.getOznaka() + " isporučen primatelju " + paket.getPrimatelj() + " pomoću vozila: "
          + vozilo.getRegistracija());

      vrijemePreuzimanjaPaketa.put(paket.getOznaka(), vrijemeSljedeceDostave);
      statusPaketa.put(paket.getOznaka(), "Dostavljeno");
      isporuceniPaketi.put(paket.getOznaka(), true);
      vozilo.setSlobodno(true);

      vrijemeSljedeceDostave = vrijemeSljedeceDostave.plusMinutes(vrijemeIsporuke);
      vozilo.setVrijemeSljedeceDostave(vrijemeSljedeceDostave);

      iterator.remove();
    }

    if (paketiZaIsporuku.isEmpty()) {
      ukrcaniPaketi.remove(vozilo);
    }
  }

}
