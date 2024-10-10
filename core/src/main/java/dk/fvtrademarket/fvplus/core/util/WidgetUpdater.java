package dk.fvtrademarket.fvplus.core.util;

import net.labymod.api.client.gui.hud.HudWidgetRegistry;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;

public class WidgetUpdater {
  private final HudWidgetRegistry hudWidgetRegistry;
  private int tickCounter;

  public WidgetUpdater(HudWidgetRegistry hudWidgetRegistry) {
    this.hudWidgetRegistry = hudWidgetRegistry;
    this.tickCounter = 0;
  }

  @Subscribe
  public void onGameTick(GameTickEvent event) {
    this.tickCounter += 2;
    if (this.tickCounter == 40) {
      hudWidgetRegistry.updateHudWidgets("Timed Widgets Update");
    }
    if (this.tickCounter >= 40) {
      this.tickCounter = 0;
    }
  }
}
