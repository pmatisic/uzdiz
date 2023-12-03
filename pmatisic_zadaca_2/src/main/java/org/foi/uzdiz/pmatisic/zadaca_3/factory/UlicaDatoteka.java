package org.foi.uzdiz.pmatisic.zadaca_3.factory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.pmatisic.zadaca_2.model.Ulica;
import org.foi.uzdiz.pmatisic.zadaca_2.pomagala.Greske;

public class UlicaDatoteka implements Datoteka<Ulica> {

  private String putanjaDatoteke;
  private List<Ulica> ulice = new ArrayList<>();

  @Override
  public void postaviPutanju(String putanja) {
    this.putanjaDatoteke = putanja;
  }

  @Override
  public List<Ulica> dohvatiPodatke() {
    return new ArrayList<>(ulice);
  }

  @Override
  public void citajPodatke() {
    try {
      Path staza = Path.of(putanjaDatoteke);

      if (!Files.exists(staza) || Files.isDirectory(staza) || !Files.isReadable(staza)) {
        throw new IOException("Datoteka '" + putanjaDatoteke + "' ne postoji ili nije čitljiva!");
      }

      List<String> linije = Files.readAllLines(staza, Charset.forName("UTF-8"));
      ulice.clear();

      if (!linije.isEmpty() && linije.get(0).startsWith("id")) {
        linije.remove(0);
      }

      for (String linija : linije) {
        String[] dijelovi = linija.split(";");

        if (dijelovi.length != 7) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija, "Pogrešan broj atributa");
          continue;
        }

        try {
          int id = Integer.parseInt(dijelovi[0].trim());
          String naziv = dijelovi[1].trim();
          double gpsLat1 = Double.parseDouble(dijelovi[2].trim());
          double gpsLon1 = Double.parseDouble(dijelovi[3].trim());
          double gpsLat2 = Double.parseDouble(dijelovi[4].trim());
          double gpsLon2 = Double.parseDouble(dijelovi[5].trim());
          int najveciKucniBroj = Integer.parseInt(dijelovi[6].trim());

          Ulica ulica = new Ulica(id, naziv, gpsLat1, gpsLon1, gpsLat2, gpsLon2, najveciKucniBroj);
          ulice.add(ulica);
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
