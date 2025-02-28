package dk.fvtrademarket.fvplus.api.service.message;

import dk.fvtrademarket.fvplus.api.enums.FreakyVilleMessage;
import dk.fvtrademarket.fvplus.api.service.Service;
import net.labymod.api.reference.annotation.Referenceable;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

@Referenceable
public interface MessageService extends Service {

  /**
   * Tilføjer et pattern ved hjælp af en string.
   * @param message en string som skal konverteres til et pattern.
   * @param event en event message som skal tilknyttes til et pattern.
   */
  void addMessagePattern(String message, FreakyVilleMessage event);

  /**
   * Returnerer en kopi af en collection med alle message patterns.
   * @return en kopi af en collection med alle message patterns.
   */
  Collection<Pattern> getMessagePatterns();

  /**
   * Returnerer en kopi af en map med alle message patterns og deres tilhørende event messages.
   * @return en kopi af en map med alle message patterns og deres tilhørende event messages.
   */
  Map<Pattern, FreakyVilleMessage> getPatternMap();

  boolean addIgnoredPlayer(String player);

  boolean removeIgnoredPlayer(String player);

  Collection<String> getIgnoredPlayers();

  boolean addBlockedPlayer(String player);

  boolean removeBlockedPlayer(String player);

  Collection<String> getBlockedPlayers();

  @Override
  void initialize();

  @Override
  void shutdown();
}
