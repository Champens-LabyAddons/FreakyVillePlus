package dk.fvtrademarket.fvplus.core.listeners;

import dk.fvtrademarket.fvplus.api.FreakyVillePlus;
import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.enums.SkillType;
import dk.fvtrademarket.fvplus.api.event.prison.gangarea.GangAreaFinishEvent;
import dk.fvtrademarket.fvplus.api.event.prison.gangarea.GangAreaTryEvent;
import dk.fvtrademarket.fvplus.api.event.prison.guardvault.GuardVaultFinishEvent;
import dk.fvtrademarket.fvplus.api.event.prison.guardvault.GuardVaultTryEvent;
import dk.fvtrademarket.fvplus.api.event.prison.guardvault.GuardVaultUpdateEvent;
import dk.fvtrademarket.fvplus.api.event.housing.LivingAreaLookupEvent;
import dk.fvtrademarket.fvplus.api.event.messaging.MessageRecognizedEvent;
import dk.fvtrademarket.fvplus.api.event.prison.skill.SkillExperienceGainEvent;
import dk.fvtrademarket.fvplus.api.event.prison.skill.SkillLevelUpEvent;
import dk.fvtrademarket.fvplus.api.service.skill.SkillService;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.util.Messaging;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.chat.ChatMessage;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.util.I18n;
import net.labymod.api.util.logging.Logging;
import java.util.regex.Matcher;

public class MessageRecognizedListener {

  private final Logging logging = Logging.create(this.getClass());

  private final ClientInfo clientInfo;
  private final ChatExecutor chatExecutor;

  public MessageRecognizedListener(ClientInfo clientInfo, ChatExecutor chatExecutor) {
    this.clientInfo = clientInfo;
    this.chatExecutor = chatExecutor;
  }

  @Subscribe
  public void onMessageRecognized(MessageRecognizedEvent event) {
    if (!this.clientInfo.isOnFreakyVille()) {
      return;
    }
    switch (event.getType()) {
      case LIVING_AREA_LOOKUP -> livingAreaLookup(event.getMatcher());
      case LIVING_AREA_HELP -> livingAreaHelpSendWaypoint(event.getMessage().orElse(null));

      case C_GUARD_VAULT_START -> guardVaultTry(PrisonSector.C, event.getMatcher());
      case B_GUARD_VAULT_START -> guardVaultTry(PrisonSector.B, event.getMatcher());
      case B_PLUS_GUARD_VAULT_START -> guardVaultTry(PrisonSector.B_PLUS, event.getMatcher());
      case A_GUARD_VAULT_START -> guardVaultTry(PrisonSector.A, event.getMatcher());
      case A_PLUS_GUARD_VAULT_START -> guardVaultTry(PrisonSector.A_PLUS, event.getMatcher());

      case C_GUARD_VAULT_UPDATE -> guardVaultUpdate(PrisonSector.C, event.getMatcher());
      case B_GUARD_VAULT_UPDATE -> guardVaultUpdate(PrisonSector.B, event.getMatcher());
      case B_PLUS_GUARD_VAULT_UPDATE -> guardVaultUpdate(PrisonSector.B_PLUS, event.getMatcher());
      case A_GUARD_VAULT_UPDATE -> guardVaultUpdate(PrisonSector.A, event.getMatcher());
      case A_PLUS_GUARD_VAULT_UPDATE -> guardVaultUpdate(PrisonSector.A_PLUS, event.getMatcher());

      case B_PLUS_GUARD_VAULT_FINISH -> guardVaultFinish(PrisonSector.B_PLUS, event.getMatcher());
      case A_GUARD_VAULT_FINISH -> guardVaultFinish(PrisonSector.A, event.getMatcher());
      case A_PLUS_GUARD_VAULT_FINISH -> guardVaultFinish(PrisonSector.A_PLUS, event.getMatcher());

      case GANG_AREA_TRY_UNSPECIFIED -> gangAreaTryUnspecified(event.getMatcher());
      case GANG_AREA_TRY_B -> gangAreaTrySpecified(PrisonSector.B, event.getMatcher());
      case GANG_AREA_TRY_B_PLUS -> gangAreaTrySpecified(PrisonSector.B_PLUS, event.getMatcher());
      case GANG_AREA_TRY_A_PLUS -> gangAreaTrySpecified(PrisonSector.A_PLUS, event.getMatcher());

      case GANG_AREA_UPDATE -> {
        try {
          gangAreaUpdate(event.getMatcher());
        } catch (NotImplementedException e) {
          this.logging.error("Failed to handle the GangAreaUpdateEvent", e);
        }
      }

      case GANG_AREA_FINISH_UNSPECIFIED -> gangAreaFinishUnspecified(event.getMatcher());
      case GANG_AREA_FINISH_B_PLUS -> gangAreaFinishSpecified(PrisonSector.B_PLUS, event.getMatcher());
      case GANG_AREA_FINISH_A_PLUS -> gangAreaFinishSpecified(PrisonSector.A_PLUS, event.getMatcher());

      case SKILL_EXPERIENCE_GAIN_GENERIC -> skillExperienceGainGeneric(event.getMatcher());
      case SKILL_EXPERIENCE_GAIN_FISHING_SCROLL -> skillExperienceGainXpScroll(SkillType.FISHING, event.getMatcher());
      case SKILL_EXPERIENCE_GAIN_RESPECT_SCROLL -> skillExperienceGainXpScroll(SkillType.RESPECT, event.getMatcher());

      case SKILL_LEVEL_UP -> skillLevelUp(event.getMatcher());

      case SKILL_INFO_LOOKUP -> skillInfoLookup(event.getMatcher());

      case PRISON_CHECK -> prisonCheck(event.getMatcher());

      case UNRECOGNIZED -> unrecognizedMessage(event.getMatcher());
    }
  }

  private void livingAreaLookup(Matcher matcher) {
    String areaIdentifier = matcher.group(1);
    Laby.fireEvent(new LivingAreaLookupEvent(areaIdentifier));
  }

  private void guardVaultTry(PrisonSector sector, Matcher matcher) {
    String robber = matcher.group(1);
    Laby.fireEvent(new GuardVaultTryEvent(sector, robber));
  }

  private void guardVaultUpdate(PrisonSector sector, Matcher matcher) {
    if (sector == PrisonSector.A_PLUS || sector == PrisonSector.B_PLUS) {
      PrisonSector currentSector = this.clientInfo.getPrisonSector().orElse(null);
      if (currentSector == null) {
        this.logging.error("Failed to get the current prison sector");
        return;
      }
      if (currentSector == PrisonSector.A && sector != PrisonSector.A_PLUS) {
        sector = PrisonSector.A_PLUS;
      }
      if (currentSector == PrisonSector.B && sector != PrisonSector.B_PLUS) {
        sector = PrisonSector.B_PLUS;
      }
    }
    byte hours = extractNumber(matcher, 1);
    byte minutes = extractNumber(matcher, 2);
    byte seconds = extractNumber(matcher, 3);
    Laby.fireEvent(new GuardVaultUpdateEvent(sector, hours, minutes, seconds));
  }

  private void guardVaultFinish(PrisonSector sector, Matcher matcher) {
    String player = matcher.group(1);
    Laby.fireEvent(new GuardVaultFinishEvent(sector, player, true));
  }

  private void gangAreaTryUnspecified(Matcher matcher) {
    String takerName = matcher.group(1);
    PrisonSector sector = this.clientInfo.getPrisonSector().orElse(null);
    if (sector == null) {
      this.logging.error("Failed to get the current prison sector");
      return;
    }
    Laby.fireEvent(new GangAreaTryEvent(sector, takerName));
  }

  private void gangAreaTrySpecified(PrisonSector sector, Matcher matcher) {
    String takerName = matcher.group(1);
    Laby.fireEvent(new GangAreaTryEvent(sector, takerName));
  }

  private void gangAreaUpdate(Matcher matcher) throws NotImplementedException {
    throw new NotImplementedException("The GangAreaUpdateEvent is not supported by the client in this version");
  }

  private void gangAreaFinishUnspecified(Matcher matcher) {
    String takerName = matcher.group(1);
    PrisonSector sector = this.clientInfo.getPrisonSector().orElse(null);
    if (sector == null) {
      this.logging.error("Failed to get the current prison sector");
      return;
    }
    Laby.fireEvent(new GangAreaFinishEvent(sector, takerName, true));
  }

  private void gangAreaFinishSpecified(PrisonSector sector, Matcher matcher) {
    String takerName = matcher.group(1);
    Laby.fireEvent(new GangAreaFinishEvent(sector, takerName, true));
  }

  private void skillExperienceGainGeneric(Matcher matcher) {
    double experience = Double.parseDouble(matcher.group(1));
    SkillType type = SkillType.fromString(matcher.group(2).toUpperCase());
    boolean treasureDrop = matcher.group(3) != null;
    PrisonSector sector = this.clientInfo.getPrisonSector().orElse(null);
    if (sector == null) {
      this.logging.error("Failed to get the current prison sector");
      return;
    }
    Laby.fireEvent(new SkillExperienceGainEvent(sector, experience, type, false, treasureDrop));
  }

  private void skillExperienceGainXpScroll(SkillType skillType, Matcher matcher) {
    double experience = Double.parseDouble(matcher.group(1));
    PrisonSector sector = this.clientInfo.getPrisonSector().orElse(null);
    if (sector == null) {
      this.logging.error("Failed to get the current prison sector");
      return;
    }
    Laby.fireEvent(new SkillExperienceGainEvent(sector, experience, skillType, true, false));
  }

  private void skillLevelUp(Matcher matcher) {
    byte newLevel = Byte.parseByte(matcher.group(1));
    SkillType type = SkillType.fromString(matcher.group(2));
    PrisonSector sector = this.clientInfo.getPrisonSector().orElse(null);
    if (sector == null) {
      this.logging.error("Failed to get the current prison sector");
      return;
    }
    Laby.fireEvent(new SkillLevelUpEvent(sector, type, newLevel));
  }

  private void skillInfoLookup(Matcher matcher) {
    SkillType type = SkillType.fromString(matcher.group(1));
    byte level = Byte.parseByte(matcher.group(2));
    SkillService skillService = FreakyVillePlus.getReferences().skillService();
    PrisonSector sector = this.clientInfo.getPrisonSector().orElse(null);
    if (sector == null) {
      this.logging.error("Failed to get the current prison sector");
      return;
    }
    if (level == skillService.getMaxLevel(sector, type)) {
      skillService.updateRequirement(type, -1);
      skillService.updateLevel(type, level);
      return;
    }
    double experience = Double.parseDouble(matcher.group(4));
    double requirement = Double.parseDouble(matcher.group(5));
    skillService.updateExperience(type, experience);
    skillService.updateRequirement(type, requirement);
    skillService.updateLevel(type, level);
  }

  private void livingAreaHelpSendWaypoint(ChatMessage message) {
    if (message == null) {
      throw new NullPointerException("The provided ChatMessage is null");
    }
    markedResend(message, "§", NamedTextColor.GRAY);
    this.chatExecutor.displayClientMessage(
        Component.translatable("fvplus.server.prison.cell.commands.waypoint.description", NamedTextColor.WHITE,
            Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("/ce waypoint <celleID>", NamedTextColor.RED))
                .append(Component.text("]", NamedTextColor.DARK_GRAY))
        )
    );
  }

  private void prisonCheck(Matcher matcher) {
    if (this.clientInfo.getCurrentServer() != FreakyVilleServer.PRISON) {
      return;
    }
    if (this.clientInfo.getPrisonSector().isPresent()) {
      return;
    }
    try {
      this.clientInfo.setPrisonSector(PrisonSector.valueOf(matcher.group(1).toUpperCase()));
    } catch (IllegalArgumentException e) {
      this.clientInfo.setPrisonSector(null);
      this.logging.error(I18n.translate("fvplus.logging.error.findingPrison"), e);
      Messaging.displayTranslatable("fvplus.logging.error.findingPrison", NamedTextColor.RED);
    }
  }

  private void unrecognizedMessage(Matcher matcher) {
    this.logging.warn("The following chat-message is registered but is not supported by the client in this version, is the addon up-to-date?: " + matcher.group());
  }

  private void markedResend(ChatMessage message, String addition, TextColor color) {
    Component marked = Component.text(addition, color);
    this.chatExecutor.displayClientMessage(
        marked.append(message.component())
    );
  }

  private byte extractNumber(Matcher matcher, int group) {
    byte num = 0;
    if (matcher.group(group) == null) {
      return num;
    }
    StringBuilder numbers = new StringBuilder();
    for (int i = 0; i < matcher.group(group).length(); i++) {
      if (!Character.isDigit(matcher.group(group).charAt(i))) {
        continue;
      }
      numbers.append(matcher.group(group).charAt(i));
    }
    if (!numbers.isEmpty()) {
      try {
        num = Byte.parseByte(numbers.toString());
      } catch (NumberFormatException e) {
        this.logging.error("Failed to parse number from string: " + matcher.group(group));
      }
    }
    return num;
  }
}
