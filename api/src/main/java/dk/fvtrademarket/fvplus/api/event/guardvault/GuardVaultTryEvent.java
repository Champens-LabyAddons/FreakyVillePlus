package dk.fvtrademarket.fvplus.api.event.guardvault;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import java.time.Instant;

public class GuardVaultTryEvent extends GuardVaultEvent {

  private final Instant time = Instant.now();

  public GuardVaultTryEvent(PrisonSector sector, String robber) {
    super(sector, robber);
  }

  public Instant getTime() {
    return time;
  }
}
