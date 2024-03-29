package org.foi.uzdiz.pmatisic.zadaca_3.factory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.foi.uzdiz.pmatisic.zadaca_3.model.PrijemPaketa;
import org.foi.uzdiz.pmatisic.zadaca_3.model.UslugaDostave;
import org.foi.uzdiz.pmatisic.zadaca_3.pomagala.Greske;

public class PrijemPaketaDatoteka implements Datoteka<PrijemPaketa> {

  private String putanjaDatoteke;
  private List<PrijemPaketa> paketi = new ArrayList<>();

  @Override
  public void postaviPutanju(String putanja) {
    this.putanjaDatoteke = putanja;
  }

  @Override
  public List<PrijemPaketa> dohvatiPodatke() {
    return new ArrayList<>(paketi);
  }

  @Override
  public void citajPodatke() {
    try {
      Path staza = Path.of(putanjaDatoteke);

      if (!Files.exists(staza) || Files.isDirectory(staza) || !Files.isReadable(staza)) {
        throw new IOException("Datoteka '" + putanjaDatoteke + "' ne postoji ili nije čitljiva!");
      }

      List<String> linije = Files.readAllLines(staza, Charset.forName("UTF-8"));
      paketi.clear();

      if (!linije.isEmpty() && linije.get(0).startsWith("Oznaka")) {
        linije.remove(0);
      }

      Set<String> dozvoljeneUsluge = new HashSet<>(Arrays.asList("S", "H", "P", "R"));

      for (String linija : linije) {
        if (linija.trim().isEmpty()) {
          continue;
        }

        String[] dijelovi =
            Arrays.stream(linija.split(";")).map(String::trim).toArray(String[]::new);

        if (dijelovi.length != 11) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija, "Pogrešan broj atributa");
          continue;
        }

        try {
          if (!Arrays.asList("A", "B", "C", "D", "E", "X").contains(dijelovi[4].toUpperCase())) {
            Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija,
                "Nepoznata vrsta paketa: " + dijelovi[4]);
            continue;
          }

          if (Arrays.asList("A", "B", "C", "D", "E").contains(dijelovi[4].toUpperCase())
              && (Double.parseDouble(dijelovi[5].replace(',', '.')) != 0
                  || Double.parseDouble(dijelovi[6].replace(',', '.')) != 0
                  || Double.parseDouble(dijelovi[7].replace(',', '.')) != 0)) {
            Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija,
                "Tipski paket mora imati visinu, širinu i dužinu postavljenu na 0!");
            continue;
          }

          if (!"P".equalsIgnoreCase(dijelovi[9])
              && Double.parseDouble(dijelovi[10].replace(',', '.')) != 0) {
            Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija,
                "Ako usluga nije plaćanje pouzećem, iznos pouzeća mora biti 0!");
            continue;
          }

          String uslugaString = dijelovi[9];
          if (!dozvoljeneUsluge.contains(uslugaString)) {
            Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija,
                "Nevažeća usluga dostave: " + uslugaString);
            continue;
          }
          UslugaDostave usluga = UslugaDostave.valueOf(uslugaString);

          PrijemPaketa prijemPaketa = new PrijemPaketa(dijelovi[0],
              PrijemPaketa.konvertirajVrijeme(dijelovi[1]), dijelovi[2], dijelovi[3], dijelovi[4],
              Double.parseDouble(dijelovi[5].replace(',', '.')),
              Double.parseDouble(dijelovi[6].replace(',', '.')),
              Double.parseDouble(dijelovi[7].replace(',', '.')),
              Double.parseDouble(dijelovi[8].replace(',', '.')), usluga,
              Double.parseDouble(dijelovi[10].replace(',', '.')));
          paketi.add(prijemPaketa);
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
