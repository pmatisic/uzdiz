package org.foi.uzdiz.pmatisic.zadaca_1.factory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.pmatisic.zadaca_1.model.Vozilo;
import org.foi.uzdiz.pmatisic.zadaca_1.pomagala.Greske;

public class VoziloDatoteka implements Datoteka<Vozilo> {

  private String putanjaDatoteke;
  private List<Vozilo> vozila = new ArrayList<>();

  @Override
  public void postaviPutanju(String putanja) {
    this.putanjaDatoteke = putanja;
  }

  @Override
  public List<Vozilo> dohvatiPodatke() {
    return new ArrayList<>(vozila);
  }

  @Override
  public void citajPodatke() {
    try {
      Path staza = Path.of(putanjaDatoteke);

      if (!Files.exists(staza) || Files.isDirectory(staza) || !Files.isReadable(staza)) {
        throw new IOException("Datoteka '" + putanjaDatoteke + "' ne postoji ili nije čitljiva!");
      }

      List<String> linije = Files.readAllLines(staza, Charset.forName("UTF-8"));
      vozila.clear();

      if (!linije.isEmpty() && linije.get(0).startsWith("Registracija")) {
        linije.remove(0);
      }

      for (String linija : linije) {
        String[] dijelovi = linija.split(";");

        if (dijelovi.length != 5) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija, "Pogrešan broj atributa");
          continue;
        }

        try {
          double kapacitetTezine = Double.parseDouble(dijelovi[2].replace(',', '.'));
          double kapacitetProstora = Double.parseDouble(dijelovi[3].replace(',', '.'));
          int redoslijed = Integer.parseInt(dijelovi[4]);

          Vozilo vozilo =
              new Vozilo(dijelovi[0], dijelovi[1], kapacitetTezine, kapacitetProstora, redoslijed);
          vozila.add(vozilo);
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
