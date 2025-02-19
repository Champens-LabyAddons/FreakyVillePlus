package dk.fvtrademarket.fvplus.core.listeners.internal;

import dk.fvtrademarket.fvplus.api.FreakyVillePlus;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameShutdownEvent;

public class GameShutdownListener {

  @Subscribe
  public void onGameShutDown(GameShutdownEvent event) {
    FreakyVillePlus.getReferences().messageService().shutdown();
  }
}
