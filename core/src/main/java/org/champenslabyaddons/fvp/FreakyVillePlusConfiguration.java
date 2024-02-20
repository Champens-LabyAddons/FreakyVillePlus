package org.champenslabyaddons.fvp;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import org.champenslabyaddons.fvp.configuration.DiscordSubConfiguration;

@ConfigName("settings")
public class FreakyVillePlusConfiguration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  private final DiscordSubConfiguration discordSubSettings = new DiscordSubConfiguration();

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public DiscordSubConfiguration getDiscordSubSettings() {
    return this.discordSubSettings;
  }
}
