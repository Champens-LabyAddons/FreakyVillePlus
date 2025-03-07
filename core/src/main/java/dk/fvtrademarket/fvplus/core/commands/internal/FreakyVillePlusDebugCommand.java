package dk.fvtrademarket.fvplus.core.commands.internal;

import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.util.Components;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.format.NamedTextColor;

public class FreakyVillePlusDebugCommand extends Command {

  private final ClientInfo clientInfo;

  public FreakyVillePlusDebugCommand(ClientInfo clientInfo) {
    super("fvplusdebug", "fvpdebug", "fvdebug", "fdebug");
    this.clientInfo = clientInfo;

    this.messagePrefix(Components.ADDON_PREFIX.copy());
    this.translationKey("fvplus.commands.debug");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.clientInfo.isOnFreakyVille()) return false;
    displayTranslatable("header", NamedTextColor.GOLD);
    displayTranslatable("isConnected", NamedTextColor.GRAY, this.clientInfo.isOnFreakyVille());
    displayTranslatable("currentServer", NamedTextColor.GRAY, this.clientInfo.getCurrentServer().getTranslatedName());
    displayTranslatable("lastServer", NamedTextColor.GRAY, this.clientInfo.getLastServer().getTranslatedName());
    if (this.clientInfo.getCurrentServer() == FreakyVilleServer.PRISON) {
      displayTranslatable("foundPrison", NamedTextColor.GRAY, this.clientInfo.getPrisonSector().orElse(null).toComponent());
    }
    return true;
  }

}
