package dk.fvtrademarket.fvplus.api.activatable.guardvault;

import dk.fvtrademarket.fvplus.api.activatable.Activatable;
import dk.fvtrademarket.fvplus.api.enums.PrisonSector;

public interface GuardVault extends Activatable {

  PrisonSector getPrisonSector();

  PrisonSector[] getVisiblePrisonSectors();
}
