package dk.fvtrademarket.fvplus.core.message;

import dk.fvtrademarket.fvplus.api.misc.EventMessage;
import dk.fvtrademarket.fvplus.api.service.message.MessageService;
import net.labymod.api.models.Implements;
import net.labymod.api.util.Pair;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Implements(MessageService.class)
public class DefaultMessageService implements MessageService {
  private final Map<String, EventMessage> messages;
  private final Map<Pair<String, String>, EventMessage> advancedMessages;
  private final Map<String, EventMessage> endVarMessages;

  public DefaultMessageService() {
    this.messages = new HashMap<>();
    this.advancedMessages = new HashMap<>();
    this.endVarMessages = new HashMap<>();
  }

  @Override
  public void addMessage(String messageStr, EventMessage eventMessage) {
    this.messages.put(messageStr, eventMessage);
  }

  @Override
  public void addAdvancedMessage(Pair<String, String> messagePair, EventMessage eventMessage) {
    this.advancedMessages.put(messagePair, eventMessage);
  }

  @Override
  public void addEndVarMessage(String messageStr, EventMessage eventMessage) {
    this.endVarMessages.put(messageStr, eventMessage);
  }

  @Override
  public void removeMessage(EventMessage eventMessage) {
    this.messages.values().remove(eventMessage);
    this.advancedMessages.values().remove(eventMessage);
    this.endVarMessages.values().remove(eventMessage);
  }

  @Override
  public Map<String, EventMessage> getMessages() {
    return Map.copyOf(this.messages);
  }

  @Override
  public Map<Pair<String, String>, EventMessage> getAdvancedMessages() {
    return Map.copyOf(this.advancedMessages);
  }

  @Override
  public Map<String, EventMessage> getEndVarMessages() {
    return Map.copyOf(this.endVarMessages);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void shutdown() {

  }
}
