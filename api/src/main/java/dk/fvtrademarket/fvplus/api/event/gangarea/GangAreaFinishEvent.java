package dk.fvtrademarket.fvplus.api.event.gangarea;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;

public class GangAreaFinishEvent extends GangAreaEvent {

  private final boolean success;

  public GangAreaFinishEvent(PrisonSector sector, String takerName, boolean success) {
    super(sector, takerName);
    this.success = success;
  }

  public boolean wasSuccessFull() {
    return this.success;
  }
}
