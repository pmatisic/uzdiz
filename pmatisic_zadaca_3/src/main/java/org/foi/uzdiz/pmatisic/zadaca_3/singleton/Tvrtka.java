package org.foi.uzdiz.pmatisic.zadaca_3.singleton;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.uzdiz.pmatisic.zadaca_3.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_3.chain.Handler;
import org.foi.uzdiz.pmatisic.zadaca_3.chain.IPHandler;
import org.foi.uzdiz.pmatisic.zadaca_3.chain.POHandler;
import org.foi.uzdiz.pmatisic.zadaca_3.chain.PPHandler;
import org.foi.uzdiz.pmatisic.zadaca_3.chain.PSHandler;
import org.foi.uzdiz.pmatisic.zadaca_3.chain.QHandler;
import org.foi.uzdiz.pmatisic.zadaca_3.chain.SVHandler;
import org.foi.uzdiz.pmatisic.zadaca_3.chain.VRHandler;
import org.foi.uzdiz.pmatisic.zadaca_3.factory.DatotekaFactory;
import org.foi.uzdiz.pmatisic.zadaca_3.factory.MjestoDatoteka;
import org.foi.uzdiz.pmatisic.zadaca_3.factory.OsobaDatoteka;
import org.foi.uzdiz.pmatisic.zadaca_3.factory.PodrucjeDatoteka;
import org.foi.uzdiz.pmatisic.zadaca_3.factory.PrijemPaketaDatoteka;
import org.foi.uzdiz.pmatisic.zadaca_3.factory.UlicaDatoteka;
import org.foi.uzdiz.pmatisic.zadaca_3.factory.VoziloDatoteka;
import org.foi.uzdiz.pmatisic.zadaca_3.factory.VrstaPaketaDatoteka;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Mjesto;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Osoba;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Podrucje;
import org.foi.uzdiz.pmatisic.zadaca_3.model.PrijemPaketa;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Ulica;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Vozilo;
import org.foi.uzdiz.pmatisic.zadaca_3.model.VrstaPaketa;
import org.foi.uzdiz.pmatisic.zadaca_3.observer.ServisObavijesti;
import org.foi.uzdiz.pmatisic.zadaca_3.pomagala.Provjera;
import org.foi.uzdiz.pmatisic.zadaca_3.pomagala.UredZaDostavu;
import org.foi.uzdiz.pmatisic.zadaca_3.pomagala.UredZaPrijem;

public class Tvrtka {

  private static volatile Tvrtka instance;
  private static Map<String, String> podatci = new HashMap<>();
  public UredZaPrijem uredZaPrijem;
  public UredZaDostavu uredZaDostavu;
  private ServisObavijesti servisObavijesti;
  private Object citacVrstaPaketa = new VrstaPaketaDatoteka();
  private Object citacVozila = new VoziloDatoteka();
  private Object citacPrijemaPaketa = new PrijemPaketaDatoteka();
  private Object citacOsoba = new OsobaDatoteka();
  private Object citacMjesta = new MjestoDatoteka();
  private Object citacUlica = new UlicaDatoteka();
  private Object citacPodrucja = new PodrucjeDatoteka();
  private List<Paket> paketiZaObavijesti;
  private List<VrstaPaketa> vrste;
  private List<Vozilo> vozila;
  private List<PrijemPaketa> prijemi;
  public List<Osoba> osobe;
  private List<Mjesto> mjesta;
  public List<Ulica> ulice;
  private List<Podrucje> podrucja;
  private String gps;
  private int maxTezina;
  private int vrijemeIsporuke;
  private int mnoziteljSekunde;
  private int isporuka;
  private LocalDateTime virtualnoVrijeme;
  private LocalTime pocetakRada;
  private LocalTime krajRada;

  private Tvrtka() {
    this.gps = podatci.get("gps").toString();
    this.maxTezina = Integer.parseInt(podatci.get("mt"));
    this.vrijemeIsporuke = Integer.parseInt(podatci.get("vi"));
    this.mnoziteljSekunde = Integer.parseInt(podatci.get("ms"));
    this.isporuka = Integer.parseInt(podatci.get("isporuka"));
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
    this.virtualnoVrijeme = LocalDateTime.parse(podatci.get("vs"), dateTimeFormatter);
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    this.pocetakRada = LocalTime.parse(podatci.get("pr"), timeFormatter);
    this.krajRada = LocalTime.parse(podatci.get("kr"), timeFormatter);
    this.servisObavijesti = new ServisObavijesti();
  }

  public static Tvrtka getInstance() {
    Tvrtka result = instance;
    if (result == null) {
      synchronized (Tvrtka.class) {
        result = instance;
        if (result == null) {
          instance = result = new Tvrtka();
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

    podatci = provjera.dohvatiPodatke();
    if (podatci == null || podatci.isEmpty()) {
      System.out.println("Greška u dohvaćanju podataka!");
      return;
    }

    Tvrtka tvrtkaInstance = Tvrtka.getInstance();
    tvrtkaInstance.citajPodatke(podatci);
    tvrtkaInstance.inicijaliziraj();
    tvrtkaInstance.interakcija();
  }

  public void interakcija() {
    Handler ipHandler = new IPHandler();
    Handler poHandler = new POHandler();
    Handler ppHandler = new PPHandler();
    Handler psHandler = new PSHandler();
    Handler qHandler = new QHandler();
    Handler svHandler = new SVHandler();
    Handler vrHandler = new VRHandler();

    ipHandler.setNextHandler(poHandler).setNextHandler(ppHandler).setNextHandler(psHandler)
        .setNextHandler(qHandler).setNextHandler(svHandler).setNextHandler(vrHandler);

    Scanner scanner = new Scanner(System.in);
    String unos;
    boolean continueLoop = true;

    do {
      System.out.println("Unesite komandu:");
      unos = scanner.nextLine().trim();
      continueLoop = ipHandler.handle(unos);
    } while (continueLoop);

    scanner.close();
  }

  public void citajPodatke(Map<String, String> podatci) {
    citacVrstaPaketa = DatotekaFactory.stvoriDatoteku("vp", podatci);
    citacVozila = DatotekaFactory.stvoriDatoteku("pv", podatci);
    citacPrijemaPaketa = DatotekaFactory.stvoriDatoteku("pp", podatci);
    citacOsoba = DatotekaFactory.stvoriDatoteku("po", podatci);
    citacMjesta = DatotekaFactory.stvoriDatoteku("pm", podatci);
    citacUlica = DatotekaFactory.stvoriDatoteku("pu", podatci);
    citacPodrucja = DatotekaFactory.stvoriDatoteku("pmu", podatci);
    ((VrstaPaketaDatoteka) citacVrstaPaketa).citajPodatke();
    ((VoziloDatoteka) citacVozila).citajPodatke();
    ((PrijemPaketaDatoteka) citacPrijemaPaketa).citajPodatke();
    ((OsobaDatoteka) citacOsoba).citajPodatke();
    ((MjestoDatoteka) citacMjesta).citajPodatke();
    ((UlicaDatoteka) citacUlica).citajPodatke();
    ((PodrucjeDatoteka) citacPodrucja).citajPodatke();
  }

  public void inicijaliziraj() {
    vrste = (List<VrstaPaketa>) ((VrstaPaketaDatoteka) citacVrstaPaketa).dohvatiPodatke();
    vozila = (List<Vozilo>) ((VoziloDatoteka) citacVozila).dohvatiPodatke();
    prijemi = (List<PrijemPaketa>) ((PrijemPaketaDatoteka) citacPrijemaPaketa).dohvatiPodatke();
    osobe = (List<Osoba>) ((OsobaDatoteka) citacOsoba).dohvatiPodatke();
    mjesta = (List<Mjesto>) ((MjestoDatoteka) citacMjesta).dohvatiPodatke();
    ulice = (List<Ulica>) ((UlicaDatoteka) citacUlica).dohvatiPodatke();
    podrucja = (List<Podrucje>) ((PodrucjeDatoteka) citacPodrucja).dohvatiPodatke();

    uredZaPrijem = new UredZaPrijem(vrste, maxTezina);

    uredZaPrijem.preuzmiPodatkeIzPrijema(prijemi);

    paketiZaObavijesti = uredZaPrijem.dohvatiPaketeZaObavijesti();

    servisObavijesti.automatskaPretplata(paketiZaObavijesti);

    uredZaDostavu = new UredZaDostavu(vozila, vrijemeIsporuke, isporuka, gps);

    uredZaDostavu.preuzmiPodatkeOProstoru(podrucja, mjesta, ulice);

    uredZaDostavu.izgradiCompositeStrukturu();

    uredZaDostavu.dohvatiOsobe(osobe);
  }

  public void izvrsiVirtualnoVrijeme(String unos) {
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
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
    System.out.println("Virtualno vrijeme: " + virtualnoVrijeme.format(dateTimeFormatter));

    if (virtualnoVrijeme.toLocalTime().isBefore(pocetakRada)
        || virtualnoVrijeme.toLocalTime().isAfter(krajRada)) {
      System.out.println("Izvan radnog vremena!");
      return;
    }

    uredZaPrijem.postaviVirtualnoVrijeme(virtualnoVrijeme);
    uredZaDostavu.postaviTrenutnoVirtualnoVrijeme(virtualnoVrijeme);

    var primljeniPaketi = uredZaPrijem.dohvatiPrimljenePakete();
    for (Paket paket : primljeniPaketi) {
      if (paket.getVrijemePrijema().isBefore(virtualnoVrijeme)) {
        boolean posiljateljPretplacen =
            servisObavijesti.jePretplacen(paket.getPosiljatelj(), paket.getOznaka());
        boolean primateljPretplacen =
            servisObavijesti.jePretplacen(paket.getPrimatelj(), paket.getOznaka());
        if (posiljateljPretplacen) {
          System.out.println("Paket " + paket.getOznaka() + " je zaprimljen za pošiljatelja "
              + paket.getPosiljatelj() + ".");
        }
        if (primateljPretplacen) {
          System.out.println("Paket " + paket.getOznaka() + " je zaprimljen za primatelja "
              + paket.getPrimatelj() + ".");
        }
      }
    }

    uredZaDostavu.azurirajPaketeZaDostavu(primljeniPaketi);
    uredZaDostavu.ukrcajPaket();
  }

  public void promijeniStatusObavijesti(String unos) {
    Pattern pattern =
        Pattern.compile("PO\\s+'\\s*([\\p{L} .'-]+?)\\s*'\\s+([\\p{L}\\p{N}]+)\\s+(D|N)");
    Matcher matcher = pattern.matcher(unos);

    if (matcher.matches()) {
      String osoba = matcher.group(1).trim();
      String oznaka = matcher.group(2).trim();
      String status = matcher.group(3).trim();
      boolean statusObavijesti = status.equals("D");

      if (statusObavijesti) {
        servisObavijesti.subscribe(osoba, oznaka);
      } else {
        servisObavijesti.unsubscribe(osoba, oznaka);
      }

      servisObavijesti.notifyObservers(oznaka, osoba, statusObavijesti);
    } else {
      System.out.println("Neispravan format naredbe.");
    }
  }

  public void promijeniStanjeVozila(String unos) {
    Pattern pattern = Pattern.compile("PS\\s+([A-Z0-9ČŽŠĆĐ]+)\\s+(A|NI|NA)");
    Matcher matcher = pattern.matcher(unos);

    if (matcher.matches()) {
      String vozilo = matcher.group(1).trim();
      String status = matcher.group(2).trim();

      uredZaDostavu.promijeniStanjeVozila(vozilo, status);
    } else {
      System.out.println("Neispravan format naredbe.");
    }
  }

}
