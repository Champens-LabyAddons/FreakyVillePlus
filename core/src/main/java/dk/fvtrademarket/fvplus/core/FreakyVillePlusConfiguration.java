package dk.fvtrademarket.fvplus.core;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.Exclude;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import dk.fvtrademarket.fvplus.core.configuration.DiscordSubConfiguration;
import dk.fvtrademarket.fvplus.core.configuration.PrisonSubConfiguration;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import java.util.ArrayList;
import java.util.List;

@ConfigName("settings")
@SpriteTexture("settings/icons.png")
public class FreakyVillePlusConfiguration extends AddonConfig {

  @SettingSection("general")
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SettingSection("servers")
  private final PrisonSubConfiguration prisonSubSettings = new PrisonSubConfiguration();

  @SettingSection("misc")
  private final DiscordSubConfiguration discordSubSettings = new DiscordSubConfiguration();

  @Exclude
  private final List<String> ignoredPlayers = new ArrayList<>();

  @Exclude
  private final List<String> blockedPlayers = new ArrayList<>();

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

  public List<String> getIgnoredPlayers() {
    return this.ignoredPlayers;
  }

  public List<String> getBlockedPlayers() {
    return this.blockedPlayers;
  }
}
