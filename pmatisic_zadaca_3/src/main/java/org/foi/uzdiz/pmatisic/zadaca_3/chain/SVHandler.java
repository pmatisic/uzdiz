package org.foi.uzdiz.pmatisic.zadaca_3.chain;

import org.foi.uzdiz.pmatisic.zadaca_3.model.Vozilo;
import org.foi.uzdiz.pmatisic.zadaca_3.singleton.Tvrtka;
import org.foi.uzdiz.pmatisic.zadaca_3.visitor.IspisVisitor;

public class SVHandler extends Handler {

  @Override
  public boolean handle(String komanda) {
    if (komanda.equals("SV")) {
      System.out.println(
          "+-----------------+--------------------------------+--------------+----------------+--------------+---------------+-------------------+---------------+--------------+------------+");
      System.out.println(
          "| Registracija    | Opis                           | Status       | Odvoženi km    | Hitni paketi | Obični paketi | Isporučeni paketi | Prostor u %   | Težina u %   | Br. vožnji |");
      System.out.println(
          "+-----------------+--------------------------------+--------------+----------------+--------------+---------------+-------------------+---------------+--------------+------------+");

      IspisVisitor visitor = new IspisVisitor();
      for (Vozilo vozilo : Tvrtka.getInstance().uredZaDostavu.vozila) {
        vozilo.accept(visitor);
      }
      System.out.println(
          "+-----------------+--------------------------------+--------------+----------------+--------------+---------------+-------------------+---------------+--------------+------------+");
      return true;
    }
    return handleNext(komanda);
  }

}
