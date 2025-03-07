package dk.fvtrademarket.fvplus.api.event.prison;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;

public abstract class SectoredEvent {
  protected final PrisonSector sector;

  public SectoredEvent(PrisonSector sector) {
    this.sector = sector;
  }

  public PrisonSector getSector() {
    return sector;
  }

}
