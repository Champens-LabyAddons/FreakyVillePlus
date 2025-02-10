package dk.fvtrademarket.fvplus.core.listeners;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.event.guardvault.GuardVaultFinishEvent;
import dk.fvtrademarket.fvplus.api.event.guardvault.GuardVaultTryEvent;
import dk.fvtrademarket.fvplus.api.event.guardvault.GuardVaultUpdateEvent;
import dk.fvtrademarket.fvplus.api.event.housing.LivingAreaLookupEvent;
import dk.fvtrademarket.fvplus.api.event.messaging.MessageRecognizedEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.chat.ChatMessage;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.util.logging.Logging;
import java.util.regex.Matcher;

public class MessageRecognizedListener {

  private final Logging logging = Logging.create(this.getClass());

  private final ChatExecutor chatExecutor;

  public MessageRecognizedListener(ChatExecutor chatExecutor) {
    this.chatExecutor = chatExecutor;
  }

  @Subscribe
  public void onMessageRecognized(MessageRecognizedEvent event) {
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

      case UNRECOGNIZED -> unrecognizedMessage(event.getMatcher());
    }
  }

  private void livingAreaLookup(Matcher matcher) {
    String areaIdentifier = "";
    Laby.fireEvent(new LivingAreaLookupEvent(areaIdentifier));
  }

  private void guardVaultTry(PrisonSector sector, Matcher matcher) {
    String robber = "";
    Laby.fireEvent(new GuardVaultTryEvent(sector, robber));
  }

  private void guardVaultUpdate(PrisonSector sector, Matcher matcher) {
    String timeLeftStr = "";
    Laby.fireEvent(new GuardVaultUpdateEvent(sector, timeLeftStr));
  }

  private void guardVaultFinish(PrisonSector sector, Matcher matcher) {
    String player = "";
    Laby.fireEvent(new GuardVaultFinishEvent(sector, player, true));
  }

  private void livingAreaHelpSendWaypoint(ChatMessage message) {
    if (message == null) {
      throw new NullPointerException("The provided ChatMessage is null");
    }
    markedResend(message);
    this.chatExecutor.displayClientMessage(
        Component.translatable("fvplus.server.prison.cell.commands.waypoint.description", NamedTextColor.WHITE,
            Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text("/ce waypoint <celleID>", NamedTextColor.RED))
                .append(Component.text("]", NamedTextColor.DARK_GRAY))
        )
    );
  }

  private void unrecognizedMessage(Matcher matcher) {
    this.logging.warn("The following chat-message is registered but is not supported by the client in this version, is the addon up-to-date?: " + matcher.group());
  }

  private void markedResend(ChatMessage message) {
    this.chatExecutor.displayClientMessage(
        message.component()
            .append(Component.space().append(Component.text("✉", NamedTextColor.GOLD)))
    );
  }


}
