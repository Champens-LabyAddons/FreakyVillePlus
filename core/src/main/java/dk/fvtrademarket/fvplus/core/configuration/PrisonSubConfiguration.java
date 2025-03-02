package dk.fvtrademarket.fvplus.core.configuration;

import dk.fvtrademarket.fvplus.core.configuration.prison.PrisonSkillConfiguration;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class PrisonSubConfiguration extends Config {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  private final PrisonSkillConfiguration skillConfiguration = new PrisonSkillConfiguration();

  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public PrisonSkillConfiguration getSkillConfiguration() {
    return this.skillConfiguration;
  }

}
