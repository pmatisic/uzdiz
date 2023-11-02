package org.foi.uzdiz.pmatisic.zadaca_1.singleton;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.uzdiz.pmatisic.zadaca_1.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_1.factory.PrijemPaketaDatoteka;
import org.foi.uzdiz.pmatisic.zadaca_1.factory.VoziloDatoteka;
import org.foi.uzdiz.pmatisic.zadaca_1.factory.VrstePaketaDatoteka;
import org.foi.uzdiz.pmatisic.zadaca_1.model.PrijemPaketa;
import org.foi.uzdiz.pmatisic.zadaca_1.model.Vozilo;
import org.foi.uzdiz.pmatisic.zadaca_1.model.VrstaPaketa;
import org.foi.uzdiz.pmatisic.zadaca_1.pomagala.Greske;
import org.foi.uzdiz.pmatisic.zadaca_1.pomagala.UredZaDostavu;
import org.foi.uzdiz.pmatisic.zadaca_1.pomagala.UredZaPrijem;

public class Tvrtka {

  private static volatile Tvrtka instance;
  private List<PrijemPaketa> prijemi;
  private List<Vozilo> vozila;
  private List<VrstaPaketa> vrste;
  private UredZaPrijem uredZaPrijem;
  private UredZaDostavu uredZaDostavu;
  private PrijemPaketaDatoteka citacPrijemaPaketa = new PrijemPaketaDatoteka();
  private VoziloDatoteka citacVozila = new VoziloDatoteka();
  private VrstePaketaDatoteka citacVrstaPaketa = new VrstePaketaDatoteka();
  private String putanjaDoPP;
  private String putanjaDoPV;
  private String putanjaDoVP;
  private int maxTezina;
  private int vrijemeIsporuke;
  private int mnoziteljSekunde;
  private LocalDateTime virtualnoVrijeme;
  private LocalTime pocetakRada;
  private LocalTime krajRada;

  private Tvrtka() {}

  private Tvrtka(Map<String, String> podatci) {
    this.putanjaDoPP = podatci.get("pp");
    this.putanjaDoPV = podatci.get("pv");
    this.putanjaDoVP = podatci.get("vp");
    this.maxTezina = Integer.parseInt(podatci.get("mt"));
    this.vrijemeIsporuke = Integer.parseInt(podatci.get("vi"));
    this.mnoziteljSekunde = Integer.parseInt(podatci.get("ms"));
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
    this.virtualnoVrijeme = LocalDateTime.parse(podatci.get("vs"), dateTimeFormatter);
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    this.pocetakRada = LocalTime.parse(podatci.get("pr"), timeFormatter);
    this.krajRada = LocalTime.parse(podatci.get("kr"), timeFormatter);
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
    }
    return result;
  }

  public static void main(String[] args) {
    Tvrtka t = new Tvrtka();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < args.length; i++) {
      sb.append(args[i]).append(" ");
    }

    String s = sb.toString().trim();
    Matcher m = t.provjeriArgumente(s);
    if (m == null) {
      System.out.println("Greška u argumentima ili fali argument, provjerite unos!");
      return;
    }

    Map<String, String> podatci = obradiKomandu(m);
    if (podatci == null) {
      System.out.println("Greška u komandi!");
      return;
    }

    Tvrtka tvrtkaInstance = Tvrtka.getInstance(podatci);
    tvrtkaInstance.citajPodatke();
    tvrtkaInstance.ucitajPodatke();
    tvrtkaInstance.interakcija();
  }

  public void interakcija() {
    Scanner scanner = new Scanner(System.in);
    String unos;
    do {
      System.out.println("Unesite komandu:");
      unos = scanner.nextLine().trim();
      switch (unos) {
        case "IP":
          try {
            uredZaPrijem.ispisTablicePrimljenihPaketa(uredZaDostavu);
          } catch (Exception e) {
            Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, "-", "Nema podataka!");
          }
          break;
        case "Q":
          System.out.println("Program je završen.");
          break;
        default:
          if (unos.startsWith("VR ")) {
            izvrsiVirtualnoVrijeme(unos);
          } else {
            System.out.println("Pogrešan unos!");
          }
          break;
      }
    } while (!unos.equals("Q"));
    scanner.close();
  }

  public void citajPodatke() {
    citacPrijemaPaketa.postaviPutanju(putanjaDoPP);
    citacVozila.postaviPutanju(putanjaDoPV);
    citacVrstaPaketa.postaviPutanju(putanjaDoVP);

    citacPrijemaPaketa.citajPodatke();
    citacVozila.citajPodatke();
    citacVrstaPaketa.citajPodatke();
  }

  public void ucitajPodatke() {
    prijemi = (List<PrijemPaketa>) citacPrijemaPaketa.dohvatiPodatke();
    vozila = (List<Vozilo>) citacVozila.dohvatiPodatke();
    vrste = (List<VrstaPaketa>) citacVrstaPaketa.dohvatiPodatke();

    uredZaPrijem = new UredZaPrijem(vrste, maxTezina);
    uredZaPrijem.preuzmiPodatkeIzPrijema(prijemi);
    uredZaPrijem.postaviTrenutnoVirtualnoVrijeme(virtualnoVrijeme);
  }

  private Map<Paket, Double> dohvatiCijeneDostave(List<Paket> paketi) {
    Map<Paket, Double> cijene = new HashMap<>();
    for (Paket paket : paketi) {
      Double cijena = uredZaPrijem.dohvatiCijenuDostaveZaPaket(paket);
      if (cijena != null) {
        cijene.put(paket, cijena);
      }
    }
    return cijene;
  }

  private void izvrsiVirtualnoVrijeme(String unos) {
    int brojSati = Integer.parseInt(unos.split(" ")[1]);
    LocalDateTime krajIzvrsavanja = virtualnoVrijeme.plusHours(brojSati);

    while (virtualnoVrijeme.isBefore(krajIzvrsavanja)) {
      azurirajVirtualnoVrijeme();

      if (virtualnoVrijeme.toLocalTime().isAfter(krajRada)) {
        System.out.println("Kraj radnog vremena!");
        break;
      }
    }
  }

  private void azurirajVirtualnoVrijeme() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    virtualnoVrijeme = virtualnoVrijeme.plusSeconds(mnoziteljSekunde);
    System.out.println("Virtualno vrijeme: " + virtualnoVrijeme);

    if (virtualnoVrijeme.toLocalTime().isBefore(pocetakRada)) {
      System.out.println("Prije početka radnog vremena!");
      return;
    }

    List<Paket> primljeniPaketi = uredZaPrijem.dohvatiPrimljenePakete();
    Map<Paket, Double> cijeneDostave = dohvatiCijeneDostave(primljeniPaketi);

    uredZaDostavu = new UredZaDostavu(vozila, vrijemeIsporuke, primljeniPaketi, cijeneDostave);
    uredZaDostavu.postaviTrenutnoVirtualnoVrijeme(virtualnoVrijeme);
    uredZaDostavu.ukrcavanjePaketa();
    uredZaDostavu.isporukaPaketa();
  }


  private Matcher provjeriArgumente(String s) {
    String sintaksa =
        "^(?=.*--vp (?<vp>[^\\s]+\\.csv))(?=.*--pv (?<pv>[^\\s]+\\.csv))(?=.*--pp (?<pp>[^\\s]+\\.csv))(?=.*--mt (?<mt>\\d+(?=\\s|$)))(?=.*--vi (?<vi>\\d+(?=\\s|$)))(?=.*--vs '?(?<vs>(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.\\d{4}\\. (0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9])'?)(?=.*--ms (?<ms>\\d+(?=\\s|$)))(?=.*--pr '?(?<pr>(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9])'?)(?=.*--kr '?(?<kr>(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9])'?).+$";
    Pattern p = Pattern.compile(sintaksa);
    Matcher m = p.matcher(s);
    if (!m.matches()) {
      return null;
    }
    return m;
  }

  private static Map<String, String> obradiKomandu(Matcher m) {
    Map<String, String> grupe = new HashMap<>();
    grupe.put("vp", m.group("vp"));
    grupe.put("pv", m.group("pv"));
    grupe.put("pp", m.group("pp"));
    grupe.put("mt", m.group("mt"));
    grupe.put("vi", m.group("vi"));
    grupe.put("vs", m.group("vs"));
    grupe.put("ms", m.group("ms"));
    grupe.put("pr", m.group("pr"));
    grupe.put("kr", m.group("kr"));
    return grupe;
  }

}
