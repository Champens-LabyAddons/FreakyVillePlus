package dk.fvtrademarket.fvplus.core.widgets.factory;

import dk.fvtrademarket.fvplus.api.activatable.Activatable;
import dk.fvtrademarket.fvplus.api.activatable.prison.GuardVault;
import dk.fvtrademarket.fvplus.api.activatable.prison.GangArea;
import dk.fvtrademarket.fvplus.core.widgets.TimerHudWidget;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.serializer.plain.PlainTextComponentSerializer;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class TimerHudWidgetPatterns {

  public TimerHudWidgetPatterns() {

  }

  public TimerHudWidget createTimerHudWidget(GuardVault guardVault) {
    TextComponent component = (TextComponent) guardVault.toComponent();
    String id = "guard_vault_timer_" + correctFormat(componentToString(component));
    return new TimerHudWidget(id, componentToString(component), getIcon(guardVault), -1L,
      "fvplus_guard_vault_timer_category"
    );
  }

  public TimerHudWidget createTimerHudWidget(GangArea gangArea) {
    TextComponent component = (TextComponent) gangArea.toComponent();
    String id = "gang_area_timer_" + correctFormat(componentToString(component));
    return new TimerHudWidget(id, componentToString(component), getIcon(gangArea), -1L,
        "fvplus_gang_area_timer_category"
    );
  }

  public TimerHudWidget createTimerHudWidget(Activatable activatable) {
    TextComponent component = (TextComponent) activatable.toComponent();
    String id = "activatable_misc_timer_" + correctFormat(componentToString(component));
    return new TimerHudWidget(id, componentToString(component), getIcon(activatable), -1L,
        "fvplus_misc_timer_category"
    );
  }

  private String componentToString(TextComponent component) {
    return PlainTextComponentSerializer.plainText().serialize(component);
  }

  private String correctFormat(String str) {
    return str.replace(" ", "_").toLowerCase();
  }

  private Icon getIcon(Activatable activatable) {
    ResourceLocation iconLocation = ResourceLocation.create("fvplus", "themes/vanilla/textures/settings/icons.png");
    if (activatable instanceof GuardVault vault) {
      return switch (vault.getPrisonSector()) {
        case A_PLUS -> Icon.sprite16(iconLocation, 4, 0);
        case A -> Icon.sprite16(iconLocation, 2, 0);
        case B_PLUS -> Icon.sprite16(iconLocation, 3, 0);
        case B -> Icon.sprite16(iconLocation, 1, 0);
        case C -> Icon.sprite16(iconLocation, 0, 0);
      };
    } else if (activatable instanceof GangArea) {
      return Icon.sprite16(iconLocation, 2, 1);
    }
    return Icon.sprite16(iconLocation, 0, 0);
  }
}
