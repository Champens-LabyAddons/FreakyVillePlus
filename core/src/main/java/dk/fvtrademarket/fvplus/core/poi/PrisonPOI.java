package dk.fvtrademarket.fvplus.core.poi;

import net.labymod.api.util.Pair;
import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.core.util.Prison;

/**
 * Repræsenteret et Point of Interest (POI) på NPrison.
 * <p>
 * Denne klasse har funktionalitet specifikt til at hjælpe med at vurdere om en POI håndteres
 * korrekt ud fra den specifikke fængselsblok man befinder sig i på NPrison.
 *
 * @see POI
 * @see Prison
 * @since 1.0.0
 */
public class PrisonPOI extends POI {

  private final Prison[] whereCanItBeUpdated;

  public PrisonPOI(String identifier, String displayName,
      Pair<String, String> activationPair,
      Pair<String, String> confirmationPair, String updateTimerMessage,
      int timedCooldown, int waitTimeUponFailure, int expectedTimeToFinish,
      int personalTimedCooldown, Prison... whereCanItBeUpdated) {
    super(identifier, displayName, activationPair, confirmationPair, updateTimerMessage,
        timedCooldown, waitTimeUponFailure, expectedTimeToFinish, personalTimedCooldown,
        FreakyVilleServer.PRISON);
    this.whereCanItBeUpdated = whereCanItBeUpdated;
  }

  public Prison[] getWhereCanItBeUpdated() {
    return whereCanItBeUpdated;
  }
}
