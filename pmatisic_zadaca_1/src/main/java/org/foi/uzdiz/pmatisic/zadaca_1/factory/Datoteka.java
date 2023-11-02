package org.foi.uzdiz.pmatisic.zadaca_1.factory;

import java.util.List;

public interface Datoteka<T> {

  void citajPodatke();

  void postaviPutanju(String putanja);

  List<T> dohvatiPodatke();

}
