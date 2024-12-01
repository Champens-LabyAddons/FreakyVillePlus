package dk.fvtrademarket.fvplus.api.enums;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TranslatableComponent;
import net.labymod.api.util.I18n;

/**
 * Repræsentation af forskellige servere på Freakyville
 * <p>
 * De forskellige ENUMS kan afkodes fra diverse scoreboard headers
 *
 * @author Champen_V1ldtand
 * @since 2.0.0
 */
public enum FreakyVilleServer {
  PRISON("fvplus.server.prison.name"),
  CREATIVE("fvplus.server.creative.name"),
  SKY_BLOCK("fvplus.server.skyBlock.name"),
  THE_CITY("fvplus.server.city.name"),
  KIT_PVP("fvplus.server.kitpvp.name"),
  HUB("fvplus.server.hub.name"),
  NONE,

  /**
   * @deprecated Ekspeditionen er ikke længere tilgængelig. Den kan blive genaktiveret i fremtiden.
   */
  @Deprecated(forRemoval = false)
  THE_EXPEDITION(),

  /**
   * @deprecated Factions er ikke længere tilgængelig. Den kan blive genaktiveret i fremtiden.
   */
  @Deprecated(forRemoval = false)
  FACTIONS(),

  /**
   * @deprecated OP-Prison er ikke længere tilgængelig. Den kan blive genaktiveret i fremtiden.
   */
  @Deprecated(forRemoval = false)
  OP_PRISON(),

  /**
   * @deprecated Rumrejsen er ikke længere tilgængelig. Den kan blive genaktiveret i fremtiden.
   */
  @Deprecated(forRemoval = false)
  SPACE_JOURNEY(),
  ;

  private String nameKey;

  FreakyVilleServer() {}

  FreakyVilleServer(String nameKey) {
      this.nameKey = nameKey;
  }

  public String getNameKey() {
      return nameKey;
  }

  public String getTranslatedName() {
      return I18n.translate(nameKey);
  }

  public TranslatableComponent getTranslatedComponent() {
      return Component.translatable(nameKey);
  }

  public static FreakyVilleServer fromString(String scoreBoardTitle) {
      return switch (scoreBoardTitle) {
          case "FV Skyblock" -> FreakyVilleServer.SKY_BLOCK;
          case "FV PRISON" -> FreakyVilleServer.PRISON;
          case "FRIHEDEN" -> FreakyVilleServer.THE_CITY;
          case "FV Creative" -> FreakyVilleServer.CREATIVE;
          case "FreakyVille" -> FreakyVilleServer.HUB;
          /*
           * KitPvP har flere forskellige navne, ifølge det "scoreboard" klienten læser.
           * Derfor tjekker vi for flere navne. Det skifter engang i mellem?
           */
          case "FV KitPvP", "HP", "mvdwan" -> FreakyVilleServer.KIT_PVP;
          default -> FreakyVilleServer.NONE;
      };
  }
}
