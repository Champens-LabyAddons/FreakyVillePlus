package dk.fvtrademarket.fvplus.core.configuration;

import dk.fvtrademarket.fvplus.core.configuration.prison.PrisonSkillConfiguration;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.settings.annotation.SettingSection;

public class PrisonSubConfiguration extends Config {

  @SettingSection("shared")
  private final PrisonSkillConfiguration skillConfiguration = new PrisonSkillConfiguration();

  public PrisonSkillConfiguration getSkillConfiguration() {
    return this.skillConfiguration;
  }

}
