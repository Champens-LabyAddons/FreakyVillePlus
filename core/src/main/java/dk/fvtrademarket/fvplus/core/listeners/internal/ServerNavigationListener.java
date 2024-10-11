package dk.fvtrademarket.fvplus.core.listeners.internal;

import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerJoinEvent;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.event.RequestEvent;
import dk.fvtrademarket.fvplus.core.event.RequestEvent.RequestType;
import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;

public class ServerNavigationListener {
  private final ClientInfo clientInfo;

  public ServerNavigationListener(ClientInfo clientInfo) {
    this.clientInfo = clientInfo;
  }

  @Subscribe
  public void onServerJoin(ServerJoinEvent event) {
    if (!this.isValidServerAddress(event.serverData().address().getHost())) {
      this.clientInfo.setCurrentServer(FreakyVilleServer.NONE);
      this.clientInfo.setLastServer(FreakyVilleServer.NONE);
      this.clientInfo.setPrison(null);
      this.clientInfo.setHasUpdatedToCurrentServer(false);
      return;
    }
    this.clientInfo.setClientPlayer(Laby.labyAPI().minecraft().getClientPlayer());
    this.clientInfo.setCurrentServer(FreakyVilleServer.HUB);
    this.clientInfo.setLastServer(this.clientInfo.getLastServer());
    this.clientInfo.setPrison(null);
  }

  @Subscribe
  public void onServerDisconnect(ServerDisconnectEvent event) {
    if (this.clientInfo.getCurrentServer() != FreakyVilleServer.NONE) {
      this.clientInfo.setCurrentServer(FreakyVilleServer.NONE);
      this.clientInfo.setLastServer(FreakyVilleServer.NONE);
      this.clientInfo.setPrison(null);
      this.clientInfo.setHasUpdatedToCurrentServer(false);
      Laby.labyAPI().thirdPartyService().discord().displayDefaultActivity();
    }
  }

  @Subscribe
  public void onSubServerSwitch(SubServerSwitchEvent event) {
    if (!this.clientInfo.isOnFreakyVille()) {
      return;
    }
    this.clientInfo.setLastServer(this.clientInfo.getCurrentServer());
    this.clientInfo.setPrison(null);
    this.clientInfo.setHasUpdatedToCurrentServer(false);
    Laby.fireEvent(new RequestEvent(RequestType.REMOVE_WAYPOINTS));
  }

  private boolean isValidServerAddress(String address) {
    address = address.toLowerCase();
    return address.endsWith(".freakyville.dk") || address.endsWith(".freakyville.net");
  }
}
