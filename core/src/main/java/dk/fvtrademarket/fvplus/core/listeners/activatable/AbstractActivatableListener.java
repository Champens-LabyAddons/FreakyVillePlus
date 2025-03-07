package dk.fvtrademarket.fvplus.core.listeners.activatable;

import dk.fvtrademarket.fvplus.api.activatable.Activatable;
import dk.fvtrademarket.fvplus.core.activatable.DefaultActivatableService;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import net.labymod.api.LabyAPI;

public abstract class AbstractActivatableListener<T extends Activatable> {
  protected final ClientInfo clientInfo;
  protected final LabyAPI labyAPI;
  protected final DefaultActivatableService activatableService;
  protected final Class<T> activatableClass;

  public AbstractActivatableListener(ClientInfo clientInfo, LabyAPI labyAPI, DefaultActivatableService activatableService, Class<T> activatableClass) {
    this.clientInfo = clientInfo;
    this.labyAPI = labyAPI;
    this.activatableService = activatableService;
    this.activatableClass = activatableClass;
  }

  protected boolean wasPersonal(String initiatorName) {
    String playerName = this.labyAPI.getName();
    return playerName.equals(initiatorName);
  }
}
