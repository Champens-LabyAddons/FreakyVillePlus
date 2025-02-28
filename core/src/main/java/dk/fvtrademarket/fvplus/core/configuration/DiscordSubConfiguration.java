package dk.fvtrademarket.fvplus.core.configuration;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import dk.fvtrademarket.fvplus.core.module.general.RPCModule;
import dk.fvtrademarket.fvplus.core.util.Setting;

public class DiscordSubConfiguration extends Config {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> showCurrentServer = new ConfigProperty<>(true);

  public DiscordSubConfiguration() {
    Setting.addModuleListener(this.enabled, RPCModule.class);
  }

  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> getShowCurrentServer() {
    return this.showCurrentServer;
  }
}
