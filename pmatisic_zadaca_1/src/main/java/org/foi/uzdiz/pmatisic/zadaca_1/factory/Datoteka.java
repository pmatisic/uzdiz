package org.foi.uzdiz.pmatisic.zadaca_1.factory;

import java.util.List;

public interface Datoteka {
  void citajPodatke();

  void postaviPutanju(String putanja);

  List<Object> dohvatiPodatke();
}
