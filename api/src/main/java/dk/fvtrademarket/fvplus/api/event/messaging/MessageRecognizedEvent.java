package dk.fvtrademarket.fvplus.api.event.messaging;

import dk.fvtrademarket.fvplus.api.enums.FreakyVilleMessage;
import net.labymod.api.client.chat.ChatMessage;
import net.labymod.api.event.Event;
import java.util.Optional;
import java.util.regex.Matcher;

public class MessageRecognizedEvent implements Event {
  private final FreakyVilleMessage type;
  private final Matcher matcher;
  private final ChatMessage message;

  public MessageRecognizedEvent(FreakyVilleMessage type, Matcher matcher) {
    this.type = type;
    this.matcher = matcher;
    this.message = null;
  }

  public MessageRecognizedEvent(FreakyVilleMessage type, Matcher matcher, ChatMessage message) {
    this.type = type;
    this.matcher = matcher;
    this.message = message;
  }

  public FreakyVilleMessage getType() {
    return this.type;
  }

  public Matcher getMatcher() {
    return this.matcher;
  }

  public Optional<ChatMessage> getMessage() {
    return Optional.ofNullable(this.message);
  }
}