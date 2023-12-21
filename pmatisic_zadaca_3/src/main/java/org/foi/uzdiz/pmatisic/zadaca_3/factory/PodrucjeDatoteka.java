package org.foi.uzdiz.pmatisic.zadaca_3.factory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Podrucje;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Podrucje.Par;
import org.foi.uzdiz.pmatisic.zadaca_3.pomagala.Greske;

public class PodrucjeDatoteka implements Datoteka<Podrucje> {

  private String putanjaDatoteke;
  private List<Podrucje> podrucja = new ArrayList<>();

  @Override
  public void postaviPutanju(String putanja) {
    this.putanjaDatoteke = putanja;
  }

  @Override
  public List<Podrucje> dohvatiPodatke() {
    return new ArrayList<>(podrucja);
  }

  @Override
  public void citajPodatke() {
    try {
      Path staza = Path.of(putanjaDatoteke);

      if (!Files.exists(staza) || Files.isDirectory(staza) || !Files.isReadable(staza)) {
        throw new IOException("Datoteka '" + putanjaDatoteke + "' ne postoji ili nije čitljiva!");
      }

      List<String> linije = Files.readAllLines(staza, Charset.forName("UTF-8"));
      podrucja.clear();

      if (!linije.isEmpty() && linije.get(0).startsWith("id")) {
        linije.remove(0);
      }

      for (String linija : linije) {
        if (linija.trim().isEmpty()) {
          continue;
        }

        String[] dijelovi = linija.split(";");

        if (dijelovi.length != 2) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija, "Pogrešan broj atributa");
          continue;
        }

        try {
          int id = Integer.parseInt(dijelovi[0].trim());
          List<Par<Integer, String>> gradUlicaParovi = new ArrayList<>();
          for (String par : dijelovi[1].split(",")) {
            String[] gradUlica = par.split(":");
            int gradId = Integer.parseInt(gradUlica[0].trim());
            String ulica = gradUlica[1].trim();
            gradUlicaParovi.add(new Par<>(gradId, ulica));
          }

          Podrucje podrucje = new Podrucje(id, gradUlicaParovi);
          podrucja.add(podrucje);
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
