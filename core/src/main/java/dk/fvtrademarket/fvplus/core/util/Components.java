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

  public static Component getProgressBar(double progress, double maxProgress, int length) {
    if (maxProgress == -1) {
      return Component.text("▇".repeat(length), NamedTextColor.DARK_AQUA);
    } else if (progress >= maxProgress) {
      return Component.text("▇".repeat(length), NamedTextColor.AQUA);
    }
    int fullBars = (int) Math.floor(progress / maxProgress * length);
    int emptyBars = length - fullBars;
    return Component.text()
        .append(Component.text("▇".repeat(fullBars), NamedTextColor.AQUA))
        .append(Component.text("▇".repeat(emptyBars), NamedTextColor.DARK_GRAY))
        .build();
  }

  public static Component getProgressFraction(double progress, double maxProgress) {
    String progressString = NumberUtil.convertNumberToSimpleString(progress);
    String maxProgressString = NumberUtil.convertNumberToSimpleString(maxProgress);
    if (maxProgress == -1) {
      maxProgressString = "Ω";
    } else if (maxProgress == 0) {
      maxProgressString = "¿";
    }
    return Component.text()
        .append(Component.text(progressString, NamedTextColor.AQUA))
        .append(Component.text("/", NamedTextColor.GRAY))
        .append(Component.text(maxProgressString, NamedTextColor.DARK_AQUA))
        .build();
  }
}
