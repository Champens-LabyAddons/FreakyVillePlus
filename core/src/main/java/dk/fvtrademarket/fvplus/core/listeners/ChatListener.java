package dk.fvtrademarket.fvplus.core.listeners;

import dk.fvtrademarket.fvplus.api.enums.FreakyVilleMessage;
import dk.fvtrademarket.fvplus.api.event.messaging.MessageRecognizedEvent;
import dk.fvtrademarket.fvplus.api.service.message.MessageService;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import net.labymod.api.Laby;
import net.labymod.api.event.Priority;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener {
  private final ClientInfo clientInfo;
  private final MessageService messageService;

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
}
