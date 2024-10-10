package dk.fvtrademarket.fvplus.core.listeners.misc;

import net.labymod.api.LabyAPI;
import net.labymod.api.event.Subscribe;
import net.labymod.api.thirdparty.discord.DiscordActivity;
import net.labymod.api.thirdparty.discord.DiscordActivity.Asset;
import net.labymod.api.thirdparty.discord.DiscordActivity.Builder;
import net.labymod.api.util.I18n;
import dk.fvtrademarket.fvplus.core.configuration.DiscordSubConfiguration;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.event.RequestEvent;
import dk.fvtrademarket.fvplus.core.event.RequestEvent.RequestType;
import dk.fvtrademarket.fvplus.core.util.FreakyVilleServer;
import java.time.Instant;

public class DiscordRPCListener {

  private final LabyAPI labyAPI;
  private final ClientInfo clientInfo;
  private final DiscordSubConfiguration configuration;
  private boolean currentlyRunning;
  private final String freakyVille;

  public DiscordRPCListener(ClientInfo clientInfo, LabyAPI labyAPI, DiscordSubConfiguration configuration) {
    this.clientInfo = clientInfo;
    this.labyAPI = labyAPI;
    this.configuration = configuration;
    this.currentlyRunning = false;
    this.freakyVille = I18n.translate("fvplus.server.name");
  }

  private void updateRichPresence() {
    if (this.currentlyRunning) {
      return;
    }
    if (!clientInfo.isOnFreakyVille()) {
      return;
    }

    currentlyRunning = true;
    Builder acBuilder = DiscordActivity.builder(this);
    String description;
    String state = "";

    if (!configuration.getShowCurrentServer().get()) {
      description = I18n.translate("fvplus.rpc.playing", freakyVille);
      acBuilder.details(description);
      acBuilder.largeAsset(getServerAsset(FreakyVilleServer.HUB));
      labyAPI.thirdPartyService().discord().displayActivity(acBuilder.build());
      currentlyRunning = false;
      return;
    }

    if (clientInfo.getCurrentServer() == FreakyVilleServer.HUB) {
      description = I18n.translate("fvplus.rpc.in", FreakyVilleServer.HUB.getTranslatedName());
      if (clientInfo.getLastServer() != FreakyVilleServer.NONE
          && clientInfo.getLastServer() != FreakyVilleServer.HUB) {
        state = I18n.translate("fvplus.rpc.lastSeen",
            clientInfo.getLastServer().getTranslatedName());
      }
    } else if (clientInfo.getCurrentServer() != FreakyVilleServer.NONE
        && clientInfo.getCurrentServer() != null) {
      description = I18n.translate("fvplus.rpc.playing", clientInfo.getCurrentServer().getTranslatedName());
    } else {
      description = I18n.translate("fvplus.rpc.playing", freakyVille);
    }
    acBuilder.details(description);
    if (!state.isEmpty()) {
      acBuilder.state(state);
    }
    acBuilder.largeAsset(getServerAsset(clientInfo.getCurrentServer()));
    acBuilder.start(Instant.now());

    this.labyAPI.thirdPartyService().discord().displayActivity(acBuilder.build());

    currentlyRunning = false;
  }

  @Subscribe
  public void onDiscordRPC(RequestEvent event) {
    if (event.getRequestType() != RequestType.DISCORD_RPC) {
      return;
    }
    updateRichPresence();
  }

  private Asset getServerAsset(FreakyVilleServer game) {
    return switch (game) {
      case SKY_BLOCK -> Asset.of(
          "https://imgur.com/g3KaKYM.png",
          freakyVille + " - " + game.getTranslatedName());
      case PRISON -> Asset.of(
          "https://imgur.com/ubmcddH.png",
          freakyVille + " - " + game.getTranslatedName());
      case KIT_PVP -> Asset.of(
          "https://imgur.com/WtVkxLX.png",
          freakyVille + " - " + game.getTranslatedName());
      case THE_CITY -> Asset.of(
          "https://imgur.com/px58nWq.png",
          freakyVille + " - " + game.getTranslatedName());
      case CREATIVE -> Asset.of(
          "https://imgur.com/3yEnk1T.png",
          freakyVille + " - " + game.getTranslatedName());
      default -> Asset.of(
          "https://i.imgur.com/gCRkhmm.png",
          freakyVille);
    };
  }
}
