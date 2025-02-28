package dk.fvtrademarket.fvplus.api.event.prison.guardvault;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.event.prison.SectoredEvent;
import net.labymod.api.event.Event;

public abstract class GuardVaultEvent extends SectoredEvent implements Event {
  private final String robber;

  public GuardVaultEvent(PrisonSector sector, String robber) {
    super(sector);
    this.robber = robber;
  }

  public String getRobber() {
    return robber;
  }
}
