package dk.fvtrademarket.fvplus.core.configuration;

import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

public class DiscordSubConfiguration extends SubConfiguration {

  @SettingSection("information")
  @SwitchSetting
  private final ConfigProperty<Boolean> showCurrentServer = new ConfigProperty<>(true);

  public ConfigProperty<Boolean> getShowCurrentServer() {
    return this.showCurrentServer;
  }
}
