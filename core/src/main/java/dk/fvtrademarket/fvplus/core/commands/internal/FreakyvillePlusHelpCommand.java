package dk.fvtrademarket.fvplus.core.commands.internal;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.util.I18n;
import dk.fvtrademarket.fvplus.core.commands.FreakyVillePlusCommand;

public class FreakyvillePlusHelpCommand extends FreakyVillePlusCommand {
  public FreakyvillePlusHelpCommand() {
    super("freakyHelp", "", "fh", "fhelp");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (arguments.length == 0) {
      sendHelpMessage();
      return true;
    }
    specificHelp(arguments[0]);
    return true;
  }

  private void sendHelpMessage() {
    String header = " -= [ " + I18n.getTranslation(getTranslationKey("header")) + " ] =-";
    Component headerComponent = Component.text(header).color(NamedTextColor.GOLD);
    displayMessage(headerComponent);
    displayTranslatable("commands.waypoint.description", NamedTextColor.GRAY, " - /ce waypoint");
    displayTranslatable("commands.checkRoll.description", NamedTextColor.GRAY, " - /checkroll");
    displayTranslatable("commands.timer.description", NamedTextColor.GRAY, " - /timer");
    displayTranslatable("commands.freakyHelp.description", NamedTextColor.GRAY, " - /freakyhelp");
  }

  private void specificHelp(String command) {
    switch (command.toLowerCase()) {
      case "waypoint":
      case "w":
      case "wp":
        displayTranslatable("commands.waypoint.usage", NamedTextColor.AQUA);
        break;
      case "timer":
      case "tim":
        displayTranslatable("commands.timer.usage", NamedTextColor.AQUA);
        break;
      case "freakyhelp":
      case "fhelp":
      case "fh":
        displayTranslatable("commands.freakyHelp.usage", NamedTextColor.AQUA);
        break;
      default:
        displayMessage(Component.translatable(getTranslationKey("noCommand")).color(NamedTextColor.RED));
    }
  }
}
