package dk.fvtrademarket.fvplus.api.event.guardvault;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import net.labymod.api.event.Event;

public class GuardVaultUpdateEvent extends GuardVaultEvent {
  private final PrisonSector prisonSector;
  private final String timeLeftStr;

  public GuardVaultUpdateEvent(PrisonSector prisonSector, String timeLeftStr) {
    super(prisonSector, "");
    this.prisonSector = prisonSector;
    this.timeLeftStr = timeLeftStr;
  }

  public PrisonSector getPrisonSector() {
    return prisonSector;
  }

  public String getTimeLeftStr() {
    return timeLeftStr;
  }
}
