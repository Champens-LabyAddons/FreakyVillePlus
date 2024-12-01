package dk.fvtrademarket.fvplus.api.activatable;

import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.api.misc.Displayable;

/**
 * Et aktiverbart objektiv
 *
 * @author Champen_V1ldtand
 * @since 2.0.0
 */
public interface Activatable extends Displayable {
  FreakyVilleServer getAssociatedServer();

  int getExpectedActivationTime();

  int getCooldownUponFailure();

  int getPersonalCooldown();

  int getSharedCooldown();
}
