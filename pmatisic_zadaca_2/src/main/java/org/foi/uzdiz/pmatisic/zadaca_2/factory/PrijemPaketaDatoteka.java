package org.foi.uzdiz.pmatisic.zadaca_2.factory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.pmatisic.zadaca_2.model.PrijemPaketa;
import org.foi.uzdiz.pmatisic.zadaca_2.model.UslugaDostave;
import org.foi.uzdiz.pmatisic.zadaca_2.pomagala.Greske;

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

      for (String linija : linije) {
        String[] dijelovi = linija.split(";");

        if (dijelovi.length != 11) {
          Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija, "Pogrešan broj atributa");
          continue;
        }

        try {
          if (!"A".equalsIgnoreCase(dijelovi[4]) && !"B".equalsIgnoreCase(dijelovi[4])
              && !"C".equalsIgnoreCase(dijelovi[4]) && !"D".equalsIgnoreCase(dijelovi[4])
              && !"E".equalsIgnoreCase(dijelovi[4]) && !"X".equalsIgnoreCase(dijelovi[4])) {
            Greske.logirajGresku(Greske.getRedniBrojGreske() + 1, linija,
                "Nepoznata vrsta paketa!");
            continue;
          }

          if (("A".equalsIgnoreCase(dijelovi[4]) || "B".equalsIgnoreCase(dijelovi[4])
              || "C".equalsIgnoreCase(dijelovi[4]) || "D".equalsIgnoreCase(dijelovi[4])
              || "E".equalsIgnoreCase(dijelovi[4]))
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

          PrijemPaketa prijemPaketa = new PrijemPaketa(dijelovi[0],
              PrijemPaketa.konvertirajVrijeme(dijelovi[1]), dijelovi[2], dijelovi[3], dijelovi[4],
              Double.parseDouble(dijelovi[5].replace(',', '.')),
              Double.parseDouble(dijelovi[6].replace(',', '.')),
              Double.parseDouble(dijelovi[7].replace(',', '.')),
              Double.parseDouble(dijelovi[8].replace(',', '.')), UslugaDostave.valueOf(dijelovi[9]),
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
