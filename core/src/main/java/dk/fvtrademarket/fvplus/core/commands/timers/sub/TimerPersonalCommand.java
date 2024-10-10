package dk.fvtrademarket.fvplus.core.commands.timers.sub;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.internal.PoiList;
import dk.fvtrademarket.fvplus.core.poi.POI;
import java.util.ArrayList;
import java.util.List;

public class TimerPersonalCommand extends TimerSubCommand {
  public TimerPersonalCommand(String serverAndCategoryKey, String parentPrefix, ClientInfo clientInfo, PoiList poiList) {
      super("personal", serverAndCategoryKey, parentPrefix, clientInfo, poiList,  "personlig", "p");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!clientInfo.isOnFreakyVille()) return false;
    List<POI> poisToShow = new ArrayList<>();
    for (POI poi : poiList.getPois()) {
      if (poi.getPersonalTimeLeft() == null) {
        continue;
      }
      poisToShow.add(poi);
    }
    return howToExecute(arguments, poisToShow);
  }

  @Override
  protected final void displayPOI(POI poi) {
    Component line = Component.text(" - ", NamedTextColor.GRAY);
    Component poiName = Component.text(poi.getDisplayName(), NamedTextColor.YELLOW);
    Component separator = Component.text(": ", NamedTextColor.GRAY);
    Component timeLeft = Component.text(poi.getPersonalTimeLeft(), NamedTextColor.WHITE);
    displayMessage(line.append(poiName).append(separator).append(timeLeft));
  }
}
