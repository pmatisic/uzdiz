package org.foi.uzdiz.pmatisic.zadaca_3.decorator;

import java.time.format.DateTimeFormatter;
import org.foi.uzdiz.pmatisic.zadaca_3.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_3.pomagala.UredZaDostavu;
import org.foi.uzdiz.pmatisic.zadaca_3.singleton.Tvrtka;

public class IspisPaketa implements Ispis {

  @Override
  public void ispisTablicePrimljenihPaketa() {
    System.out.println(
        "+-----------+----------------------+----------------------+----------------------+----------------------+---------------------------+----------------------+----------------------+");
    System.out.println(
        "|   Oznaka  |   Vrijeme prijema    |   Vrsta paketa       |   Vrsta usluge       |   Status isporuke    |   Vrijeme preuzimanja     |     Iznos dostave    |     Iznos poduzeća   |");
    System.out.println(
        "+-----------+----------------------+----------------------+----------------------+----------------------+---------------------------+----------------------+----------------------+");

    for (Paket paket : Tvrtka.getInstance().uredZaPrijem.zaprimljeniPaketi) {
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
      String statusIsporuke =
          UredZaDostavu.statusPaketa.getOrDefault(paket.getOznaka(), "Na čekanju");
      String vrijemePreuzimanja = "-";
      if (statusIsporuke.equals("Dostavljeno")
          && UredZaDostavu.vrijemePreuzimanjaPaketa.containsKey(paket.getOznaka())) {
        vrijemePreuzimanja =
            UredZaDostavu.vrijemePreuzimanjaPaketa.get(paket.getOznaka()).format(dateTimeFormatter);
      }
      Double iznosDostave =
          Tvrtka.getInstance().uredZaPrijem.mapaCijenaDostave.getOrDefault(paket, 0.0);
      System.out.printf("| %-6s | %-20s | %-20s | %-20s | %-20s | %-25s | %20.2f | %20.2f |\n",
          paket.getOznaka(), paket.getVrijemePrijema().format(dateTimeFormatter),
          paket.getVrstaPaketa(), paket.getUslugaDostave(), statusIsporuke, vrijemePreuzimanja,
          iznosDostave, paket.getIznosPouzeca());
    }
    System.out.println(
        "+-----------+----------------------+----------------------+----------------------+----------------------+---------------------------+----------------------+----------------------+");
  }

}
