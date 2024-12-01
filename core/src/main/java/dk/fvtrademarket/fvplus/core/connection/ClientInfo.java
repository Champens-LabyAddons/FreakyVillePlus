package dk.fvtrademarket.fvplus.core.connection;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.network.server.ServerController;
import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.core.util.Prison;
import java.util.Objects;
import java.util.Optional;

public class ClientInfo {
  private ServerController serverController;
  private ClientPlayer clientPlayer;
  private FreakyVilleServer currentServer;
  private FreakyVilleServer lastServer;
  private PrisonSector prisonSector;
  private boolean hasUpdatedToCurrentServer;

  public ClientInfo(ServerController serverController, ClientPlayer clientPlayer) {
    this.serverController = serverController;
    this.clientPlayer = clientPlayer;
    this.currentServer = FreakyVilleServer.NONE;
    this.lastServer = FreakyVilleServer.NONE;
    this.prisonSector = null;
    this.hasUpdatedToCurrentServer = false;
  }

  public boolean isOnFreakyVille() {
    if (!this.serverController.isConnected()) return false;
    return isValidServerAddress(
        Objects.requireNonNull(this.serverController.getCurrentServerData()).address().getHost());
  }

  public Optional<ClientPlayer> getClientPlayer() {
    return Optional.ofNullable(clientPlayer);
  }

  public void setClientPlayer(ClientPlayer clientPlayer) {
    this.clientPlayer = clientPlayer;
  }

  public FreakyVilleServer getCurrentServer() {
    return currentServer;
  }

  public void setCurrentServer(FreakyVilleServer currentServer) {
    this.currentServer = currentServer;
  }

  public FreakyVilleServer getLastServer() {
    return lastServer;
  }

  public void setLastServer(FreakyVilleServer lastServer) {
    this.lastServer = lastServer;
  }

  public Optional<PrisonSector> getPrisonSector() {
    return Optional.ofNullable(prisonSector);
  }

  public void setPrisonSector(PrisonSector prisonSector) {
    this.prisonSector = prisonSector;
  }

  public boolean isUpdatedToCurrentServer() {
    return hasUpdatedToCurrentServer;
  }

  public void setHasUpdatedToCurrentServer(boolean hasUpdatedToCurrentServer) {
    this.hasUpdatedToCurrentServer = hasUpdatedToCurrentServer;
  }

  private boolean isValidServerAddress(String address) {
    address = address.toLowerCase();
    return address.endsWith(".freakyville.dk") || address.endsWith(".freakyville.net");
  }
}
