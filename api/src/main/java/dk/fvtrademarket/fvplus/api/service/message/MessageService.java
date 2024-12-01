package dk.fvtrademarket.fvplus.api.service.message;

import dk.fvtrademarket.fvplus.api.misc.EventMessage;
import dk.fvtrademarket.fvplus.api.service.Service;
import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.api.util.Pair;
import java.util.Map;

@Referenceable
public interface MessageService extends Service {

  /**
   * Tilføjer en besked
   * <p>
   * Simple beskeder uden egentlige variable informationer
   *
   * @param messageStr  Beskedens tekst
   * @param eventMessage Beskedtypen
   */
  void addMessage(String messageStr, EventMessage eventMessage);

  /**
   * Tilføjer en besked med advancerede parametre
   * <p>
   * En avanceret besked ses som en besked i 2 dele, hvor den første del er beskedens begyndelse og den anden del er beskedens slutning.
   * Ideen er at man på en nem måde kan få variable informationer ud af en besked, fordi vi kender den generelle beskedstruktur.
   * <p>
   * Eksempel:
   * <br> Første del: "Du har nu"
   * <br> Anden del: "point"
   * <br> Besked: "Du har nu 10 point"
   * <br>
   * Her kan vi nemt udtrække tallet 10, fordi vi kender beskedens struktur.
   *
   * @param messagePair  Beskedens begyndelse og slutning
   * @param eventMessage Beskedtypen
   */
  void addAdvancedMessage(Pair<String, String> messagePair, EventMessage eventMessage);

  /**
   * Tilføjer en besked med variabel information i slutningen
   *
   * @param messageStr Beskedens begyndelse
   * @param eventMessage Beskedtypen
   */
  void addEndVarMessage(String messageStr, EventMessage eventMessage);

  /**
   * Fjerner en besked
   *
   * @param eventMessage Beskedtypen
   */
  void removeMessage(EventMessage eventMessage);

  /**
   * Returnerer alle almindelige beskeder
   *
   * @return Beskederne
   */
  Map<String, EventMessage> getMessages();

  /**
   * Returnerer alle avancerede beskeder
   *
   * @return Beskederne
   */
  Map<Pair<String, String>, EventMessage> getAdvancedMessages();

  /**
   * Returnerer alle endVar beskeder
   *
   * @return Beskederne
   */
  Map<String, EventMessage> getEndVarMessages();

  @Override
  void initialize();

  @Override
  void shutdown();
}
