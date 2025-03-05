package dk.fvtrademarket.fvplus.core.listeners.skill;

import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.enums.SkillType;
import dk.fvtrademarket.fvplus.api.event.prison.skill.SkillExperienceGainEvent;
import dk.fvtrademarket.fvplus.api.service.skill.SkillService;
import dk.fvtrademarket.fvplus.core.configuration.prison.PrisonSkillConfiguration;
import dk.fvtrademarket.fvplus.core.configuration.prison.PrisonSkillConfiguration.ColourProfile;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.skill.SkillProfile;
import dk.fvtrademarket.fvplus.core.util.Components;
import net.labymod.api.LabyAPI;
import net.labymod.api.client.chat.Title;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer;
import net.labymod.api.event.Subscribe;

public class SkillListener {

  private final ClientInfo clientInfo;
  private final LabyAPI labyAPI;
  private final PrisonSkillConfiguration prisonSkillConfiguration;
  private final SkillService skillService;

  public SkillListener(ClientInfo clientInfo, LabyAPI labyAPI, PrisonSkillConfiguration prisonSkillConfiguration, SkillService skillService) {
    this.clientInfo = clientInfo;
    this.labyAPI = labyAPI;
    this.prisonSkillConfiguration = prisonSkillConfiguration;
    this.skillService = skillService;
  }

  @Subscribe
  public void onSkillExperienceGainEvent(SkillExperienceGainEvent event) {
    if (!this.clientInfo.isOnFreakyVille()) {
      return;
    }
    if (this.clientInfo.getCurrentServer() != FreakyVilleServer.PRISON) {
      return;
    }
    if (!this.prisonSkillConfiguration.getSkillProfiles().containsKey(this.labyAPI.getName())) {
      return;
    }
    SkillProfile skillProfile = this.prisonSkillConfiguration.getSkillProfiles().get(this.labyAPI.getName());
    skillProfile.addExperience(event.getType(), event.getExperience());
    if (event.isTreasureDrop()) {
      Title treasureDropTitle = Title.builder()
          .title(Component.empty())
          .subTitle(treasureDropSubTitle(event.getType()))
          .fadeIn(20)
          .stay(60)
          .fadeOut(20)
          .build();
      this.labyAPI.minecraft().showTitle(treasureDropTitle);
    }
    PrisonSector sector = this.clientInfo.getPrisonSector().orElse(null);
    if (sector == null) {
      return;
    }
    this.skillService.increaseRecentGain(
      sector,
      event.getType(),
      event.getExperience()
    );
    if (!this.prisonSkillConfiguration.experienceActionbar().get()) {
      return;
    }
    ColourProfile colourProfile = this.prisonSkillConfiguration.colourProfile().get();
    Component gain = Component.text("+" + this.skillService.getRecentGain(sector, event.getType()), colourProfile.getColours().getFirst());
    double progress = getProgress(skillProfile, event.getType());
    double maxProgress = this.skillService.getExperienceRequirement(
        sector,
        event.getType(),
        getLevel(skillProfile, event.getType())
    );
    Component progressBar = Components.getProgressBar(progress, maxProgress, 10,
        colourProfile.getColours().getFirst(), colourProfile.getColours().getSecond(), NamedTextColor.DARK_GRAY);
    Component progressFraction = Components.getProgressFraction(progress, maxProgress,
        colourProfile.getColours().getFirst(), NamedTextColor.GRAY, colourProfile.getColours().getSecond());
    Component finalComponent = Component.text()
        .append(gain)
        .append(Component.space())
        .append(progressBar)
        .append(Component.space())
        .append(Component.text("[", NamedTextColor.DARK_GRAY))
        .append(progressFraction)
        .append(Component.text("]", NamedTextColor.DARK_GRAY))
        .build();
    this.labyAPI.minecraft().chatExecutor().displayActionBar(LegacyComponentSerializer.legacySection()
        .serialize(finalComponent));
  }

  private Component treasureDropSubTitle(SkillType skillType) {
    char skillTypeIcon = switch (skillType) {
      case MINING -> '❒';
      case FISHING -> '﹌';
      case RESPECT -> '✌';
    };
    return Component.text()
        .append(Component.text(skillTypeIcon, NamedTextColor.LIGHT_PURPLE))
        .append(Component.space())
        .append(Component.translatable("fvplus.server.prison.skill.treasureDrop", NamedTextColor.GOLD))
        .append(Component.space())
        .append(Component.text(skillTypeIcon, NamedTextColor.LIGHT_PURPLE))
        .build();
  }

  private double getProgress(SkillProfile skillProfile, SkillType type) {
    return switch (type) {
      case MINING -> skillProfile.getMiningExperience();
      case FISHING -> skillProfile.getFishingExperience();
      case RESPECT -> skillProfile.getRespectExperience();
    };
  }

  private int getLevel(SkillProfile skillProfile, SkillType type) {
    return switch (type) {
      case MINING -> skillProfile.getMiningLevel();
      case FISHING -> skillProfile.getFishingLevel();
      case RESPECT -> skillProfile.getRespectLevel();
    };
  }

}
