package dk.fvtrademarket.fvplus.core.message;

import dk.fvtrademarket.fvplus.api.enums.FreakyVilleMessage;
import dk.fvtrademarket.fvplus.api.service.message.MessageService;
import dk.fvtrademarket.fvplus.core.util.DataFormatter;
import dk.fvtrademarket.fvplus.core.util.Resource;
import net.labymod.api.models.Implements;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Singleton
@Implements(MessageService.class)
public class DefaultMessageService implements MessageService {

  private final HashMap<Pattern, FreakyVilleMessage> patternMap = new HashMap<>();

  private boolean initialized = false;

  @Override
  public void addMessagePattern(String message, FreakyVilleMessage event) {
    this.patternMap.put(Pattern.compile(message), event);
  }

  @Override
  public Collection<Pattern> getMessagePatterns() {
    return this.patternMap.keySet();
  }

  @Override
  public Map<Pattern, FreakyVilleMessage> getPatternMap() {
    return Map.copyOf(this.patternMap);
  }

  @Override
  public void initialize() {
    if (this.initialized) {
      throw new IllegalStateException("Service already initialized");
    }

    ArrayList<String[]> messages = DataFormatter.tsv(Resource.MESSAGES.toString(), true);

    for (String[] line : messages) {
      this.addMessagePattern(
          line[0],
          FreakyVilleMessage.fromString(line[1])
      );
    }

    this.initialized = true;
  }

  @Override
  public void shutdown() {

  }
}
