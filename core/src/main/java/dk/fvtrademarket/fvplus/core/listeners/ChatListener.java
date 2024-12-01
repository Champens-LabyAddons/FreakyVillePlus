package dk.fvtrademarket.fvplus.core.listeners;

import dk.fvtrademarket.fvplus.api.event.guardvault.GuardVaultFinishEvent;
import dk.fvtrademarket.fvplus.api.event.guardvault.GuardVaultTryEvent;
import dk.fvtrademarket.fvplus.api.event.guardvault.GuardVaultUpdateEvent;
import dk.fvtrademarket.fvplus.api.service.message.MessageService;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import net.labymod.api.Laby;
import net.labymod.api.event.Event;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.util.Pair;
import java.util.Objects;

public class ChatListener {
  private final ClientInfo clientInfo;
  private final MessageService messageService;

  public ChatListener(ClientInfo clientInfo, MessageService messageService) {
    this.clientInfo = clientInfo;
    this.messageService = messageService;
  }

  @Subscribe
  public void onChatMessageReceive(ChatReceiveEvent event) {
    if (!this.clientInfo.isOnFreakyVille()) {
      return;
    }
    String message = event.chatMessage().getPlainText().trim();
    if (this.messageService.getMessages().containsKey(message)) {
      fireEvent(this.messageService.getMessages().get(message).getEvent());
      return;
    }
    for (Pair<String, String> pair : this.messageService.getAdvancedMessages().keySet()) {
      String stringedPair = pairToString(pair);
      String variable = getVariableFromString(pair, message);
      if (message.replace(variable, "").equals(stringedPair)) {
        fireEvent(this.messageService.getAdvancedMessages().get(pair).getEvent(), variable);
        return;
      }
    }
    for (String endVarMessage : this.messageService.getEndVarMessages().keySet()) {
      if (message.startsWith(endVarMessage)) {
        fireEvent(this.messageService.getEndVarMessages().get(endVarMessage).getEvent(), message.replace(endVarMessage, ""));
        return;
      }
    }
  }

  private <E extends Event> void fireEvent(E event, Object... args) {
    Event e = switch (event) {
      case GuardVaultTryEvent ignored -> {
        if (args.length > 0 && args[0] instanceof String player) {
          yield new GuardVaultTryEvent(((GuardVaultTryEvent) event).getSector(), player);
        } else {
          yield null;
        }
      }
      case GuardVaultFinishEvent ignored -> {
        if (args.length > 0 && args[0] instanceof String player) {
          yield new GuardVaultFinishEvent(((GuardVaultFinishEvent) event).getSector(), player, ((GuardVaultFinishEvent) event).wasSuccessFull());
        } else {
          yield null;
        }
      }
      case GuardVaultUpdateEvent ignored -> {
        if (args.length > 0 && args[0] instanceof String time) {
          yield new GuardVaultUpdateEvent(((GuardVaultUpdateEvent) event).getPrisonSector(), time);
        } else {
          yield null;
        }
      }
      default -> null;
    };

    if (e != null) {
      Laby.fireEvent(e);
    }
  }

  private String getVariableFromString(Pair<String, String> pair, String message) {
    String variable = "";
    if (message.startsWith(Objects.requireNonNull(pair.getFirst())) && message.endsWith(Objects.requireNonNull(pair.getSecond()))) {
      variable = message.substring(pair.getFirst().length() + 1, message.length() - pair.getSecond().length());
    }
    return variable;
  }

  private String pairToString(Pair<String, String> pair) {
    return pair.getFirst() + " " + pair.getSecond();
  }
}
