package dk.fvtrademarket.fvplus.core.configuration;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import dk.fvtrademarket.fvplus.core.module.nprison.CellModule;
import dk.fvtrademarket.fvplus.core.module.nprison.NPrisonModule;
import dk.fvtrademarket.fvplus.core.util.Setting;

public class PrisonSubConfiguration extends Config {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> enabledCellModule = new ConfigProperty<>(true);

  public PrisonSubConfiguration() {
    Setting.addModuleListener(this.enabled, NPrisonModule.class);
    Setting.addModuleListener(this.enabledCellModule, CellModule.class);
  }

  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> getEnabledCellModule() {
    return this.enabledCellModule;
  }
}
