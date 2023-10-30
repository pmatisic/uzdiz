package org.foi.uzdiz.pmatisic.zadaca_1.factory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.pmatisic.zadaca_1.model.Vozilo;
import org.foi.uzdiz.pmatisic.zadaca_1.pomagala.Greske;

public class VoziloDatoteka implements Datoteka {

  private String putanjaDatoteke;

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
      List<Vozilo> vozila = new ArrayList<>();

      for (String linija : linije) {
        String[] dijelovi = linija.split(";");

        if (dijelovi.length != 5) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija, "Pogrešan broj atributa");
          continue;
        }

        try {
          Vozilo vozilo = new Vozilo(dijelovi[0], dijelovi[1], Double.parseDouble(dijelovi[2]),
              Double.parseDouble(dijelovi[3]), Integer.parseInt(dijelovi[4]));
          vozila.add(vozilo);
        } catch (Exception e) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija,
              "Greška prilikom obrade retka");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
