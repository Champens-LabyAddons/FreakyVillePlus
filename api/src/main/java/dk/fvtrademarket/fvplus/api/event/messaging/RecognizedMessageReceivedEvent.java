package dk.fvtrademarket.fvplus.api.event.messaging;

import net.labymod.api.client.chat.ChatMessage;
import net.labymod.api.event.Event;

public class RecognizedMessageReceivedEvent implements Event {
  private final ChatMessage message;

  public RecognizedMessageReceivedEvent(ChatMessage message) {
    this.message = message;
  }

  public ChatMessage getMessage() {
    return message;
  }
}
