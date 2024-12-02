package dk.fvtrademarket.fvplus.core.listeners;

import dk.fvtrademarket.fvplus.api.event.messaging.RecognizedMessageReceivedEvent;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;

public class AdvancedChatListener {
  private final ClientInfo clientInfo;
  private final ChatExecutor chatExecutor;

  public AdvancedChatListener(ClientInfo clientInfo, ChatExecutor chatExecutor) {
    this.clientInfo = clientInfo;
    this.chatExecutor = chatExecutor;
  }

  @Subscribe
  public void onRecognizedMessageReceived(RecognizedMessageReceivedEvent event) {
    if (!this.clientInfo.isOnFreakyVille()) {
      return;
    }
    this.chatExecutor.displayClientMessage(event.getMessage()
        .component().append(Component.text("✉", NamedTextColor.GOLD)));
    this.chatExecutor.displayClientMessage(responseComponent(event.getMessage().getPlainText().trim()));
  }

  private Component responseComponent(String whatToRespondTo) {
    return switch (whatToRespondTo) {
      case "[/ce find <spiller>] Se hvem spilleren har add hos" -> waypointHelp();
      default -> Component.empty();
    };
  }

  private Component waypointHelp() {
    TextComponent syntax = Component.text("[", NamedTextColor.DARK_GRAY)
        .append(Component.text("/ce waypoint <celleID>", NamedTextColor.RED))
        .append(Component.text("]", NamedTextColor.DARK_GRAY));
    return Component.translatable("fvplus.server.prison.cell.commands.waypoint.description", NamedTextColor.WHITE, syntax);
  }
}
