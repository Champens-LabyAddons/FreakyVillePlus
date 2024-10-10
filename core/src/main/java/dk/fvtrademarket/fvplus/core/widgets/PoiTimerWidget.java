package dk.fvtrademarket.fvplus.core.widgets;

import net.labymod.api.client.gui.icon.Icon;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.poi.POI;

public class PoiTimerWidget extends TimerWidget {
  private final POI poi;

  public PoiTimerWidget(POI poi, ClientInfo clientInfo, Icon associatedIcon) {
    super(poi.getIdentifier(), clientInfo, associatedIcon);
    this.poi = poi;
  }

  @Override
  protected String getTimeLeft() {
    return this.poi.getTimeLeft(this.clientInfo);
  }

  @Override
  protected String getPOI() {
    return this.poi.getDisplayName();
  }
}
