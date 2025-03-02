package dk.fvtrademarket.fvplus.core.gui.activity;

import dk.fvtrademarket.fvplus.core.skill.SkillProfile;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.widget.SimpleWidget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;

@AutoWidget
public class SkillProfileWidget extends SimpleWidget {

  private String userName;
  private SkillProfile skillProfile;

  public SkillProfileWidget(String userName, SkillProfile skillProfile) {
    this.userName = userName;
    this.skillProfile = skillProfile;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    IconWidget iconWidget = new IconWidget(this.getIconWidget(this.userName));
    iconWidget.addId("avatar");
    this.addChild(iconWidget);

    ComponentWidget nameWidget = ComponentWidget.component(Component.text(this.userName));
    nameWidget.addId("name");
    this.addChild(nameWidget);

    ComponentWidget sectorNameWidget = ComponentWidget.component(this.skillProfile.getSector().toComponent());
    sectorNameWidget.addId("prison-sector");
    this.addChild(sectorNameWidget);
  }

  public Icon getIconWidget(String userName) {
    return Icon.head(userName.length() == 0 ? "MHF_Question" : userName);
  }

  public String getUserName() {
    return this.userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public SkillProfile getSkillProfile() {
    return this.skillProfile;
  }

  public void setSkillProfile(SkillProfile skillProfile) {
    this.skillProfile = skillProfile;
  }
}
