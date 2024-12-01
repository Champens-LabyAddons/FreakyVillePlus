package dk.fvtrademarket.fvplus.core.listeners.internal;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
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
import java.util.Objects;

public class PrisonNavigationListener {
  private final ClientInfo clientInfo;
  private final Logging logger = Logging.create(this.getClass());

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
    if (this.clientInfo.getPrisonSector().isPresent()) {
      return;
    }
    String plainMessage = event.chatMessage().getPlainText().trim();
    if (plainMessage.startsWith(Objects.requireNonNull(headerDecoration().getFirst())) &&
        plainMessage.endsWith(Objects.requireNonNull(headerDecoration().getSecond()))) {
      try {
        this.clientInfo.setPrisonSector(prisonFromHeader(plainMessage));
      } catch (IllegalArgumentException e) {
        this.clientInfo.setPrisonSector(null);
        this.logger.error(I18n.translate("fvplus.logging.error.findingPrison"), e);
        Messaging.displayTranslatable("fvplus.logging.error.findingPrison", NamedTextColor.RED);
      }
    }
  }

  private PrisonSector prisonFromHeader(String header) {
    String restProduct = header
        .replace(Objects.requireNonNull(headerDecoration().getFirst()), "")
        .replace(Objects.requireNonNull(headerDecoration().getSecond()), "")
        .trim();
      return switch (restProduct.toUpperCase()) {
      case "C" -> PrisonSector.C;
      case "B" -> PrisonSector.B;
      case "A" -> PrisonSector.A;
      default -> throw new IllegalArgumentException(I18n.translate("fvplus.logging.error.unexpectedValue", restProduct.toUpperCase()));
    };
  }

  private Pair<String, String> headerDecoration() {
    return Pair.of("----- Online Fanger i", "----");
  }
}
