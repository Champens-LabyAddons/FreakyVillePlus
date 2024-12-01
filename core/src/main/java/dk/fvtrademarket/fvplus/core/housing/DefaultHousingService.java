package dk.fvtrademarket.fvplus.core.housing;

import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.api.housing.LivingArea;
import dk.fvtrademarket.fvplus.api.service.housing.HousingService;
import dk.fvtrademarket.fvplus.core.util.DataFormatter;
import dk.fvtrademarket.fvplus.core.util.Resource;
import net.labymod.api.models.Implements;
import net.labymod.api.util.Pair;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Singleton
@Implements(HousingService.class)
public class DefaultHousingService implements HousingService {
  private final Set<LivingArea> livingQuarters;

  private boolean initialized;

  public DefaultHousingService() {
    this.livingQuarters = new HashSet<>();

    this.initialized = false;
  }

  @Override
  public void registerLivingQuarters(LivingArea housing) {
    this.livingQuarters.add(housing);
  }

  @Override
  public void unregisterLivingQuarters(LivingArea housing) {
    this.livingQuarters.remove(housing);
  }

  @Override
  public Collection<LivingArea> getAllLivingQuarters() {
    return this.livingQuarters;
  }

  @Override
  public void initialize() {
    if (initialized) {
      throw new IllegalStateException("Service already initialized");
    }
    ArrayList<String[]> livingAreas = DataFormatter.csv(Resource.CELL_LIST.toString(), true);
    livingAreas.addAll(DataFormatter.csv(Resource.HOMES_LIST.toString(), true));

    for (String[] line : livingAreas) {
      this.livingQuarters.add(new DefaultLivingArea(
              FreakyVilleServer.valueOf(line[0]),
              line[1],
              Pair.of(Integer.parseInt(line[2]), Integer.parseInt(line[3])),
              line[4],
              Integer.parseInt(line[5]),
              Integer.parseInt(line[6]),
              Integer.parseInt(line[7])
      ));
    }

    initialized = true;
  }

  @Override
  public void shutdown() {

  }
}
