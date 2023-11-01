package org.foi.uzdiz.pmatisic.zadaca_1.pomagala;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.uzdiz.pmatisic.zadaca_1.singleton.Tvrtka;
import org.foi.uzdiz.pmatisic.zadaca_1.singleton.VirtualniSat;

public class Main {

  public static void main(String[] args) {
    Main p = new Main();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < args.length; i++) {
      sb.append(args[i]).append(" ");
    }

    String s = sb.toString().trim();
    Matcher m = p.provjeriArgumente(s);
    if (m == null) {
      System.out.println("Greška u argumentima ili fali argument, provjerite unos!");
      return;
    }

    Map<String, String> podatci = obradiKomandu(m);
    if (podatci == null) {
      System.out.println("Greška u komandi!");
      return;
    }

    Tvrtka.getInstance(podatci);
    String pocetnoVrijeme = podatci.get("vs");
    int mnoziteljSekunde = Integer.parseInt(podatci.get("ms"));
    VirtualniSat virtualniSat = VirtualniSat.getInstance(pocetnoVrijeme, mnoziteljSekunde);


    Scanner scanner = new Scanner(System.in);
    String unos;
    do {
      System.out.println("Unesite komandu:");
      unos = scanner.nextLine();

      if (unos.equals("IP")) {
        pregledStatusaPaketa();
      } else if (unos.startsWith("VR ")) {
        izvrsavanjePrograma(unos);
      } else {
        System.out.println("Pogrešan unos!");
      }
    } while (!unos.equals("Q"));
    scanner.close();
    System.out.println("Program je završen.");
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

  public String getVirtualnoVrijeme() {
    return virtualniSat.dohvatiTrenutnoVrijeme();
  }

}
