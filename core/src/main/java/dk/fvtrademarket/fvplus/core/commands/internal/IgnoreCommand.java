package dk.fvtrademarket.fvplus.core.commands.internal;

import dk.fvtrademarket.fvplus.api.service.message.MessageService;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.util.Components;
import dk.fvtrademarket.fvplus.core.util.Messaging;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class IgnoreCommand extends Command {

  private final ClientInfo clientInfo;
  private final MessageService messageService;

  public IgnoreCommand(ClientInfo clientInfo, MessageService messageService) {
    super("ignore", "ign");
    this.clientInfo = clientInfo;
    this.messageService = messageService;
    this.messagePrefix(Messaging.addonPrefix());
    this.translationKey("fvplus.commands.ignore");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.clientInfo.isOnFreakyVille()) return false;
    if (arguments.length < 1) {
      displaySyntax();
      return false;
    }
    if (arguments[0].equalsIgnoreCase("list")) {
      listIgnoredPlayers();
      return true;
    }
    if (arguments[0].equalsIgnoreCase("add")) {
      if (arguments.length < 2) {
        displaySyntax("add");
        return false;
      }
      addIgnoredPlayer(arguments[1]);
      return true;
    }
    if (arguments[0].equalsIgnoreCase("remove")) {
      if (arguments.length < 2) {
        displaySyntax("remove");
        return false;
      }
      removeIgnoredPlayer(arguments[1]);
      return true;
    }
    return false;
  }

  private void listIgnoredPlayers() {
    displayTranslatable("list.header", NamedTextColor.GOLD);
    if (this.messageService.getIgnoredPlayers().isEmpty()) {
      displayTranslatable("list.emptyIgnoreList", NamedTextColor.GRAY);
      return;
    }
    Component indent = Components.INDENT.color(NamedTextColor.GRAY);
    this.messageService.getIgnoredPlayers().forEach(player -> displayMessage(
          indent.copy().append(Component.text(player, NamedTextColor.AQUA))
    ));
  }

  private void addIgnoredPlayer(String player) {
    if (this.messageService.addIgnoredPlayer(player)) {
      displayTranslatable("add.success", NamedTextColor.GREEN, player);
    } else {
      displayTranslatable("add.alreadyIgnored", NamedTextColor.RED, player);
    }
  }

  private void removeIgnoredPlayer(String player) {
    if (this.messageService.removeIgnoredPlayer(player)) {
      displayTranslatable("remove.success", NamedTextColor.GREEN, player);
    } else {
      displayTranslatable("remove.notIgnored", NamedTextColor.RED, player);
    }
  }
}
