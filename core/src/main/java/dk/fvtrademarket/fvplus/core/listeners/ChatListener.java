package dk.fvtrademarket.fvplus.core.listeners;

import dk.fvtrademarket.fvplus.api.enums.FreakyVilleMessage;
import dk.fvtrademarket.fvplus.api.event.messaging.MessageRecognizedEvent;
import dk.fvtrademarket.fvplus.api.service.message.MessageService;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Priority;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.util.logging.Logging;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener {
  private final ClientInfo clientInfo;
  private final MessageService messageService;
  private final Logging logging = Logging.create(this.getClass());

  public ChatListener(ClientInfo clientInfo, MessageService messageService) {
    this.clientInfo = clientInfo;
    this.messageService = messageService;
  }

  @Subscribe(Priority.FIRST)
  public void onChatMessageReceive(ChatReceiveEvent event) {
    if (!this.clientInfo.isOnFreakyVille()) {
      return;
    }
    String message = event.chatMessage().getOriginalPlainText().trim();
    for (Pattern pattern : this.messageService.getMessagePatterns()) {
      Matcher matcher = pattern.matcher(message);
      if (matcher.matches()) {
        FreakyVilleMessage type = this.messageService.getPatternMap().get(pattern);
        if (type == FreakyVilleMessage.PLAYER_MESSAGE_ALL) {
          playerMessageAll(matcher, event);
          return;
        }
        MessageRecognizedEvent recognizedEvent;
        if (type.isMessageCancelled()) {
          event.setCancelled(true);
          recognizedEvent = new MessageRecognizedEvent(type, matcher, event.chatMessage());
        } else {
          recognizedEvent = new MessageRecognizedEvent(type, matcher);
        }
        Laby.fireEvent(recognizedEvent);
      }
    }
  }

  private void playerMessageAll(Matcher matcher, ChatReceiveEvent event) {
    String sender = matcher.group(6);
    String messageText = matcher.group(7);
    if (this.messageService.getIgnoredPlayers().contains(sender)) {
      event.setMessage(Component.text("Ignoreret besked, hold musen over for at se", NamedTextColor.RED)
          .hoverEvent(HoverEvent.showText(
                  Component.text(sender, NamedTextColor.GRAY)
                      .append(Component.text(": ", NamedTextColor.WHITE))
                      .append(Component.text(messageText, NamedTextColor.WHITE))
              )
          )
      );
    } else if (this.messageService.getBlockedPlayers().contains(sender)) {
      this.logging.info("Blocked Message from: " + sender + " - " + messageText);
      event.setCancelled(true);
    }
  }
}
