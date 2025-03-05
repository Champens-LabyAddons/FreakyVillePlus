package dk.fvtrademarket.fvplus.core.util;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;

public final class Components {
  private Components() {}

  public static final Component INDENT = Component.text(" - ");

  public static final Component ADDON_PREFIX = Component.text()
      .append(Component.text("[", NamedTextColor.DARK_GRAY))
      .append(Component.translatable("fvplus.prefix", NamedTextColor.GOLD))
      .append(Component.text("+", NamedTextColor.AQUA))
      .append(Component.text("]", NamedTextColor.DARK_GRAY))
      .build();

  public static Component getProgressBar(double progress, double maxProgress, int length, TextColor coloredIn, TextColor fullyColoredIn, TextColor empty) {
    if (maxProgress == -1) {
      return Component.text("▇".repeat(length), fullyColoredIn);
    } else if (progress >= maxProgress) {
      return Component.text("▇".repeat(length), coloredIn);
    }
    int fullBars = (int) Math.floor(progress / maxProgress * length);
    int emptyBars = length - fullBars;
    return Component.text()
        .append(Component.text("▇".repeat(fullBars), coloredIn))
        .append(Component.text("▇".repeat(emptyBars), empty))
        .build();
  }

  public static Component getProgressFraction(double progress, double maxProgress, TextColor counterColor, TextColor fraction, TextColor referenceColor) {
    String progressString = NumberUtil.convertNumberToSimpleString(progress);
    String maxProgressString = NumberUtil.convertNumberToSimpleString(maxProgress);
    if (maxProgress == -1) {
      maxProgressString = "Ω";
    } else if (maxProgress == 0) {
      maxProgressString = "¿";
    }
    return Component.text()
        .append(Component.text(progressString, counterColor))
        .append(Component.text("/", fraction))
        .append(Component.text(maxProgressString, referenceColor))
        .build();
  }
}
