package org.champenslabyaddons.fvplus.configuration;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import org.champenslabyaddons.fvplus.module.general.SessionModule;
import org.champenslabyaddons.fvplus.util.Setting;

public class SessionSubConfiguration {
  @ParentSwitch
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> includeStatsFromPrison = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> saveToFileSystem = new ConfigProperty<>(false);

  public SessionSubConfiguration() {
    Setting.addModuleListener(this.enabled, SessionModule.class);
  }

  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> includeStatsFromPrison() {
    return this.includeStatsFromPrison;
  }

  public ConfigProperty<Boolean> saveToFileSystem() {
    return this.saveToFileSystem;
  }
}
