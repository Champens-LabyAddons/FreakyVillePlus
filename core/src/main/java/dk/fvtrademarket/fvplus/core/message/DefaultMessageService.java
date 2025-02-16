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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Singleton
@Implements(MessageService.class)
public class DefaultMessageService implements MessageService {

  private final HashMap<Pattern, FreakyVilleMessage> patternMap = new HashMap<>();
  private final HashSet<String> ignoredPlayers = new HashSet<>();
  private final HashSet<String> blockedPlayers = new HashSet<>();

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
  public boolean addIgnoredPlayer(String player) {
    return this.ignoredPlayers.add(player);
  }

  @Override
  public boolean removeIgnoredPlayer(String player) {
    return this.ignoredPlayers.remove(player);
  }

  @Override
  public Collection<String> getIgnoredPlayers() {
    return Set.copyOf(this.ignoredPlayers);
  }

  @Override
  public boolean addBlockedPlayer(String player) {
    return this.blockedPlayers.add(player);
  }

  @Override
  public boolean removeBlockedPlayer(String player) {
    return this.blockedPlayers.remove(player);
  }

  @Override
  public Collection<String> getBlockedPlayers() {
    return Set.copyOf(this.blockedPlayers);
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
