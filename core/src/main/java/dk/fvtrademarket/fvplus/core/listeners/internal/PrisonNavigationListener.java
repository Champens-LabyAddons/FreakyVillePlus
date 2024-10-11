package dk.fvtrademarket.fvplus.core.listeners.internal;

import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Priority;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.util.I18n;
import net.labymod.api.util.Pair;
import net.labymod.api.util.logging.Logging;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.core.util.Messaging;
import dk.fvtrademarket.fvplus.core.util.Prison;
import java.util.Objects;

public class PrisonNavigationListener {
  private final ClientInfo clientInfo;

  public PrisonNavigationListener(ClientInfo clientInfo) {
    this.clientInfo = clientInfo;
  }

  @Subscribe(Priority.FIRST)
  public void onChatReceive(ChatReceiveEvent event) {
    if (!this.clientInfo.isOnFreakyVille()) {
      return;
    }
    if (this.clientInfo.getCurrentServer() != FreakyVilleServer.PRISON) {
      return;
    }
    if (this.clientInfo.getPrison().isPresent()) {
      return;
    }
    String plainMessage = event.chatMessage().getPlainText().trim();
    if (plainMessage.startsWith(Objects.requireNonNull(headerDecoration().getFirst())) &&
        plainMessage.endsWith(Objects.requireNonNull(headerDecoration().getSecond()))) {
      try {
        this.clientInfo.setPrison(prisonFromHeader(plainMessage));
      } catch (IllegalArgumentException e) {
        this.clientInfo.setPrison(null);
        Logging.getLogger().error(I18n.translate("fvplus.logging.error.findingPrison"), e);
        Messaging.displayTranslatable("fvplus.logging.error.findingPrison", NamedTextColor.RED);
      }
    }
  }

  private Prison prisonFromHeader(String header) {
    String restProduct = header
        .replace(Objects.requireNonNull(headerDecoration().getFirst()), "")
        .replace(Objects.requireNonNull(headerDecoration().getSecond()), "")
        .trim();
      return switch (restProduct.toUpperCase()) {
      case "C" -> Prison.C;
      case "B" -> Prison.B;
      case "A" -> Prison.A;
      default -> throw new IllegalArgumentException(I18n.translate("fvplus.logging.error.unexpectedValue", restProduct.toUpperCase()));
    };
  }

  private Pair<String, String> headerDecoration() {
    return Pair.of("----- Online Fanger i", "----");
  }
}
