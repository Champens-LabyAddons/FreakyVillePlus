package dk.fvtrademarket.fvplus.api.activatable.prison;

import dk.fvtrademarket.fvplus.api.activatable.Activatable;
import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.api.enums.PrisonSector;

public interface PrisonActivatable extends Activatable {

  @Override
  default FreakyVilleServer getAssociatedServer() {
    return FreakyVilleServer.PRISON;
  }

  PrisonSector getPrisonSector();
}
