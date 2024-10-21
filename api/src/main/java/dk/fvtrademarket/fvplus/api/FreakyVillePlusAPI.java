package dk.fvtrademarket.fvplus.api;

import dk.fvtrademarket.fvplus.api.activatable.guardvault.GuardVault;
import dk.fvtrademarket.fvplus.api.activatable.misc.GangArea;
import dk.fvtrademarket.fvplus.api.service.activatable.ActivatableService;
import dk.fvtrademarket.fvplus.api.service.message.MessageService;

/**
 * Brugergrænseflade til FreakyVillePlus.
 * <p>
 * Denne klasse giver adgang til alle FreakyVillePlus-tjenester.
 *
 * @since 2.0.0
 */
public interface FreakyVillePlusAPI {

  /**
   * Henter GuardVault-tjenesten.
   *
   * @return GuardVault-tjenesten
   */
  ActivatableService<GuardVault> getGuardVaultService();

  /**
   * Henter GangArea-tjenesten.
   *
   * @return GangArea-tjenesten
   */
  ActivatableService<GangArea> getGangAreaService();

  /**
   * Henter en anden aktiverbar tjeneste.
   *
   * @return Aktiverbar tjeneste
   */
  ActivatableService<?> getMiscActivatableService();

  /**
   * Henter beskedtjenesten.
   *
   * @return Beskedtjenesten
   */
  MessageService getMessageService();


}
