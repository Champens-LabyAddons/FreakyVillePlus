package dk.fvtrademarket.fvplus.core.internal;

import dk.fvtrademarket.fvplus.api.activatable.misc.DefaultGangArea;
import dk.fvtrademarket.fvplus.api.activatable.misc.GangArea;
import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.service.activatable.ActivatableService;
import dk.fvtrademarket.fvplus.core.util.DataFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GangAreaActivatableService implements ActivatableService<GangArea> {

  @Override
  public void registerActivatable(GangArea activatable) {

  }

  @Override
  public void unregisterActivatable(GangArea activatable) {

  }

  @Override
  public Collection<GangArea> getAllActivatables() {
    return List.of();
  }

  @Override
  public void initialize() {
    ArrayList<String[]> gangAreaData = DataFormatter.csv();
    for (String[] line : gangAreaData) {
      GangArea gangArea = new DefaultGangArea(
          // Den Sector som Bandeområdet er placeret i
          PrisonSector.fromString(line[0]),
          Integer.parseInt(line[1]),
          Integer.parseInt(line[2]),
          Integer.parseInt(line[3]),
          Integer.parseInt(line[4])
      );
      this.registerActivatable(gangArea);
    }
  }

  @Override
  public void shutdown() {

  }
}
