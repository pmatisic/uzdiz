package org.foi.uzdiz.pmatisic.zadaca_3.strategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.foi.uzdiz.pmatisic.zadaca_3.builder.Paket;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Osoba;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Ulica;
import org.foi.uzdiz.pmatisic.zadaca_3.model.Vozilo;
import org.foi.uzdiz.pmatisic.zadaca_3.pomagala.UredZaDostavu;
import org.foi.uzdiz.pmatisic.zadaca_3.singleton.Tvrtka;

public class Udaljenost implements StrategijaIsporuke {

  private UredZaDostavu uredZaDostavu;
  private String trenutniGPS = null;

  public Udaljenost(UredZaDostavu uredZaDostavu) {
    this.uredZaDostavu = uredZaDostavu;
  }

  @Override
  public void isporuciPakete(Vozilo vozilo, String gps) {
    List<Paket> paketiZaIsporuku =
        uredZaDostavu.ukrcaniPaketi.getOrDefault(vozilo, new ArrayList<>());
    if (paketiZaIsporuku.isEmpty())
      return;

    LocalDateTime vrijemeSljedeceDostave = vozilo.getVrijemeSljedeceDostave();
    vrijemeSljedeceDostave =
        uredZaDostavu.trenutnoVirtualnoVrijeme.plusMinutes(uredZaDostavu.vrijemeIsporuke);

    System.out.println(
        "\nU " + uredZaDostavu.trenutnoVirtualnoVrijeme.format(uredZaDostavu.dateTimeFormatter)
            + " dostava je pokrenuta za vozilo " + vozilo.getRegistracija() + ".\n");

    if (trenutniGPS == null) {
      trenutniGPS = gps;
    }

    while (!paketiZaIsporuku.isEmpty()) {
      Paket najbliziPaket = null;
      double najmanjaUdaljenost = Double.MAX_VALUE;
      String gpsNajblizegPaketa = "";

      for (Paket paket : paketiZaIsporuku) {
        String status = UredZaDostavu.statusPaketa.getOrDefault(paket.getOznaka(), "");
        if (Boolean.TRUE.equals(uredZaDostavu.isporuceniPaketi.get(paket.getOznaka()))
            || !"Ukrcano".equals(status)) {
          continue;
        }

        Ulica ulica = dohvatiUlicuZaPaket(paket);
        if (ulica == null)
          continue;

        Osoba primatelj = dohvatiPrimatelja(paket.getPrimatelj());
        if (primatelj == null)
          continue;

        String gpsPaketa = izracunajGPSAdresePaketa(ulica, primatelj.getKucniBroj());
        double udaljenost = izracunajUdaljenost(trenutniGPS, gpsPaketa);
        vozilo.dodajOdvozeneKilometre(udaljenost);

        if (udaljenost < najmanjaUdaljenost) {
          najmanjaUdaljenost = udaljenost;
          najbliziPaket = paket;
          gpsNajblizegPaketa = gpsPaketa;
        }
      }

      if (najbliziPaket != null) {
        paketiZaIsporuku.remove(najbliziPaket);
        trenutniGPS = gpsNajblizegPaketa;

        System.out.printf("U %s paket %s isporučen primatelju %s pomoću vozila %s.%n",
            vrijemeSljedeceDostave.format(uredZaDostavu.dateTimeFormatter),
            najbliziPaket.getOznaka(), najbliziPaket.getPrimatelj(), vozilo.getRegistracija());

        UredZaDostavu.vrijemePreuzimanjaPaketa.put(najbliziPaket.getOznaka(),
            vrijemeSljedeceDostave);
        UredZaDostavu.statusPaketa.put(najbliziPaket.getOznaka(), "Dostavljeno");
        uredZaDostavu.isporuceniPaketi.put(najbliziPaket.getOznaka(), true);
        vozilo.povecajBrojIsporucenihPaketa();
        vozilo.setSlobodno(true);
        vrijemeSljedeceDostave = vrijemeSljedeceDostave.plusMinutes(uredZaDostavu.vrijemeIsporuke);
        vozilo.setVrijemeSljedeceDostave(vrijemeSljedeceDostave);
      }
    }

    if (paketiZaIsporuku.isEmpty()) {
      uredZaDostavu.ukrcaniPaketi.remove(vozilo);
    }
  }

  private String izracunajGPSAdresePaketa(Ulica ulica, int kucniBroj) {
    double postotak = Math.min((double) kucniBroj / ulica.getNajveciKucniBroj(), 1.0);
    double lat = interpoliraj(ulica.getGpsLat1(), ulica.getGpsLat2(), postotak);
    double lon = interpoliraj(ulica.getGpsLon1(), ulica.getGpsLon2(), postotak);
    return lat + "," + lon;
  }

  private double interpoliraj(double pocetak, double kraj, double postotak) {
    return pocetak + (kraj - pocetak) * postotak;
  }

  private double izracunajUdaljenost(String startGPS, String ciljniGPS) {
    String[] start = startGPS.split(",");
    String[] cilj = ciljniGPS.split(",");
    double lat1 = Double.parseDouble(start[0]);
    double lon1 = Double.parseDouble(start[1]);
    double lat2 = Double.parseDouble(cilj[0]);
    double lon2 = Double.parseDouble(cilj[1]);
    return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2));
  }

  private Ulica dohvatiUlicuZaPaket(Paket paket) {
    String imePrimatelja = paket.getPrimatelj();
    Osoba primatelj = null;
    for (Osoba osoba : Tvrtka.getInstance().osobe) {
      if (osoba.getIme().equals(imePrimatelja)) {
        primatelj = osoba;
        break;
      }
    }

    if (primatelj == null) {
      return null;
    }

    int ulicaId = primatelj.getUlicaId();
    for (Ulica ulica : Tvrtka.getInstance().ulice) {
      if (ulica.getId() == ulicaId) {
        return ulica;
      }
    }

    return null;
  }

  private Osoba dohvatiPrimatelja(String imePrimatelja) {
    for (Osoba osoba : Tvrtka.getInstance().osobe) {
      if (osoba.getIme().equals(imePrimatelja)) {
        return osoba;
      }
    }
    return null;
  }

}
