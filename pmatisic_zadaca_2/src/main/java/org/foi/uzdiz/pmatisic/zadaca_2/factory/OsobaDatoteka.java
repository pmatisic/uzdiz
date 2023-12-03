package org.foi.uzdiz.pmatisic.zadaca_2.factory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.pmatisic.zadaca_2.model.Osoba;
import org.foi.uzdiz.pmatisic.zadaca_2.pomagala.Greske;

public class OsobaDatoteka implements Datoteka<Osoba> {

  private String putanjaDatoteke;
  private List<Osoba> osobe = new ArrayList<>();

  @Override
  public void postaviPutanju(String putanja) {
    this.putanjaDatoteke = putanja;
  }

  @Override
  public List<Osoba> dohvatiPodatke() {
    return new ArrayList<>(osobe);
  }

  @Override
  public void citajPodatke() {
    try {
      Path staza = Path.of(putanjaDatoteke);

      if (!Files.exists(staza) || Files.isDirectory(staza) || !Files.isReadable(staza)) {
        throw new IOException("Datoteka '" + putanjaDatoteke + "' ne postoji ili nije čitljiva!");
      }

      List<String> linije = Files.readAllLines(staza, Charset.forName("UTF-8"));
      osobe.clear();

      if (!linije.isEmpty() && linije.get(0).startsWith("osoba")) {
        linije.remove(0);
      }

      for (String linija : linije) {
        String[] dijelovi = linija.split(";");

        if (dijelovi.length != 4) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija, "Pogrešan broj atributa");
          continue;
        }

        try {
          String ime = dijelovi[0].trim();
          int gradId = Integer.parseInt(dijelovi[1].trim());
          int ulicaId = Integer.parseInt(dijelovi[2].trim());
          int kucniBroj = Integer.parseInt(dijelovi[3].trim());

          Osoba osoba = new Osoba(ime, gradId, ulicaId, kucniBroj);
          osobe.add(osoba);
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
