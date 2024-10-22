package dk.fvtrademarket.fvplus.core.widgets.factory;

import dk.fvtrademarket.fvplus.api.activatable.Activatable;
import dk.fvtrademarket.fvplus.api.activatable.guardvault.GuardVault;
import dk.fvtrademarket.fvplus.api.activatable.misc.GangArea;
import dk.fvtrademarket.fvplus.core.widgets.TimerHudWidget;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.serializer.plain.PlainTextComponentSerializer;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class TimerHudWidgetPatterns {

  public TimerHudWidget createTimerHudWidget(GuardVault guardVault) {
    TextComponent component = (TextComponent) guardVault.toComponent();
    return new TimerHudWidget("guard_vault_timer_" +
        correctFormat(componentToString(component)),
        componentToString((TextComponent) guardVault.toComponent()), getIcon(guardVault), 0L);
  }

  public TimerHudWidget createTimerHudWidget(GangArea gangArea) {
    TextComponent component = (TextComponent) gangArea.toComponent();
    return new TimerHudWidget("gang_area_timer_" +
        correctFormat(componentToString(component)),
        componentToString((TextComponent) gangArea.toComponent()), getIcon(gangArea), 0L);
  }

  public TimerHudWidget createTimerHudWidget(Activatable activatable) {
    TextComponent component = (TextComponent) activatable.toComponent();
    return new TimerHudWidget("activatable_misc_timer_" +
        correctFormat(componentToString(component)),
        componentToString(component),
        getIcon(activatable), 0L);
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
        default -> Icon.sprite16(iconLocation, 5, 0);
      };
    } else {
      return Icon.sprite16(iconLocation, 1, 5);
    }
  }
}
