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
import java.util.Optional;
import java.util.Set;

@Singleton
@Implements(HousingService.class)
public class DefaultHousingService implements HousingService {
  private final Set<LivingArea> livingAreas;

  private boolean initialized;

  public DefaultHousingService() {
    this.livingAreas = new HashSet<>();

    this.initialized = false;
  }

  @Override
  public void registerLivingArea(LivingArea housing) {
    this.livingAreas.add(housing);
  }

  @Override
  public void unregisterLivingArea(LivingArea housing) {
    this.livingAreas.remove(housing);
  }

  @Override
  public Optional<LivingArea> getLivingArea(FreakyVilleServer freakyVilleServer, String areaIdentifier) {
    for (LivingArea livingArea : this.livingAreas) {
      if (livingArea.getAssociatedServer() != freakyVilleServer) {
        continue;
      }
      if (isPartOfArea(livingArea, areaIdentifier)) {
        return Optional.of(livingArea);
      }
    }
    return Optional.empty();
  }

  @Override
  public Collection<LivingArea> getAllLivingAreas() {
    return Set.copyOf(this.livingAreas);
  }

  @Override
  public void initialize() {
    if (initialized) {
      throw new IllegalStateException("Service already initialized");
    }
    ArrayList<String[]> livingAreas = DataFormatter.csv(Resource.CELL_LIST.toString(), true);
    livingAreas.addAll(DataFormatter.csv(Resource.HOMES_LIST.toString(), true));

    for (String[] line : livingAreas) {
      this.livingAreas.add(new DefaultLivingArea(
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

  private boolean isPartOfArea(LivingArea area, String typeAndId) {
    typeAndId = typeAndId.toLowerCase();
    StringBuilder type = new StringBuilder();
    StringBuilder tempId = new StringBuilder();
    for (int i = 0; i < typeAndId.length(); i++) {
      if (isNumeric(typeAndId.charAt(i))) {
        tempId.append(typeAndId.charAt(i));
        continue;
      }
      type.append(typeAndId.charAt(i));
    }
    int finalId = Integer.parseInt(String.valueOf(tempId));
    return type.toString().equals(area.getCode().toLowerCase())
        && isInsideRange(
            finalId,
            area.getIdRange().getFirst(),
            area.getIdRange().getSecond()
    );
  }

  private boolean isInsideRange(int id, int smallestId, int biggestId) {
    return id >= smallestId && id <= biggestId;
  }

  private boolean isNumeric(char c) {
    return switch (c) {
      case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> true;
      default -> false;
    };
  }
}
