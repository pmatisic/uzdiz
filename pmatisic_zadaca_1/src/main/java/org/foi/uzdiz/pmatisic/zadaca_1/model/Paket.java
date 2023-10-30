package org.foi.uzdiz.pmatisic.zadaca_1.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Paket(String oznaka, LocalDateTime vrijemePrijema, String posiljatelj,
    String primatelj, String vrstaPaketa, double visina, double sirina, double duzina,
    double tezina, UslugaDostave uslugaDostave, double iznosPouzeca) {

  public static LocalDateTime konvertirajVrijeme(String vrijeme) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");
    return LocalDateTime.parse(vrijeme, formatter);
  }
}
