package org.foi.uzdiz.pmatisic.zadaca_3.builder;

import java.time.LocalDateTime;
import org.foi.uzdiz.pmatisic.zadaca_3.model.UslugaDostave;

public interface Builder {

  Builder oznaka(String oznaka);

  Builder vrijemePrijema(LocalDateTime vrijemePrijema);

  Builder posiljatelj(String posiljatelj);

  Builder primatelj(String primatelj);

  Builder vrstaPaketa(String vrstaPaketa);

  Builder visina(double visina);

  Builder sirina(double sirina);

  Builder duzina(double duzina);

  Builder tezina(double tezina);

  Builder uslugaDostave(UslugaDostave uslugaDostave);

  Builder iznosPouzeca(double iznosPouzeca);

}
