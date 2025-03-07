package dk.fvtrademarket.fvplus.api.event.prison.gangarea;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.event.prison.SectoredEvent;
import net.labymod.api.event.Event;

public abstract class GangAreaEvent extends SectoredEvent implements Event {
  private final String takerName;

  public GangAreaEvent(PrisonSector sector, String takerName) {
    super(sector);
    this.takerName = takerName;
  }

  public String getTakerName() {
    return takerName;
  }
}
