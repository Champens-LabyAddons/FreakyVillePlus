package dk.fvtrademarket.fvplus.core.message;

import dk.fvtrademarket.fvplus.api.enums.FreakyVilleMessage;
import dk.fvtrademarket.fvplus.api.misc.EventMessage;
import dk.fvtrademarket.fvplus.api.service.message.MessageService;
import dk.fvtrademarket.fvplus.core.util.DataFormatter;
import dk.fvtrademarket.fvplus.core.util.Resource;
import net.labymod.api.models.Implements;
import net.labymod.api.util.Pair;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Implements(MessageService.class)
public class DefaultMessageService implements MessageService {
  private final Map<String, EventMessage> messages;
  private final Map<Pair<String, String>, EventMessage> advancedMessages;
  private final Map<String, EventMessage> endVarMessages;

  private boolean initialized;

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
    if (this.initialized) {
      throw new IllegalStateException("Service already initialized");
    }

    ArrayList<String[]> messageData = DataFormatter.csv(Resource.MESSAGES.toString());
    ArrayList<String[]> advancedMessageData = DataFormatter.csv(Resource.ADVANCED_MESSAGES.toString());
    ArrayList<String[]> endVarMessageData = DataFormatter.csv(Resource.END_VAR_MESSAGES.toString());

    if (!messageData.isEmpty()) {
      messageData.removeFirst();
      for (String[] line : messageData) {
        this.messages.put(line[0], FreakyVilleMessage.fromString(line[1]));
      }
    }

    if (!advancedMessageData.isEmpty()) {
      advancedMessageData.removeFirst();
      for (String[] line : advancedMessageData) {
        this.advancedMessages.put(Pair.of(line[0], line[1]), FreakyVilleMessage.fromString(line[2]));
      }
    }

    if (!endVarMessageData.isEmpty()) {
      endVarMessageData.removeFirst();
      for (String[] line : endVarMessageData) {
        this.endVarMessages.put(line[0], FreakyVilleMessage.fromString(line[1]));
      }
    }

    this.initialized = true;
  }

  @Override
  public void shutdown() {

  }
}
