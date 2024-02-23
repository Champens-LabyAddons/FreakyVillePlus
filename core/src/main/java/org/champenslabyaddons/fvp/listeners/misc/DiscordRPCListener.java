package org.champenslabyaddons.fvp.listeners.misc;

import net.labymod.api.LabyAPI;
import net.labymod.api.event.Subscribe;
import net.labymod.api.thirdparty.discord.DiscordActivity;
import net.labymod.api.thirdparty.discord.DiscordActivity.Asset;
import net.labymod.api.thirdparty.discord.DiscordActivity.Builder;
import net.labymod.api.util.I18n;
import org.champenslabyaddons.fvp.connection.ClientInfo;
import org.champenslabyaddons.fvp.event.DiscordRPCEvent;
import org.champenslabyaddons.fvp.util.FreakyVilleServer;
import java.time.Instant;

public class DiscordRPCListener {

  private final LabyAPI labyAPI;
  private final ClientInfo clientInfo;
  private boolean currentlyRunning;

  public DiscordRPCListener(ClientInfo clientInfo, LabyAPI labyAPI) {
    this.clientInfo = clientInfo;
    this.labyAPI = labyAPI;
    this.currentlyRunning = false;
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
    String status;
    String extraDetails = "";

    if (clientInfo.getCurrentServer() == FreakyVilleServer.HUB) {
      status = I18n.translate("fvp.rpc.in", FreakyVilleServer.HUB.getServerName());
      if (clientInfo.getLastServer() != FreakyVilleServer.NONE
          && clientInfo.getLastServer() != FreakyVilleServer.HUB) {
        extraDetails = I18n.translate("fvp.rpc.lastSeen",
            clientInfo.getLastServer().getServerName());
      }
    } else if (clientInfo.getCurrentServer() != FreakyVilleServer.NONE
        && clientInfo.getCurrentServer() != null) {
      status = I18n.translate("fvp.rpc.playing", clientInfo.getCurrentServer().getServerName());
    } else {
      status = I18n.translate("fvp.rpc.playing", "FreakyVille");
    }
    acBuilder.state(status);
    if (!extraDetails.isEmpty()) {
      acBuilder.details(extraDetails);
    }
    acBuilder.largeAsset(getServerAsset(clientInfo.getCurrentServer()));
    acBuilder.start(Instant.now());
    //acBuilder.smallAsset(getPlayerAsset());

    this.labyAPI.thirdPartyService().discord().displayActivity(acBuilder.build());

    currentlyRunning = false;
  }

  @Subscribe
  public void onDiscordRPC(DiscordRPCEvent event) {
    updateRichPresence();
  }

  private Asset getServerAsset(FreakyVilleServer game) {
    String freakyVille = "Freakyville - ";
    return switch (game) {
      case SKY_BLOCK -> Asset.of(
          "https://imgur.com/g3KaKYM.png",
          freakyVille + game.getServerName());
      case PRISON -> Asset.of(
          "https://imgur.com/ubmcddH.png",
          freakyVille + game.getServerName());
      case KIT_PVP -> Asset.of(
          "https://imgur.com/WtVkxLX.png",
          freakyVille + game.getServerName());
      case THE_CITY -> Asset.of(
          "https://imgur.com/px58nWq.png",
          freakyVille + game.getServerName());
      case CREATIVE -> Asset.of(
          "https://imgur.com/3yEnk1T.png",
          freakyVille + game.getServerName());
      default -> Asset.of(
          "https://i.imgur.com/gCRkhmm.png",
          freakyVille);
    };
  }

  private Asset getPlayerAsset() {
    String playerName = this.clientInfo.getClientPlayer().getName();
    return Asset.of(
        String.format("https://minotar.net/helm/%s/128.png", playerName),
        playerName);
  }
}
