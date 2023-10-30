package org.foi.uzdiz.pmatisic.zadaca_1.factory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.pmatisic.zadaca_1.model.VrstaPaketa;
import org.foi.uzdiz.pmatisic.zadaca_1.pomagala.Greske;

public class VrstePaketaDatoteka implements Datoteka {

  private String putanjaDatoteke;
  List<VrstaPaketa> vrste = new ArrayList<>();

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
      vrste.clear();

      for (String linija : linije) {
        String[] dijelovi = linija.split(";");

        if (dijelovi.length != 10) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija, "Pogrešan broj atributa");
          continue;
        }

        try {
          VrstaPaketa vrstaPaketa = new VrstaPaketa(dijelovi[0], dijelovi[1],
              Double.parseDouble(dijelovi[2]), Double.parseDouble(dijelovi[3]),
              Double.parseDouble(dijelovi[4]), Double.parseDouble(dijelovi[5]),
              Double.parseDouble(dijelovi[6]), Double.parseDouble(dijelovi[7]),
              Double.parseDouble(dijelovi[8]), Double.parseDouble(dijelovi[9]));
          vrste.add(vrstaPaketa);
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
    List<Object> rezultat = new ArrayList<>(vrste);
    return rezultat;
  }

}
