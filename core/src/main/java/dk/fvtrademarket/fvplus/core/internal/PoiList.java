package dk.fvtrademarket.fvplus.core.internal;

import dk.fvtrademarket.fvplus.core.util.DataFormatter;
import net.labymod.api.util.Pair;
import net.labymod.api.util.logging.Logging;
import dk.fvtrademarket.fvplus.core.poi.POI;
import dk.fvtrademarket.fvplus.core.poi.PrisonPOI;
import dk.fvtrademarket.fvplus.core.util.Prison;
import dk.fvtrademarket.fvplus.core.util.Resources;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PoiList implements Manager {
  private final List<POI> pois;
  private final Set<POI> limboingPois;

  public PoiList() {
    this.pois = new ArrayList<>();
    this.limboingPois = new HashSet<>();
  }

  @Override
  public void init() throws IOException {
    ArrayList<String[]> csv = DataFormatter.csv(Resources.POI_LIST_URL);
    for (String[] strings : csv) {
      this.pois.add(new PrisonPOI(
          strings[0],
          strings[1],
          Pair.of(strings[2], strings[3]),
          Pair.of(strings[4], strings[5]),
          strings[6],
          Integer.parseInt(strings[7]),
          Integer.parseInt(strings[8]),
          Integer.parseInt(strings[9]),
          Integer.parseInt(strings[10]),
          Arrays.stream(strings[11].split(";")).map(Prison::valueOf).toArray(Prison[]::new)
      ));
      Logging.getLogger().info("POI: " + strings[0]);
    }
  }

  public List<POI> getPois() {
    return List.copyOf(this.pois);
  }

  public Set<POI> getLimboingPois() {
    return this.limboingPois;
  }
}
