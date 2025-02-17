package dk.fvtrademarket.fvplus.core.util;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public final class Components {
  private Components() {}

  public static final Component INDENT = Component.text(" - ");

  public static final Component ADDON_PREFIX = Component.text()
      .append(Component.text("[", NamedTextColor.DARK_GRAY))
      .append(Component.translatable("fvplus.prefix", NamedTextColor.GOLD))
      .append(Component.text("+", NamedTextColor.AQUA))
      .append(Component.text("]", NamedTextColor.DARK_GRAY))
      .build();
}
