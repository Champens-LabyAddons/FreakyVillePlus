package dk.fvtrademarket.fvplus.core.housing;

import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.api.housing.LivingArea;
import dk.fvtrademarket.fvplus.api.misc.Location;
import net.labymod.api.util.Pair;

public class DefaultLivingArea implements LivingArea {
  private final FreakyVilleServer server;
  private final String code;
  private final Pair<Integer, Integer> idRange;
  private final String description;
  private final Location location;

  public DefaultLivingArea(
      FreakyVilleServer server,
      String code,
      Pair<Integer, Integer> idRange,
      String description,
      int locationX,
      int locationY,
      int locationZ
  ) {
    this.server = server;
    this.code = code;
    this.idRange = idRange;
    this.description = description;
    this.location = new Location(locationX, locationY, locationZ);
  }

  @Override
  public FreakyVilleServer getAssociatedServer() {
    return this.server;
  }

  @Override
  public String getCode() {
    return this.code;
  }

  @Override
  public Pair<Integer, Integer> getIdRange() {
    return this.idRange;
  }

  @Override
  public String getDescription() {
    return this.description;
  }

  @Override
  public Location getLocation() {
    return this.location;
  }
}
