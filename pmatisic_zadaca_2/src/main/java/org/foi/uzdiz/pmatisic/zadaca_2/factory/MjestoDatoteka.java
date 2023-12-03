package org.foi.uzdiz.pmatisic.zadaca_2.factory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.foi.uzdiz.pmatisic.zadaca_2.model.Mjesto;
import org.foi.uzdiz.pmatisic.zadaca_2.pomagala.Greske;

public class MjestoDatoteka implements Datoteka<Mjesto> {

  private String putanjaDatoteke;
  private List<Mjesto> mjesta = new ArrayList<>();

  @Override
  public void postaviPutanju(String putanja) {
    this.putanjaDatoteke = putanja;
  }

  @Override
  public List<Mjesto> dohvatiPodatke() {
    return new ArrayList<>(mjesta);
  }

  @Override
  public void citajPodatke() {
    try {
      Path staza = Path.of(putanjaDatoteke);

      if (!Files.exists(staza) || Files.isDirectory(staza) || !Files.isReadable(staza)) {
        throw new IOException("Datoteka '" + putanjaDatoteke + "' ne postoji ili nije čitljiva!");
      }

      List<String> linije = Files.readAllLines(staza, Charset.forName("UTF-8"));
      mjesta.clear();

      if (!linije.isEmpty() && linije.get(0).startsWith("id")) {
        linije.remove(0);
      }

      for (String linija : linije) {
        String[] dijelovi = linija.split(";");

        if (dijelovi.length != 3) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija, "Pogrešan broj atributa");
          continue;
        }

        try {
          int id = Integer.parseInt(dijelovi[0].trim());
          String naziv = dijelovi[1].trim();
          List<Integer> ulice = Arrays.stream(dijelovi[2].split(",")).map(String::trim)
              .map(Integer::parseInt).collect(Collectors.toList());

          Mjesto mjesto = new Mjesto(id, naziv, ulice);
          mjesta.add(mjesto);
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
