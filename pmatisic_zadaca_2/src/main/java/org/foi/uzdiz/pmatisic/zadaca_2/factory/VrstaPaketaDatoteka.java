package org.foi.uzdiz.pmatisic.zadaca_2.factory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.pmatisic.zadaca_2.model.VrstaPaketa;
import org.foi.uzdiz.pmatisic.zadaca_2.pomagala.Greske;

public class VrstaPaketaDatoteka implements Datoteka<VrstaPaketa> {

  private String putanjaDatoteke;
  private List<VrstaPaketa> vrste = new ArrayList<>();

  @Override
  public void postaviPutanju(String putanja) {
    this.putanjaDatoteke = putanja;
  }

  @Override
  public List<VrstaPaketa> dohvatiPodatke() {
    return new ArrayList<>(vrste);
  }

  @Override
  public void citajPodatke() {
    try {
      Path staza = Path.of(putanjaDatoteke);

      if (!Files.exists(staza) || Files.isDirectory(staza) || !Files.isReadable(staza)) {
        throw new IOException("Datoteka '" + putanjaDatoteke + "' ne postoji ili nije čitljiva!");
      }

      List<String> linije = Files.readAllLines(staza, Charset.forName("UTF-8"));
      vrste.clear();

      if (!linije.isEmpty() && linije.get(0).startsWith("Oznaka")) {
        linije.remove(0);
      }

      for (String linija : linije) {
        String[] dijelovi = linija.split(";");

        if (dijelovi.length != 10) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija, "Pogrešan broj atributa");
          continue;
        }

        try {
          VrstaPaketa vrstaPaketa = new VrstaPaketa(dijelovi[0], dijelovi[1],
              Double.parseDouble(dijelovi[2].replace(',', '.')),
              Double.parseDouble(dijelovi[3].replace(',', '.')),
              Double.parseDouble(dijelovi[4].replace(',', '.')),
              Double.parseDouble(dijelovi[5].replace(',', '.')),
              Double.parseDouble(dijelovi[6].replace(',', '.')),
              Double.parseDouble(dijelovi[7].replace(',', '.')),
              Double.parseDouble(dijelovi[8].replace(',', '.')),
              Double.parseDouble(dijelovi[9].replace(',', '.')));
          vrste.add(vrstaPaketa);
        } catch (NumberFormatException e) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija,
              "Greška prilikom konverzije broja: " + e.getMessage());
        } catch (Exception e) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija,
              "Opća greška prilikom obrade retka: " + e.getMessage());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
