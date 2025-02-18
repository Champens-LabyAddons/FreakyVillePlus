package dk.fvtrademarket.fvplus.api.event.prison.guardvault;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;

public class GuardVaultFinishEvent extends GuardVaultEvent {

  private final boolean success;

  public GuardVaultFinishEvent(PrisonSector sector, String robber, boolean success) {
    super(sector, robber);
    this.success = success;
  }

  public boolean wasSuccessFull() {
    return success;
  }
}
