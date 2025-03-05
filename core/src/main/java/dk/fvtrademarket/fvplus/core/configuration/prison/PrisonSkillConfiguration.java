package dk.fvtrademarket.fvplus.core.configuration.prison;

import dk.fvtrademarket.fvplus.core.gui.activity.SkillProfileActivity;
import dk.fvtrademarket.fvplus.core.skill.SkillProfile;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.widgets.activity.settings.ActivitySettingWidget.ActivitySetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.Exclude;
import net.labymod.api.configuration.loader.annotation.ShowSettingInParent;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.MethodOrder;
import net.labymod.api.util.Pair;
import java.util.HashMap;
import java.util.Map;

public class PrisonSkillConfiguration extends Config {

  @ShowSettingInParent
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SettingSection("general")
  @SwitchSetting
  private final ConfigProperty<Boolean> hideExperienceMessages = new ConfigProperty<>(false);

  @SettingSection("experienceBar")
  @SwitchSetting
  private final ConfigProperty<Boolean> experienceActionbar = new ConfigProperty<>(true);

  @DropdownSetting
  private final ConfigProperty<ColourProfile> colourProfile = new ConfigProperty<>(ColourProfile.AQUA);

  @Exclude
  private Map<String, SkillProfile> skillProfiles = new HashMap<>();

  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> hideExperienceMessages() {
    return this.hideExperienceMessages;
  }

  public ConfigProperty<Boolean> experienceActionbar() {
    return this.experienceActionbar;
  }

  public ConfigProperty<ColourProfile> colourProfile() {
    return this.colourProfile;
  }

  @MethodOrder(after = "colourProfile")
  @SettingSection("profiles")
  @ActivitySetting
  public Activity openSkillProfiles() {
    return new SkillProfileActivity();
  }

  public Map<String, SkillProfile> getSkillProfiles() {
    return this.skillProfiles;
  }

  public enum ColourProfile {
    AQUA(Pair.of(NamedTextColor.AQUA, NamedTextColor.DARK_AQUA)),
    BLUE(Pair.of(NamedTextColor.BLUE, NamedTextColor.DARK_BLUE)),
    GREEN(Pair.of(NamedTextColor.GREEN, NamedTextColor.DARK_GREEN)),
    RED(Pair.of(NamedTextColor.RED, NamedTextColor.DARK_RED)),
    YELLOW(Pair.of(NamedTextColor.YELLOW, NamedTextColor.GOLD)),
    PURPLE(Pair.of(NamedTextColor.LIGHT_PURPLE, NamedTextColor.DARK_PURPLE)),
    ;

    private final Pair<TextColor, TextColor> colours;

    ColourProfile(Pair<TextColor, TextColor> colours) {
      this.colours = colours;
    }

    public Pair<TextColor, TextColor> getColours() {
      return this.colours;
    }
  }
}
