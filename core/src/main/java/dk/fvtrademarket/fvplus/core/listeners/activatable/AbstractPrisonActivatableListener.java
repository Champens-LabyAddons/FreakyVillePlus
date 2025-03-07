package dk.fvtrademarket.fvplus.core.listeners.activatable;

import dk.fvtrademarket.fvplus.api.activatable.prison.PrisonActivatable;
import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.event.prison.SectoredEvent;
import dk.fvtrademarket.fvplus.core.activatable.DefaultActivatableService;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import net.labymod.api.LabyAPI;
import java.util.Optional;

public abstract class AbstractPrisonActivatableListener<T extends PrisonActivatable> extends AbstractActivatableListener<T> {

  public AbstractPrisonActivatableListener(ClientInfo clientInfo, LabyAPI labyAPI, DefaultActivatableService activatableService, Class<T> activatableClass) {
    super(clientInfo, labyAPI, activatableService, activatableClass);
  }

  protected T parsePrisonActivatable(SectoredEvent event) {
    Optional<T> prisonActivatable = getPrisonActivatableBySector(event.getSector());
    if (prisonActivatable.isEmpty()) {
      return null;
    }
    if (this.clientInfo.getCurrentServer() != prisonActivatable.get().getAssociatedServer()) {
      return null;
    }
    if (this.clientInfo.getCurrentServer() == FreakyVilleServer.PRISON && clientInfo.getPrisonSector().isEmpty()) {
      return null;
    }
    if (event.getSector() == clientInfo.getPrisonSector().get()) {
      return prisonActivatable.get();
    }
    return null;
  }

  protected Optional<T> getPrisonActivatableBySector(PrisonSector sector) {
    for (T prisonActivatable : activatableService.getActivatables(activatableClass)) {
      if (prisonActivatable.getPrisonSector() == sector) {
        return Optional.of(prisonActivatable);
      }
    }
    return Optional.empty();
  }


}
