package org.foi.uzdiz.pmatisic.zadaca_1.factory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.pmatisic.zadaca_1.model.Paket;
import org.foi.uzdiz.pmatisic.zadaca_1.model.UslugaDostave;
import org.foi.uzdiz.pmatisic.zadaca_1.pomagala.Greske;

public class PaketiDatoteka implements Datoteka {

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
      List<Paket> paketi = new ArrayList<>();

      for (String linija : linije) {
        String[] dijelovi = linija.split(";");

        if (dijelovi.length != 11) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija, "Pogrešan broj atributa");
          continue;
        }

        try {
          Paket paket = new Paket(dijelovi[0], Paket.konvertirajVrijeme(dijelovi[1]), dijelovi[2],
              dijelovi[3], dijelovi[4], Double.parseDouble(dijelovi[5]),
              Double.parseDouble(dijelovi[6]), Double.parseDouble(dijelovi[7]),
              Double.parseDouble(dijelovi[8]), UslugaDostave.valueOf(dijelovi[9]),
              Double.parseDouble(dijelovi[10]));
          paketi.add(paket);
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
