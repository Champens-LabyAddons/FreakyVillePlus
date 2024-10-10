package dk.fvtrademarket.fvplus.core.commands.timers.sub;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.util.I18n;
import dk.fvtrademarket.fvplus.core.commands.FreakyVillePlusSubCommand;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.internal.PoiList;
import dk.fvtrademarket.fvplus.core.poi.POI;
import dk.fvtrademarket.fvplus.core.util.FreakyVilleServer;
import java.util.ArrayList;
import java.util.List;

public abstract class TimerSubCommand extends FreakyVillePlusSubCommand {
  protected final ClientInfo clientInfo;
  protected final PoiList poiList;

  public TimerSubCommand(String prefix, String serverAndCategoryKey, String parentPrefix, ClientInfo clientInfo, PoiList poiList, String... aliases) {
    super(prefix, parentPrefix, serverAndCategoryKey, aliases);
    this.clientInfo = clientInfo;
    this.poiList = poiList;
  }

  public abstract boolean execute(String prefix, String[] arguments);

  protected boolean howToExecute(String[] arguments, List<POI> poisOnCooldown) {
    if (arguments.length < 1) {
      defaultExecution(poisOnCooldown);
      return true;
    }
    FreakyVilleServer specifiedServer;
    try {
      specifiedServer = getServerFromString(arguments[0]);
    } catch (IllegalArgumentException e) {
      displayTranslatable("invalidServer", NamedTextColor.RED);
      return true;
    }
    serverSpecificExecution(poisOnCooldown, specifiedServer);
    return true;
  }

  protected void defaultExecution(List<POI> poisToShow) {
    String header = " -= [ " + I18n.translate(getTranslationKey("header")) + " ] =-";
    displayMessage(Component.text(header, NamedTextColor.GOLD));
    if (poisToShow.isEmpty()) {
      displayTranslatable("noPoisToShow", NamedTextColor.GREEN);
      return;
    }
    for (POI poi : poisToShow) {
      displayPOI(poi);
    }
  }

  protected void serverSpecificExecution(List<POI> poisToShow, FreakyVilleServer specifiedServer) {
    List<POI> poisOnSpecifiedServerToShow = new ArrayList<>();
    for (POI poi : poisToShow) {
      if (poi.getAssosciatedServer() == specifiedServer) {
        poisOnSpecifiedServerToShow.add(poi);
      }
    }
    String headerVal = I18n.translate(getTranslationKey("header"));
    String header = String.format(" -= [ %s - %s ] =-", headerVal, specifiedServer.getTranslatedName());
    displayMessage(Component.text(header, NamedTextColor.GOLD));
    if (poisOnSpecifiedServerToShow.isEmpty()) {
      displayTranslatable("noPoisToShow", NamedTextColor.GREEN);
      return;
    }
    for (POI poi : poisOnSpecifiedServerToShow) {
      displayPOI(poi);
    }
  }

  protected abstract void displayPOI(POI poi);

  protected FreakyVilleServer getServerFromString(String server) {
    return switch (server.toLowerCase()) {
      case "nprison", "prison", "np" -> FreakyVilleServer.PRISON;
      case "kitpvp", "kit" -> FreakyVilleServer.KIT_PVP;
      case "skyblock", "sb" -> FreakyVilleServer.SKY_BLOCK;
      case "friheden", "fri" -> FreakyVilleServer.THE_CITY;
      default -> throw new IllegalArgumentException("Invalid server");
    };
  }
}
