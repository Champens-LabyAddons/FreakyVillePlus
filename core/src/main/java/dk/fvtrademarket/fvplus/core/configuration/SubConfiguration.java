package dk.fvtrademarket.fvplus.core.configuration;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ShowSettingInParent;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public abstract class SubConfiguration extends Config {
  @ShowSettingInParent
  @SwitchSetting
  protected final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public boolean passesChecks(ConfigProperty<Boolean> property) {
    return enabled.get() && property.get();
  }
}
