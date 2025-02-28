package dk.fvtrademarket.fvplus.core.listeners;

import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.event.prison.gangarea.GangAreaFinishEvent;
import dk.fvtrademarket.fvplus.api.event.prison.gangarea.GangAreaTryEvent;
import dk.fvtrademarket.fvplus.api.event.prison.guardvault.GuardVaultFinishEvent;
import dk.fvtrademarket.fvplus.api.event.prison.guardvault.GuardVaultTryEvent;
import dk.fvtrademarket.fvplus.api.event.prison.guardvault.GuardVaultUpdateEvent;
import dk.fvtrademarket.fvplus.api.event.housing.LivingAreaLookupEvent;
import dk.fvtrademarket.fvplus.api.event.messaging.MessageRecognizedEvent;
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

      case GANG_AREA_TRY_STANDARD -> gangAreaTryStandard(event.getMatcher());
      case GANG_AREA_TRY_ADVANCED -> gangAreaTryAdvanced(event.getMatcher());

      case GANG_AREA_UPDATE -> {
        try {
          gangAreaUpdate(event.getMatcher());
        } catch (NotImplementedException e) {
          this.logging.error("Failed to handle the GangAreaUpdateEvent", e);
        }
      }

      case GANG_AREA_FINISH_STANDARD -> gangAreaFinishStandard(event.getMatcher());
      case GANG_AREA_FINISH_ADVANCED -> gangAreaFinishAdvanced(event.getMatcher());

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

  private void gangAreaTryStandard(Matcher matcher) {
    String takerName = matcher.group(1);
    PrisonSector sector = this.clientInfo.getPrisonSector().orElse(null);
    if (sector == null) {
      this.logging.error("Failed to get the current prison sector");
      return;
    }
    Laby.fireEvent(new GangAreaTryEvent(sector, takerName));
  }

  private void gangAreaTryAdvanced(Matcher matcher) {
    String takerName = matcher.group(1);
    PrisonSector sector = this.clientInfo.getPrisonSector().orElse(null);
    if (sector == null) {
      this.logging.error("Failed to get the current prison sector");
      return;
    } else if (sector == PrisonSector.A) {
      sector = PrisonSector.A_PLUS;
    } else if (sector == PrisonSector.B) {
      sector = PrisonSector.B_PLUS;
    } else {
      return;
    }
    Laby.fireEvent(new GangAreaTryEvent(sector, takerName));
  }

  private void gangAreaUpdate(Matcher matcher) throws NotImplementedException {
    throw new NotImplementedException("The GangAreaUpdateEvent is not supported by the client in this version");
  }

  private void gangAreaFinishStandard(Matcher matcher) {
    String takerName = matcher.group(1);
    PrisonSector sector = this.clientInfo.getPrisonSector().orElse(null);
    if (sector == null) {
      this.logging.error("Failed to get the current prison sector");
      return;
    }
    Laby.fireEvent(new GangAreaFinishEvent(sector, takerName, true));
  }

  private void gangAreaFinishAdvanced(Matcher matcher) {
    String takerName = matcher.group(1);
    PrisonSector sector = this.clientInfo.getPrisonSector().orElse(null);
    if (sector == null) {
      this.logging.error("Failed to get the current prison sector");
      return;
    } else if (sector == PrisonSector.A) {
      sector = PrisonSector.A_PLUS;
    } else if (sector == PrisonSector.B) {
      sector = PrisonSector.B_PLUS;
    } else {
      return;
    }
    Laby.fireEvent(new GangAreaFinishEvent(sector, takerName, true));
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
