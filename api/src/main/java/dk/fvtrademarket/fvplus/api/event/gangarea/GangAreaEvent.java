package dk.fvtrademarket.fvplus.api.event.gangarea;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import net.labymod.api.event.Event;

public abstract class GangAreaEvent implements Event {
  private final PrisonSector sector;
  private final String takerName;

  public GangAreaEvent(PrisonSector sector, String takerName) {
    this.sector = sector;
    this.takerName = takerName;
  }

  public PrisonSector getSector() {
    return sector;
  }

  public String getTakerName() {
    return takerName;
  }
}
