package org.foi.uzdiz.pmatisic.zadaca_2.factory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.foi.uzdiz.pmatisic.zadaca_2.model.StatusVozila;
import org.foi.uzdiz.pmatisic.zadaca_2.model.Vozilo;
import org.foi.uzdiz.pmatisic.zadaca_2.pomagala.Greske;

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

      Set<String> dozvoljeniStatusi = new HashSet<>(Arrays.asList("A", "NA", "NI"));

      for (String linija : linije) {
        if (linija.trim().isEmpty()) {
          continue;
        }

        String[] dijelovi =
            Arrays.stream(linija.split(";")).map(String::trim).toArray(String[]::new);

        if (dijelovi.length != 8) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija, "Pogrešan broj atributa");
          continue;
        }

        try {
          double kapacitetTezine = Double.parseDouble(dijelovi[2].replace(',', '.'));
          double kapacitetProstora = Double.parseDouble(dijelovi[3].replace(',', '.'));
          int redoslijed = Integer.parseInt(dijelovi[4]);
          double prosjecnaBrzina = Double.parseDouble(dijelovi[5]);
          String podrucjaPoRangu = dijelovi[6];
          String statusString = dijelovi[7];
          if (!dozvoljeniStatusi.contains(statusString)) {
            Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija,
                "Nevažeći status vozila: " + statusString);
            continue;
          }
          StatusVozila status = StatusVozila.valueOf(statusString);

          Vozilo vozilo = new Vozilo(dijelovi[0], dijelovi[1], kapacitetTezine, kapacitetProstora,
              redoslijed, prosjecnaBrzina, podrucjaPoRangu, status, null);
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
