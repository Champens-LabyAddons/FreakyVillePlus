package dk.fvtrademarket.fvplus.core.util;

import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.util.I18n;

public final class Messaging {
  private static ChatExecutor executor;
  private static boolean isSet;

  private Messaging() {}

  public static void setExecutor(ChatExecutor executor) {
    if (isSet) {
      throw new RuntimeException(I18n.translate("fvplus.logging.error.messagingAlreadySet"));
    }
    Messaging.executor = executor;
    Messaging.isSet = true;
  }

  public static ChatExecutor executor() {
    return Messaging.executor;
  }

  public static void displayTranslatable(String key, TextColor color) {
    executor.displayClientMessage(addonPrefix().append(Component.text(" ")).append(Component.translatable(key, color)));
  }

  public static Component addonPrefix() {
    Component leftBracket = Component.text("[", NamedTextColor.DARK_GRAY);
    Component rightBracket = Component.text("]", NamedTextColor.DARK_GRAY);
    Component text = Component.translatable("fvplus.prefix", NamedTextColor.GOLD)
        .append(Component.text("+", NamedTextColor.AQUA));
    return leftBracket.append(text).append(rightBracket);
  }
}
