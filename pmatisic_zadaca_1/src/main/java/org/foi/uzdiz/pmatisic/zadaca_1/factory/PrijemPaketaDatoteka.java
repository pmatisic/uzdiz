package org.foi.uzdiz.pmatisic.zadaca_1.factory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.pmatisic.zadaca_1.model.PrijemPaketa;
import org.foi.uzdiz.pmatisic.zadaca_1.model.UslugaDostave;
import org.foi.uzdiz.pmatisic.zadaca_1.pomagala.Greske;

public class PrijemPaketaDatoteka implements Datoteka {

  private String putanjaDatoteke;
  List<PrijemPaketa> paketi = new ArrayList<>();

  @Override
  public void postaviPutanju(String putanja) {
    this.putanjaDatoteke = putanja;
  }

  @Override
  public void citajPodatke() {
    try {
      Path staza = Path.of(putanjaDatoteke);

      if (!Files.exists(staza) || Files.isDirectory(staza) || !Files.isReadable(staza)) {
        throw new IOException("Datoteka '" + putanjaDatoteke + "' ne postoji ili nije čitljiva!");
      }

      var linije = Files.readAllLines(staza, Charset.forName("UTF-8"));
      paketi.clear();

      for (String linija : linije) {
        String[] dijelovi = linija.split(";");

        if (dijelovi.length != 11) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija, "Pogrešan broj atributa");
          continue;
        }

        try {
          PrijemPaketa prijemPaketa = new PrijemPaketa(dijelovi[0],
              PrijemPaketa.konvertirajVrijeme(dijelovi[1]), dijelovi[2], dijelovi[3], dijelovi[4],
              Double.parseDouble(dijelovi[5]), Double.parseDouble(dijelovi[6]),
              Double.parseDouble(dijelovi[7]), Double.parseDouble(dijelovi[8]),
              UslugaDostave.valueOf(dijelovi[9]), Double.parseDouble(dijelovi[10]));
          paketi.add(prijemPaketa);
        } catch (Exception e) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija,
              "Greška prilikom obrade retka");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<Object> dohvatiPodatke() {
    List<Object> rezultat = new ArrayList<>(paketi);
    return rezultat;
  }

}
