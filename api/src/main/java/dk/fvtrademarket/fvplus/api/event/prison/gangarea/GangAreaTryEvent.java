package dk.fvtrademarket.fvplus.api.event.prison.gangarea;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import java.time.Instant;

public class GangAreaTryEvent extends GangAreaEvent {

  private final Instant time = Instant.now();

  public GangAreaTryEvent(PrisonSector sector, String takerName) {
    super(sector, takerName);
  }

  public Instant getTime() {
    return time;
  }
}
