package dk.fvtrademarket.fvplus.api.housing;

import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.api.misc.Location;
import net.labymod.api.util.Pair;

public interface LivingArea {

  /**
   * Henter den server, som området er associeret med.
   *
   * @return Serveren, som området er associeret med
   */
  FreakyVilleServer getAssociatedServer();

  /**
   * Henter områdepræfikset.
   * <p>
   * Et præfiks er et kort string, der klassificerer områdets bolig type, eksempelvis "cyt" for "C Youtube"
   * og "b" for "B Blokken".
   * @return Områdepræfikset
   */
  String getCode();

  /**
   * Henter en range af identifikationsnumre, der kan findes i området (inklusiv).
   * @return Range af identifikationsnumre
   */
  Pair<Integer, Integer> getIdRange();

  /**
   * Henter en beskrivelse af området.
   * @return Beskrivelse af området
   */
  String getDescription();

  /**
   * Beskriver minecraft lokationen af området.
   * @return Minecraft lokationen af området
   */
  Location getLocation();
}
