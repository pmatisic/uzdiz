package org.foi.uzdiz.pmatisic.zadaca_3.pomagala;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.foi.uzdiz.pmatisic.zadaca_3.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_3.composite.CompositeProstor;
import org.foi.uzdiz.pmatisic.zadaca_3.composite.Prostor;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Mjesto;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Osoba;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Podrucje;
import org.foi.uzdiz.pmatisic.zadaca_3.model.StatusVozila;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Ulica;
import org.foi.uzdiz.pmatisic.zadaca_3.model.UslugaDostave;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Vozilo;
import org.foi.uzdiz.pmatisic.zadaca_3.state.AktivnoStanjeVozila;
import org.foi.uzdiz.pmatisic.zadaca_3.state.NeaktivnoStanjeVozila;
import org.foi.uzdiz.pmatisic.zadaca_3.state.NeispravnoStanjeVozila;
import org.foi.uzdiz.pmatisic.zadaca_3.state.StanjeVozila;
import org.foi.uzdiz.pmatisic.zadaca_3.strategy.Redoslijed;
import org.foi.uzdiz.pmatisic.zadaca_3.strategy.StrategijaIsporuke;
import org.foi.uzdiz.pmatisic.zadaca_3.strategy.Udaljenost;

public class UredZaDostavu {

  public static Map<String, LocalDateTime> vrijemePreuzimanjaPaketa = new HashMap<>();
  public static Map<String, String> statusPaketa = new HashMap<>();
  private Queue<Paket> paketiZaDostavu = new LinkedList<>();
  public Map<Vozilo, List<Paket>> ukrcaniPaketi = new HashMap<>();
  public Map<String, Boolean> isporuceniPaketi = new HashMap<>();
  public List<Vozilo> vozila = new LinkedList<>();
  private Map<Vozilo, Double> tezinaVozila = new HashMap<>();
  private Map<Vozilo, Double> volumenVozila = new HashMap<>();
  private Map<Integer, Mjesto> mjestoMap = new HashMap<>();
  public Map<Integer, Ulica> ulicaMap = new HashMap<>();
  private Map<Integer, List<Integer>> podrucjeMjestaMap = new HashMap<>();
  private Map<Integer, List<Integer>> mjestoUliceMap = new HashMap<>();
  private List<CompositeProstor> prostori = new ArrayList<>();
  private List<Podrucje> podrucjaList = new ArrayList<>();
  private List<Osoba> osobe = new ArrayList<>();
  private Set<String> logiraneGreske = new HashSet<>();
  private StrategijaIsporuke strategijaIsporuke;
  private String gps;
  private int isporuka;
  public int vrijemeIsporuke;
  public LocalDateTime trenutnoVirtualnoVrijeme;
  private LocalDateTime sljedeciPuniSat;
  public DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");

  public UredZaDostavu(List<Vozilo> vozila, int vrijemeIsporuke, int isporuka, String gps) {
    this.vozila = vozila;
    this.vrijemeIsporuke = vrijemeIsporuke;
    this.isporuka = isporuka;
    this.gps = gps;
    if (this.isporuka == 1) {
      postaviStrategijuIsporuke(new Redoslijed(this));
    } else {
      postaviStrategijuIsporuke(new Udaljenost(this));
    }
  }

  public void postaviTrenutnoVirtualnoVrijeme(LocalDateTime vrijeme) {
    this.trenutnoVirtualnoVrijeme = vrijeme;
    izracunajVrijemeDoPunogSata();
  }

  public void izracunajVrijemeDoPunogSata() {
    int minuteDoPunogSata = 60 - trenutnoVirtualnoVrijeme.getMinute();
    if (minuteDoPunogSata == 60)
      minuteDoPunogSata = 0;

    sljedeciPuniSat =
        trenutnoVirtualnoVrijeme.plusMinutes(minuteDoPunogSata).withSecond(0).withNano(0);
  }

  public boolean jeNaPunomSatu() {
    return trenutnoVirtualnoVrijeme.isEqual(sljedeciPuniSat);
  }

  public double dohvatiTrenutnuTezinuVozila(Vozilo vozilo) {
    return tezinaVozila.getOrDefault(vozilo, 0.0);
  }

  public double dohvatiTrenutniVolumenVozila(Vozilo vozilo) {
    return volumenVozila.getOrDefault(vozilo, 0.0);
  }

  public void dohvatiOsobe(List<Osoba> korisnici) {
    for (Osoba osoba : korisnici) {
      osobe.add(osoba);
    }
  }

  public void azurirajPaketeZaDostavu(List<Paket> paketi) {
    if (paketi != null) {
      paketi.sort((p1, p2) -> {
        boolean p1Hitno = p1.getUslugaDostave() == UslugaDostave.H;
        boolean p2Hitno = p2.getUslugaDostave() == UslugaDostave.H;
        return Boolean.compare(p2Hitno, p1Hitno);
      });

      for (Paket paket : paketi) {
        if (!paket.jePoslanZaDostavu()) {
          this.paketiZaDostavu.add(paket);
          paket.setPoslanZaDostavu(true);
        }
      }
    }
  }

  public void ukrcajPaket() {
    if (!jeNaPunomSatu())
      return;

    for (Vozilo vozilo : vozila) {
      if (!vozilo.jeSlobodno() || !vozilo.getStatus().equals(StatusVozila.A)) {
        continue;
      }

      String podrucjeVozila = null;

      List<Integer> podrucjaVozila = dohvatiPodrucjaVozila(vozilo);
      double trenutnaTezina = dohvatiTrenutnuTezinuVozila(vozilo);
      double trenutniVolumen = dohvatiTrenutniVolumenVozila(vozilo);

      Iterator<Paket> iterator = paketiZaDostavu.iterator();
      while (iterator.hasNext()) {
        Paket paket = iterator.next();
        Osoba primatelj = dohvatiOsobuPoImenu(paket.getPrimatelj());
        if (primatelj == null) {
          statusPaketa.put(paket.getOznaka(), "Neispravna pošiljka");
          String kljucGreske =
              "Paket " + paket.getOznaka() + ", Primatelj: " + paket.getPrimatelj();
          if (!logiraneGreske.contains(kljucGreske)) {
            Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, kljucGreske,
                "Primatelj ne postoji ili nije pronađen.");
            logiraneGreske.add(kljucGreske);
          }
          continue;
        }

        String paketPodrucje = dohvatiPodrucjePaketa(primatelj);
        if (paketPodrucje == null) {
          statusPaketa.put(paket.getOznaka(), "Neispravna pošiljka");
          String kljucGreske = "Paket " + paket.getOznaka() + ", Primatelj: " + paket.getPrimatelj()
              + ", Područje paketa: " + paketPodrucje;
          if (!logiraneGreske.contains(kljucGreske)) {
            Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, kljucGreske,
                "Neispravna adresa dostave, područje ne postoji.");
            logiraneGreske.add(kljucGreske);
          }
          continue;
        }

        Ulica ulicaPrimatelja = ulicaMap.get(primatelj.getUlicaId());
        if (ulicaPrimatelja == null) {
          statusPaketa.put(paket.getOznaka(), "Neispravna pošiljka");
          String kljucGreske = "Paket " + paket.getOznaka() + ", Primatelj: " + paket.getPrimatelj()
              + ", Ulica ID: " + primatelj.getUlicaId();
          if (!logiraneGreske.contains(kljucGreske)) {
            Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, kljucGreske,
                "Neispravna adresa dostave, ulica ne postoji.");
            logiraneGreske.add(kljucGreske);
          }
          continue;
        }

        if (podrucjeVozila == null) {
          podrucjeVozila = paketPodrucje;
        } else if (!podrucjeVozila.equals(paketPodrucje)) {
          continue;
        }

        if (!prostorZaPaket(vozilo, paket, trenutnaTezina, trenutniVolumen)) {
          continue;
        }

        if (!podrucjaVozila.contains(Integer.parseInt(paketPodrucje))) {
          continue;
        }

        if (!vozilo.ukrcajPaket(paket)) {
          continue;
        }

        ukrcaniPaketi.computeIfAbsent(vozilo, k -> new ArrayList<>()).add(paket);
        vozilo.povecajBrojPaketa(paket);
        statusPaketa.put(paket.getOznaka(), "Ukrcano");

        System.out.printf("Paket %s je ukrcan na %s.%n", paket.getOznaka(),
            vozilo.getRegistracija());

        isporuceniPaketi.put(paket.getOznaka(), false);
        iterator.remove();
      }

      if (!ukrcaniPaketi.getOrDefault(vozilo, new ArrayList<>()).isEmpty()) {
        vozilo.setSlobodno(false);
        vozilo.povecajBrojVoznji();
        isporuciPaket(vozilo);
        podrucjeVozila = null;
      }
    }
  }

  private Osoba dohvatiOsobuPoImenu(String ime) {
    for (Osoba osoba : osobe) {
      if (osoba.getIme().equals(ime)) {
        return osoba;
      }
    }
    return null;
  }

  private boolean prostorZaPaket(Vozilo vozilo, Paket paket, double trenutnaTezina,
      double trenutniVolumen) {
    double novaTezina = trenutnaTezina + paket.getTezina();
    double noviVolumen =
        trenutniVolumen + paket.getVisina() * paket.getSirina() * paket.getDuzina();
    return novaTezina <= vozilo.getKapacitetTezine()
        && noviVolumen <= vozilo.getKapacitetProstora();
  }

  private List<Integer> dohvatiPodrucjaVozila(Vozilo vozilo) {
    String podrucja = vozilo.getPodrucjaPoRangu();
    String[] podrucjaSplit = podrucja.split(",");
    List<Integer> podrucjaID = new ArrayList<>();
    for (String id : podrucjaSplit) {
      try {
        podrucjaID.add(Integer.parseInt(id.trim()));
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
    }
    return podrucjaID;
  }

  private String dohvatiPodrucjePaketa(Osoba osoba) {
    int idGrada = osoba.getGradId();
    int idUlice = osoba.getUlicaId();
    List<Integer> uliceMjesta = mjestoUliceMap.get(idGrada);
    if (uliceMjesta != null && uliceMjesta.contains(idUlice)) {
      for (Map.Entry<Integer, List<Integer>> entry : podrucjeMjestaMap.entrySet()) {
        if (entry.getValue().contains(idGrada)) {
          return entry.getKey().toString();
        }
      }
    }
    return null;
  }

  public void isporuciPaket(Vozilo vozilo) {
    if (strategijaIsporuke != null) {
      strategijaIsporuke.isporuciPakete(vozilo, gps);
    }
  }

  public void postaviStrategijuIsporuke(StrategijaIsporuke strategija) {
    this.strategijaIsporuke = strategija;
  }

  public void preuzmiPodatkeOProstoru(List<Podrucje> podrucja, List<Mjesto> mjesta,
      List<Ulica> ulice) {
    for (Ulica ulica : ulice) {
      ulicaMap.put(ulica.getId(), ulica);
    }

    for (Mjesto mjesto : mjesta) {
      List<Integer> uliceMjesta = new ArrayList<>();
      for (Integer idUlice : mjesto.getUlice()) {
        uliceMjesta.add(idUlice);
      }
      mjestoMap.put(mjesto.getId(), mjesto);
      mjestoUliceMap.put(mjesto.getId(), uliceMjesta);
    }

    for (Podrucje podrucje : podrucja) {
      List<Integer> uliceUPodrucju = new ArrayList<>();
      for (Podrucje.Par<Integer, String> par : podrucje.getGradUlicaParovi()) {
        int idMjesta = par.getPrvi();
        String uliceGradova = par.getDrugi();
        if (uliceGradova.equals("*")) {
          List<Integer> uliceMjesta = mjestoUliceMap.getOrDefault(idMjesta, new ArrayList<>());
          uliceUPodrucju.addAll(uliceMjesta);
        } else {
          String[] uliceIds = uliceGradova.split(",");
          for (String ulicaIdStr : uliceIds) {
            try {
              int ulicaId = Integer.parseInt(ulicaIdStr);
              uliceUPodrucju.add(ulicaId);
            } catch (NumberFormatException e) {
              Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, "Ulica s ID-om: " + ulicaIdStr,
                  "Nevažeći ID ulice!");
            }
          }
        }
      }
      podrucjaList.add(podrucje);
      podrucjeMjestaMap.put(podrucje.getId(), uliceUPodrucju);
    }
  }

  public void ispisiTablicu() {
    for (CompositeProstor podrucje : prostori) {
      ispisiPodrucje(podrucje, 0);
    }
  }

  private void ispisiPodrucje(Prostor prostor, int razina) {
    if (prostor instanceof CompositeProstor) {
      System.out.printf("%sPodrucje: %d%n", "    ".repeat(razina), prostor.dohvatiId());
      for (Prostor dijete : prostor.dohvatiDjecu()) {
        ispisiPodrucje(dijete, razina + 1);
      }
    } else if (prostor instanceof Mjesto) {
      Mjesto mjesto = (Mjesto) prostor;
      System.out.printf("%sGrad: %d %s%n", "    ".repeat(razina), mjesto.getId(),
          mjesto.getNaziv());
      for (Integer ulicaId : mjesto.getUlice()) {
        Ulica ulica = ulicaMap.get(ulicaId);
        if (ulica != null) {
          System.out.printf("%sUlica: %d %s%n", "    ".repeat(razina + 1), ulica.getId(),
              ulica.getNaziv());
        }
      }
    }
  }

  public void izgradiCompositeStrukturu() {
    CompositeProstor korijen = new CompositeProstor(0);

    for (Podrucje podrucjeData : podrucjaList) {
      CompositeProstor podrucje = new CompositeProstor(podrucjeData.getId());
      Map<Integer, List<Integer>> mjestaUliceMap = new HashMap<>();

      for (Podrucje.Par<Integer, String> gradUlica : podrucjeData.getGradUlicaParovi()) {
        int idMjesta = gradUlica.getPrvi();
        Mjesto originalnoMjesto = mjestoMap.get(idMjesta);

        if (originalnoMjesto != null) {
          List<Integer> ulice = mjestaUliceMap.computeIfAbsent(idMjesta, k -> new ArrayList<>());

          if (gradUlica.getDrugi().equals("*")) {
            ulice.addAll(originalnoMjesto.getUlice());
          } else {
            for (String ulicaIdStr : gradUlica.getDrugi().split(",")) {
              int ulicaId = Integer.parseInt(ulicaIdStr);
              ulice.add(ulicaId);
            }
          }
        }
      }

      for (Map.Entry<Integer, List<Integer>> entry : mjestaUliceMap.entrySet()) {
        Mjesto originalnoMjesto = mjestoMap.get(entry.getKey());
        if (originalnoMjesto != null) {
          Mjesto novoMjesto =
              new Mjesto(entry.getKey(), originalnoMjesto.getNaziv(), entry.getValue());
          podrucje.dodajPodrucje(novoMjesto);
        }
      }

      korijen.dodajPodrucje(podrucje);
    }

    prostori.add(korijen);
  }

  public void promijeniStanjeVozila(String registracija, String novoStanje) {
    for (Vozilo vozilo : vozila) {
      if (vozilo.getRegistracija().equals(registracija)) {
        StanjeVozila stanje;
        switch (novoStanje) {
          case "A":
            stanje = new AktivnoStanjeVozila();
            System.out.println("Vozilu " + registracija + " se postavlja status da je aktivno.");
            break;
          case "NI":
            stanje = new NeispravnoStanjeVozila();
            System.out.println("Vozilu " + registracija + " se postavlja status da je neispravno.");
            break;
          case "NA":
            stanje = new NeaktivnoStanjeVozila();
            System.out.println("Vozilu " + registracija + " se postavlja status da je neaktivno.");
            break;
          default:
            System.out.println("Nepoznato stanje vozila.");
            return;
        }
        vozilo.promijeniStanje(stanje);
        return;
      }
    }
  }

}
