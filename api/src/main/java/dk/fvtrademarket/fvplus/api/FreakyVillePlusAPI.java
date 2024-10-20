package dk.fvtrademarket.fvplus.api;

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
  ActivatableService getActivatableService();

  MessageService getMessageService();


}
