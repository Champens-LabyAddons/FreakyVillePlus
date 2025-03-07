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
    executor.displayClientMessage(Components.ADDON_PREFIX.copy().append(Component.space()).append(Component.translatable(key, color)));
  }
}
