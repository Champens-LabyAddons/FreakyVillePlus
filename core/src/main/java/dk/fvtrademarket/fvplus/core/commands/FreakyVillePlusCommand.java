package dk.fvtrademarket.fvplus.core.commands;

import dk.fvtrademarket.fvplus.core.util.Components;
import net.labymod.api.client.chat.command.Command;
import dk.fvtrademarket.fvplus.core.util.Messaging;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public abstract class FreakyVillePlusCommand extends Command {
  private final String serverAndCategoryKey;

  protected FreakyVillePlusCommand(@NotNull String prefix, String serverAndCategoryKey, @NotNull String... aliases) {
    super(prefix, aliases);
    this.serverAndCategoryKey = serverAndCategoryKey;
    String translationKey = "fvplus.";
    if (serverAndCategoryKey != null && !serverAndCategoryKey.isEmpty()) {
      translationKey += "server." + serverAndCategoryKey + ".";
    }
    translationKey += "commands." + prefix;
    this.translationKey(translationKey);
    this.messagePrefix(Components.ADDON_PREFIX.copy());
  }

  public abstract boolean execute(String prefix, String[] arguments);

  protected final List<String> subCommandPrefixes() {
    return this.getSubCommands().stream().map(Command::getPrefix).toList();
  }

  protected String getServerAndCategoryKey() {
    return this.serverAndCategoryKey;
  }
}
