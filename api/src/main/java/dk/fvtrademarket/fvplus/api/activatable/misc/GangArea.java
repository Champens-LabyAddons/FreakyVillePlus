package dk.fvtrademarket.fvplus.api.activatable.misc;

import dk.fvtrademarket.fvplus.api.activatable.Activatable;
import dk.fvtrademarket.fvplus.api.enums.PrisonSector;

public interface GangArea extends Activatable {
  PrisonSector getPrisonSector();
}
