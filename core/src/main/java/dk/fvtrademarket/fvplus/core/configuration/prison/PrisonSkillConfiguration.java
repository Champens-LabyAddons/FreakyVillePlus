package dk.fvtrademarket.fvplus.core.configuration.prison;

import dk.fvtrademarket.fvplus.core.gui.activity.SkillProfileActivity;
import dk.fvtrademarket.fvplus.core.skill.SkillProfile;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.widgets.activity.settings.ActivitySettingWidget.ActivitySetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.Exclude;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.MethodOrder;
import java.util.HashMap;
import java.util.Map;

public class PrisonSkillConfiguration extends Config {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> experienceActionbar = new ConfigProperty<>(true);

  @Exclude
  private Map<String, SkillProfile> skillProfiles = new HashMap<>();

  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> getExperienceActionbar() {
    return this.experienceActionbar;
  }

  @MethodOrder(after = "experienceActionbar")
  @SettingSection("profiles")
  @ActivitySetting
  public Activity openSkillProfiles() {
    return new SkillProfileActivity();
  }

  public Map<String, SkillProfile> getSkillProfiles() {
    return this.skillProfiles;
  }
}
