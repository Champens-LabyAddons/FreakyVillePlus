package dk.fvtrademarket.fvplus.core.commands.internal;

import dk.fvtrademarket.fvplus.api.service.message.MessageService;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.util.Components;
import dk.fvtrademarket.fvplus.core.util.Messaging;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class BlockCommand extends Command {

  private final ClientInfo clientInfo;
  private final MessageService messageService;

  public BlockCommand(ClientInfo clientInfo, MessageService messageService) {
    super("block", "bloker");
    this.clientInfo = clientInfo;
    this.messageService = messageService;
    this.messagePrefix(Components.ADDON_PREFIX.copy());
    this.translationKey("fvplus.commands.block");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.clientInfo.isOnFreakyVille()) return false;
    if (arguments.length < 1) {
      displaySyntax();
      return false;
    }
    if (arguments[0].equalsIgnoreCase("list")) {
      listBlockedPlayers();
      return true;
    }
    if (arguments[0].equalsIgnoreCase("add")) {
      if (arguments.length < 2) {
        displaySyntax("add");
        return false;
      }
      addBlockedPlayer(arguments[1]);
      return true;
    }
    if (arguments[0].equalsIgnoreCase("remove")) {
      if (arguments.length < 2) {
        displaySyntax("remove");
        return false;
      }
      removeBlockedPlayer(arguments[1]);
      return true;
    }
    return false;
  }

  private void listBlockedPlayers() {
    displayTranslatable("list.header", NamedTextColor.GOLD);
    if (this.messageService.getBlockedPlayers().isEmpty()) {
      displayTranslatable("list.emptyBlockList", NamedTextColor.GRAY);
      return;
    }
    Component indent = Components.INDENT.color(NamedTextColor.GRAY);
    this.messageService.getBlockedPlayers().forEach(player -> displayMessage(
        indent.copy().append(Component.text(player, NamedTextColor.AQUA))
    ));
  }

  private void addBlockedPlayer(String player) {
    if (this.messageService.addBlockedPlayer(player)) {
      displayTranslatable("add.success", NamedTextColor.GREEN, player);
    } else {
      displayTranslatable("add.alreadyBlocked", NamedTextColor.RED, player);
    }
  }

  private void removeBlockedPlayer(String player) {
    if (this.messageService.removeBlockedPlayer(player)) {
      displayTranslatable("remove.success", NamedTextColor.GREEN, player);
    } else {
      displayTranslatable("remove.notBlocked", NamedTextColor.RED, player);
    }
  }
}
