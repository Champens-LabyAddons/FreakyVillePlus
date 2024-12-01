package dk.fvtrademarket.fvplus.core.commands.timers.sub;

import dk.fvtrademarket.fvplus.api.activatable.Activatable;
import dk.fvtrademarket.fvplus.api.service.activatable.ActivatableService;
import dk.fvtrademarket.fvplus.core.util.NumberUtil;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.util.I18n;
import dk.fvtrademarket.fvplus.core.commands.FreakyVillePlusSubCommand;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import java.util.ArrayList;
import java.util.List;

public abstract class TimerSubCommand extends FreakyVillePlusSubCommand {
  protected final ClientInfo clientInfo;
  protected final ActivatableService activatableService;

  public TimerSubCommand(String prefix, String serverAndCategoryKey, String parentPrefix,
      ClientInfo clientInfo, ActivatableService activatableService, String... aliases) {
    super(prefix, parentPrefix, serverAndCategoryKey, aliases);
    this.clientInfo = clientInfo;
    this.activatableService = activatableService;
  }

  public abstract boolean execute(String prefix, String[] arguments);

  protected boolean howToExecute(String[] arguments, List<Activatable> activatablesOnCooldown) {
    if (arguments.length < 1) {
      defaultExecution(activatablesOnCooldown);
      return true;
    }
    FreakyVilleServer specifiedServer;
    try {
      specifiedServer = getServerFromString(arguments[0]);
    } catch (IllegalArgumentException e) {
      displayTranslatable("invalidServer", NamedTextColor.RED);
      return true;
    }
    serverSpecificExecution(activatablesOnCooldown, specifiedServer);
    return true;
  }

  protected void defaultExecution(List<Activatable> activatablesToShow) {
    String header = " -= [ " + I18n.translate(getTranslationKey("header")) + " ] =-";
    displayMessage(Component.text(header, NamedTextColor.GOLD));
    if (activatablesToShow.isEmpty()) {
      displayTranslatable("noPoisToShow", NamedTextColor.GREEN);
      return;
    }
    for (Activatable activatable : activatablesToShow) {
      displayActivatable(activatable);
    }
  }

  protected void serverSpecificExecution(List<Activatable> activatablesToShow, FreakyVilleServer specifiedServer) {
    List<Activatable> activatablesOnSpecifiedServerToShow = new ArrayList<>();
    for (Activatable activatable : activatablesToShow) {
      if (activatable.getAssociatedServer() == specifiedServer) {
        activatablesOnSpecifiedServerToShow.add(activatable);
      }
    }
    String headerVal = I18n.translate(getTranslationKey("header"));
    String header = String.format(" -= [ %s - %s ] =-", headerVal, specifiedServer.getTranslatedName());
    displayMessage(Component.text(header, NamedTextColor.GOLD));
    if (activatablesOnSpecifiedServerToShow.isEmpty()) {
      displayTranslatable("noPoisToShow", NamedTextColor.GREEN);
      return;
    }
    for (Activatable activatable : activatablesToShow) {
      displayActivatable(activatable);
    }
  }

  protected void displayActivatable(Activatable activatable) {
    Component line = Component.text(" - ", NamedTextColor.GRAY);
    Component separator = Component.text(": ", NamedTextColor.GRAY);
    Component timeLeft = getTimeLeftComponent(activatable);
    displayMessage(line.append(activatable.toComponent()).append(separator).append(timeLeft));
  }

  protected FreakyVilleServer getServerFromString(String server) {
    return switch (server.toLowerCase()) {
      case "nprison", "prison", "np" -> FreakyVilleServer.PRISON;
      case "kitpvp", "kit" -> FreakyVilleServer.KIT_PVP;
      case "skyblock", "sb" -> FreakyVilleServer.SKY_BLOCK;
      case "friheden", "fri" -> FreakyVilleServer.THE_CITY;
      default -> throw new IllegalArgumentException("Invalid server");
    };
  }

  protected Component getTimeLeftComponent(Activatable activatable) {
    long timeLeftMillis = this.activatableService.getCooldownTime(activatable);
    String timeLeftString = NumberUtil.convertLongToTimeString(timeLeftMillis);
    return Component.text(timeLeftString, NamedTextColor.WHITE);
  }
}
