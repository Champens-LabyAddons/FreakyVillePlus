package dk.fvtrademarket.fvplus.core;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import dk.fvtrademarket.fvplus.core.configuration.DiscordSubConfiguration;
import dk.fvtrademarket.fvplus.core.configuration.PrisonSubConfiguration;
import dk.fvtrademarket.fvplus.core.configuration.SessionSubConfiguration;

@ConfigName("settings")
@SpriteTexture("settings/icons.png")
public class FreakyVillePlusConfiguration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  private final PrisonSubConfiguration prisonSubSettings = new PrisonSubConfiguration();

  private final DiscordSubConfiguration discordSubSettings = new DiscordSubConfiguration();

  //private final SessionSubConfiguration sessionSubSettings = new SessionSubConfiguration();

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public PrisonSubConfiguration getPrisonSubSettings() {
    return this.prisonSubSettings;
  }

  public DiscordSubConfiguration getDiscordSubSettings() {
    return this.discordSubSettings;
  }

  //public SessionSubConfiguration getSessionSubSettings() {
    //return this.sessionSubSettings;
  //}
}
