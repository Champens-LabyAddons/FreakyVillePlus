package dk.fvtrademarket.fvplus.api.activatable.prison;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;

public interface GuardVault extends PrisonActivatable {

  PrisonSector[] getVisiblePrisonSectors();
}
