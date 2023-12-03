package org.foi.uzdiz.pmatisic.zadaca_2.singleton;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import org.foi.uzdiz.pmatisic.zadaca_2.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_2.model.PrijemPaketa;
import org.foi.uzdiz.pmatisic.zadaca_2.model.Vozilo;
import org.foi.uzdiz.pmatisic.zadaca_2.model.VrstaPaketa;
import org.foi.uzdiz.pmatisic.zadaca_2.pomagala.Greske;
import org.foi.uzdiz.pmatisic.zadaca_2.pomagala.Provjera;
import org.foi.uzdiz.pmatisic.zadaca_2.pomagala.UredZaDostavu;
import org.foi.uzdiz.pmatisic.zadaca_2.pomagala.UredZaPrijem;
import org.foi.uzdiz.pmatisic.zadaca_3.factory.PrijemPaketaDatoteka;
import org.foi.uzdiz.pmatisic.zadaca_3.factory.VoziloDatoteka;
import org.foi.uzdiz.pmatisic.zadaca_3.factory.VrstaPaketaDatoteka;

public class Tvrtka {

  private static volatile Tvrtka instance;
  private List<PrijemPaketa> prijemi;
  private List<Vozilo> vozila;
  private List<VrstaPaketa> vrste;
  private UredZaPrijem uredZaPrijem;
  private UredZaDostavu uredZaDostavu;
  private PrijemPaketaDatoteka citacPrijemaPaketa = new PrijemPaketaDatoteka();
  private VoziloDatoteka citacVozila = new VoziloDatoteka();
  private VrstaPaketaDatoteka citacVrstaPaketa = new VrstaPaketaDatoteka();
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
    if (args.length != 1) {
      System.out.println("Greška u argumentima ili fali argument, provjerite unos!");
      return;
    }

    String regexZaArgument = "^[^\\s]+\\.txt$";
    if (!Pattern.matches(regexZaArgument, args[0])) {
      System.out.println("Neispravan format argumenta, očekivana je .txt datoteka!");
      return;
    }

    Provjera provjera = new Provjera(args[0]);
    if (!provjera.provjeriDatoteku()) {
      return;
    }

    Map<String, String> podatci = provjera.dohvatiPodatke();
    if (podatci == null || podatci.isEmpty()) {
      System.out.println("Greška u dohvaćanju podataka!");
      return;
    }

    for (Map.Entry<String, String> entry : podatci.entrySet()) {
      System.out.println("Ključ: " + entry.getKey() + ", Vrijednost: " + entry.getValue());
    }

    // Tvrtka tvrtkaInstance = Tvrtka.getInstance(podatci);
    // tvrtkaInstance.citajPodatke();
    // tvrtkaInstance.ucitajPodatke();
    // tvrtkaInstance.interakcija();
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

}
