package dk.fvtrademarket.fvplus.api.enums;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;

/**
 * Repræsentation af fængselsblokke på {@link FreakyVilleServer#PRISON}
 *
 * @author Champen_V1ldtand
 * @since 2.0.0
 */
public enum PrisonSector {
  A_PLUS("A+", NamedTextColor.GOLD),
  A("A", NamedTextColor.DARK_GREEN),
  B_PLUS("B+", NamedTextColor.YELLOW),
  B("B", NamedTextColor.BLUE),
  C("C", NamedTextColor.GRAY),
  ;

  private final String displayName;
  private final TextColor componentColor;

  PrisonSector(String displayName, TextColor componentColor) {
    this.displayName = displayName;
    this.componentColor = componentColor;
  }

  public Component toComponent() {
    return Component.text(displayName, componentColor);
  }

  public static PrisonSector fromString(String sector) {
    return switch (sector.toUpperCase()) {
      case "A+" -> A_PLUS;
      case "A" -> A;
      case "B+" -> B_PLUS;
      case "B" -> B;
      case "C" -> C;
      default -> throw new IllegalArgumentException("Unknown sector: " + sector);
    };
  }
}
