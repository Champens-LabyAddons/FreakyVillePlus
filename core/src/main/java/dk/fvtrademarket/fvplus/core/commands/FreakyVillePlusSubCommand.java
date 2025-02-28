package dk.fvtrademarket.fvplus.core.commands;

import dk.fvtrademarket.fvplus.core.util.Components;
import net.labymod.api.client.chat.command.SubCommand;
import dk.fvtrademarket.fvplus.core.util.Messaging;
import org.jetbrains.annotations.NotNull;

public abstract class FreakyVillePlusSubCommand extends SubCommand {
  protected FreakyVillePlusSubCommand(@NotNull String prefix, String parentPrefix, String serverAndCategoryKey, @NotNull String... aliases) {
    super(prefix, aliases);
    String translationKey = "fvplus.";
    if (serverAndCategoryKey != null && !serverAndCategoryKey.isEmpty()) {
      translationKey += "server." + serverAndCategoryKey + ".";
    }
    translationKey += "commands." + parentPrefix + "." + prefix;
    this.translationKey(translationKey);
    this.messagePrefix(Components.ADDON_PREFIX.copy());
  }

  public abstract boolean execute(String prefix, String[] arguments);
}
