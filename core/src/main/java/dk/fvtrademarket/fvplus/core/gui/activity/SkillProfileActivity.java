package dk.fvtrademarket.fvplus.core.gui.activity;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.core.FreakyVilleAddon;
import dk.fvtrademarket.fvplus.core.skill.SkillProfile;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.key.InputType;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.key.MouseButton;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.DivWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@AutoActivity
@Link("manage.lss")
@Link("overview.lss")
public class SkillProfileActivity extends Activity {

  private static final Pattern NAME_PATTERN = Pattern.compile("[\\w]{0,16}");

  private final FreakyVilleAddon addon;
  private final VerticalListWidget<SkillProfileWidget> skillProfileList;
  private final Map<String, SkillProfileWidget> skillProfileWidgets;

  private SkillProfileWidget selectedSkillProfile;

  private ButtonWidget removeButton;
  private ButtonWidget editButton;

  private FlexibleContentWidget inputWidget;
  private String lastUserName;

  private Action action;

  public SkillProfileActivity() {
    this.addon = FreakyVilleAddon.get();

    this.skillProfileWidgets = new HashMap<>();
    this.addon.configuration().getPrisonSubSettings().getSkillConfiguration().getSkillProfiles().forEach((userName, skillProfile) -> {
      this.skillProfileWidgets.put(userName, new SkillProfileWidget(userName, skillProfile));
    });

    this.skillProfileList = new VerticalListWidget<>();
    this.skillProfileList.addId("skill-profile-list");
    this.skillProfileList.setSelectCallback(skillProfileWidget -> {
      SkillProfileWidget selectedSkillProfile = this.skillProfileList.listSession().getSelectedEntry();
      if (selectedSkillProfile == null
          || selectedSkillProfile.getSkillProfile() != skillProfileWidget.getSkillProfile()) {
        this.editButton.setEnabled(true);
        this.removeButton.setEnabled(true);
      }
    });

    this.skillProfileList.setDoubleClickCallback(skillProfileWidget -> this.setAction(Action.EDIT));
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    FlexibleContentWidget container = new FlexibleContentWidget();
    container.addId("skill-profile-container");
    for (SkillProfileWidget skillProfileWidget : this.skillProfileWidgets.values()) {
      this.skillProfileList.addChild(skillProfileWidget);
    }

    container.addFlexibleContent(new ScrollWidget(this.skillProfileList));

    this.selectedSkillProfile = this.skillProfileList.listSession().getSelectedEntry();
    HorizontalListWidget menu = new HorizontalListWidget();
    menu.addId("overview-button-menu");

    menu.addEntry(ButtonWidget.i18n("labymod.ui.button.add", () -> this.setAction(Action.ADD)));

    this.editButton = ButtonWidget.i18n("labymod.ui.button.edit",
        () -> this.setAction(Action.EDIT));
    this.editButton.setEnabled(this.selectedSkillProfile != null);
    menu.addEntry(this.editButton);

    this.removeButton = ButtonWidget.i18n("labymod.ui.button.remove",
        () -> this.setAction(Action.REMOVE));
    this.removeButton.setEnabled(this.selectedSkillProfile != null);
    menu.addEntry(this.removeButton);

    container.addContent(menu);
    this.document().addChild(container);
    if (this.action == null) {
      return;
    }

    DivWidget manageContainer = new DivWidget();
    manageContainer.addId("manage-container");

    Widget overlayWidget;
    switch (this.action) {
      default:
      case ADD:
        SkillProfileWidget newSkillProfile = new SkillProfileWidget("", SkillProfile.createDefault());
        overlayWidget = this.initializeManageContainer(newSkillProfile);
        break;
      case EDIT:
        overlayWidget = this.initializeManageContainer(this.selectedSkillProfile);
        break;
      case REMOVE:
        overlayWidget = this.initializeRemoveContainer(this.selectedSkillProfile);
        break;
    }

    manageContainer.addChild(overlayWidget);
    this.document().addChild(manageContainer);
  }

  private FlexibleContentWidget initializeRemoveContainer(SkillProfileWidget skillProfileWidget) {
    this.inputWidget = new FlexibleContentWidget();
    this.inputWidget.addId("remove-container");

    ComponentWidget confirmationWidget = ComponentWidget.i18n(
        "fvplus.activity.prison.skill.manage.remove");
    confirmationWidget.addId("remove-confirmation");
    this.inputWidget.addContent(confirmationWidget);

    SkillProfileWidget previewWidget = new SkillProfileWidget(skillProfileWidget.getUserName(),
        selectedSkillProfile.getSkillProfile());
    previewWidget.addId("remove-preview");
    this.inputWidget.addContent(previewWidget);

    HorizontalListWidget menu = new HorizontalListWidget();
    menu.addId("remove-button-menu");

    menu.addEntry(ButtonWidget.i18n("labymod.ui.button.remove", () -> {
      this.addon.configuration().getPrisonSubSettings().getSkillConfiguration().getSkillProfiles().remove(skillProfileWidget.getUserName());
      this.skillProfileWidgets.remove(skillProfileWidget.getUserName());
      this.skillProfileList.listSession().setSelectedEntry(null);
      this.setAction(null);
    }));

    menu.addEntry(ButtonWidget.i18n("labymod.ui.button.cancel", () -> this.setAction(null)));
    this.inputWidget.addContent(menu);

    return this.inputWidget;
  }

  private DivWidget initializeManageContainer(SkillProfileWidget skillProfileWidget) {
    ButtonWidget doneButton = ButtonWidget.i18n("labymod.ui.button.done");

    DivWidget inputContainer = new DivWidget();
    inputContainer.addId("input-container");

    this.inputWidget = new FlexibleContentWidget();
    this.inputWidget.addId("input-list");

    ComponentWidget labelName = ComponentWidget.i18n("fvplus.activity.prison.skill.playerName");
    labelName.addId("label-name");
    this.inputWidget.addContent(labelName);

    HorizontalListWidget nameList = new HorizontalListWidget();
    nameList.addId("input-name-list");

    IconWidget iconWidget = new IconWidget(
        skillProfileWidget.getIconWidget(skillProfileWidget.getUserName()));
    iconWidget.addId("input-avatar");
    nameList.addEntry(iconWidget);

    TextFieldWidget nameTextField = new TextFieldWidget();
    nameTextField.maximalLength(16);
    nameTextField.setText(skillProfileWidget.getUserName());
    nameTextField.validator(newValue -> NAME_PATTERN.matcher(newValue).matches());
    nameTextField.updateListener(newValue -> {
      doneButton.setEnabled(!newValue.trim().isEmpty());
      if (newValue.equals(this.lastUserName)) {
        return;
      }

      this.lastUserName = newValue;
      iconWidget.icon().set(skillProfileWidget.getIconWidget(newValue));
    });

    nameList.addEntry(nameTextField);
    this.inputWidget.addContent(nameList);

    HorizontalListWidget checkBoxList = new HorizontalListWidget();
    checkBoxList.addId("dropdown-list");

    DivWidget dropdownContainer = new DivWidget();
    dropdownContainer.addId("dropdown-container");

    ComponentWidget dropdownText = ComponentWidget.i18n("fvplus.activity.prison.skill.sector");
    dropdownText.addId("dropdown-name");
    dropdownContainer.addChild(dropdownText);

    DropdownWidget<PrisonSector> sectorDropdown = new DropdownWidget<>();
    sectorDropdown.add(PrisonSector.A);
    sectorDropdown.add(PrisonSector.B);
    sectorDropdown.add(PrisonSector.C);
    sectorDropdown.addId("dropdown-item");
    sectorDropdown.setSelected(skillProfileWidget.getSkillProfile().getSector());
    sectorDropdown.setChangeListener(sector -> {

    });
    dropdownContainer.addChild(sectorDropdown);

    checkBoxList.addEntry(dropdownContainer);

    this.inputWidget.addContent(checkBoxList);

    HorizontalListWidget miningList = new HorizontalListWidget();
    miningList.addId("mining-list");

    DivWidget miningLevelContainer = new DivWidget();
    miningLevelContainer.addId("mining-div");

    ComponentWidget miningLevelText = ComponentWidget.i18n("fvplus.activity.prison.skill.mining.level");
    miningLevelText.addId("mining-name");
    miningLevelContainer.addChild(miningLevelText);

    SliderWidget miningLevelSlider = new SliderWidget()
        .addId("mining-level-slider");
    miningLevelSlider.range(0, 10);
    miningLevelSlider.setValue(Math.max(0, skillProfileWidget.getSkillProfile().getMiningLevel()));
    miningLevelContainer.addChild(miningLevelSlider);

    miningList.addEntry(miningLevelContainer);

    DivWidget miningExperienceContainer = new DivWidget();
    miningExperienceContainer.addId("mining-div");

    ComponentWidget miningExperienceText = ComponentWidget.i18n("fvplus.activity.prison.skill.mining.experience")
        .addId("mining-name");
    miningExperienceContainer.addChild(miningExperienceText);

    TextFieldWidget miningExperienceTextField = new TextFieldWidget();
    miningExperienceTextField.addId("mining-experience-text-field");
    miningExperienceTextField.setText(String.valueOf(skillProfileWidget.getSkillProfile().getMiningExperience()));
    miningExperienceContainer.addChild(miningExperienceTextField);

    miningList.addEntry(miningExperienceContainer);

    this.inputWidget.addContent(miningList);

    HorizontalListWidget fishingList = new HorizontalListWidget();
    fishingList.addId("fishing-list");

    DivWidget fishingLevelContainer = new DivWidget();
    fishingLevelContainer.addId("fishing-div");

    ComponentWidget fishingLevelText = ComponentWidget.i18n("fvplus.activity.prison.skill.fishing.level");
    fishingLevelText.addId("fishing-name");
    fishingLevelContainer.addChild(fishingLevelText);

    SliderWidget fishingLevelSlider = new SliderWidget();
    fishingLevelSlider.addId("fishing-level-slider");
    fishingLevelSlider.range(0, 10);
    fishingLevelSlider.setValue(skillProfileWidget.getSkillProfile().getFishingLevel());
    fishingLevelContainer.addChild(fishingLevelSlider);

    fishingList.addEntry(fishingLevelContainer);

    DivWidget fishingExperienceContainer = new DivWidget();
    fishingExperienceContainer.addId("fishing-div");

    ComponentWidget fishingExperienceText = ComponentWidget.i18n("fvplus.activity.prison.skill.fishing.experience");
    fishingExperienceText.addId("fishing-name");
    fishingExperienceContainer.addChild(fishingExperienceText);

    TextFieldWidget fishingExperienceTextField = new TextFieldWidget();
    fishingExperienceTextField.addId("fishing-experience-text-field");
    fishingExperienceTextField.setText(String.valueOf(skillProfileWidget.getSkillProfile().getFishingExperience()));
    fishingExperienceContainer.addChild(fishingExperienceTextField);

    fishingList.addEntry(fishingExperienceContainer);

    this.inputWidget.addContent(fishingList);

    HorizontalListWidget respectList = new HorizontalListWidget();
    respectList.addId("respect-list");

    DivWidget respectLevelContainer = new DivWidget();
    respectLevelContainer.addId("respect-div");

    ComponentWidget respectLevelText = ComponentWidget.i18n("fvplus.activity.prison.skill.respect.level");
    respectLevelText.addId("respect-name");
    respectLevelContainer.addChild(respectLevelText);

    SliderWidget respectLevelSlider = new SliderWidget();
    respectLevelSlider.addId("respect-level-slider");
    respectLevelSlider.range(0, 10);
    respectLevelSlider.setValue(skillProfileWidget.getSkillProfile().getRespectLevel());
    respectLevelContainer.addChild(respectLevelSlider);

    respectList.addEntry(respectLevelContainer);

    DivWidget respectExperienceContainer = new DivWidget();
    respectExperienceContainer.addId("respect-div");

    ComponentWidget respectExperienceText = ComponentWidget.i18n("fvplus.activity.prison.skill.respect.experience");
    respectExperienceText.addId("respect-name");
    respectExperienceContainer.addChild(respectExperienceText);

    TextFieldWidget respectExperienceTextField = new TextFieldWidget();
    respectExperienceTextField.addId("respect-experience-text-field");
    respectExperienceTextField.setText(String.valueOf(skillProfileWidget.getSkillProfile().getRespectExperience()));
    respectExperienceContainer.addChild(respectExperienceTextField);

    respectList.addEntry(respectExperienceContainer);

    this.inputWidget.addContent(respectList);


    HorizontalListWidget buttonList = new HorizontalListWidget();
    buttonList.addId("edit-button-menu");

    doneButton.setEnabled(!nameTextField.getText().trim().isEmpty());
    doneButton.setPressable(() -> {
      if (skillProfileWidget.getUserName().length() == 0) {
        this.skillProfileWidgets.put(nameTextField.getText(), skillProfileWidget);
        this.skillProfileList.listSession().setSelectedEntry(skillProfileWidget);
      }

      this.addon.configuration().getPrisonSubSettings().getSkillConfiguration().getSkillProfiles().remove(skillProfileWidget.getUserName());
      SkillProfile skillProfile = skillProfileWidget.getSkillProfile();
      skillProfile.setSector(sectorDropdown.getSelected());
      skillProfile.setMiningLevel((int) miningLevelSlider.getValue());
      skillProfile.setMiningExperience(Double.parseDouble(miningExperienceTextField.getText()));
      skillProfile.setFishingLevel((int) fishingLevelSlider.getValue());
      skillProfile.setFishingExperience(Double.parseDouble(fishingExperienceTextField.getText()));
      skillProfile.setRespectLevel((int) respectLevelSlider.getValue());
      skillProfile.setRespectExperience(Double.parseDouble(respectExperienceTextField.getText()));
      this.addon.configuration().getPrisonSubSettings().getSkillConfiguration().getSkillProfiles().put(nameTextField.getText(), skillProfile);

      skillProfileWidget.setUserName(nameTextField.getText());
      skillProfileWidget.setSkillProfile(skillProfile);
      this.setAction(null);
    });

    buttonList.addEntry(doneButton);

    buttonList.addEntry(ButtonWidget.i18n("labymod.ui.button.cancel", () -> this.setAction(null)));
    inputContainer.addChild(this.inputWidget);
    this.inputWidget.addContent(buttonList);
    return inputContainer;
  }

  @Override
  public boolean mouseClicked(MutableMouse mouse, MouseButton mouseButton) {
    try {
      if (this.action != null) {
        return this.inputWidget.mouseClicked(mouse, mouseButton);
      }

      return super.mouseClicked(mouse, mouseButton);
    } finally {
      this.selectedSkillProfile = this.skillProfileList.listSession().getSelectedEntry();
      this.removeButton.setEnabled(this.selectedSkillProfile != null);
      this.editButton.setEnabled(this.selectedSkillProfile != null);
    }
  }

  @Override
  public boolean keyPressed(Key key, InputType type) {
    if (key.getId() == 256 && this.action != null) {
      this.setAction(null);
      return true;
    }

    return super.keyPressed(key, type);
  }

  private void setAction(Action action) {
    this.action = action;
    this.reload();
  }

  @Override
  public void onCloseScreen() {
    super.onCloseScreen();
  }



  private enum Action {
    ADD, EDIT, REMOVE
  }
}
