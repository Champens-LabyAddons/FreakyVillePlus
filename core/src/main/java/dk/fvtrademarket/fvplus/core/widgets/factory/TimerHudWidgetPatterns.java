package dk.fvtrademarket.fvplus.core.widgets.factory;

import dk.fvtrademarket.fvplus.api.activatable.Activatable;
import dk.fvtrademarket.fvplus.api.activatable.guardvault.GuardVault;
import dk.fvtrademarket.fvplus.core.widgets.TimerHudWidget;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.serializer.plain.PlainTextComponentSerializer;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;

public class TimerHudWidgetPatterns {
  public TimerHudWidget createTimerHudWidget(GuardVault guardVault) {
    return new TimerHudWidget("guard_vault_timer_" + guardVault.getPrisonSector().name(),
        componentToString((TextComponent) guardVault.toComponent()), getIcon(guardVault), 0L);
  }

  private String componentToString(TextComponent component) {
    return PlainTextComponentSerializer.plainText().serialize(component);
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
