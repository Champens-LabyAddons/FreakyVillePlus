package dk.fvtrademarket.fvplus.core.listeners.misc;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;

public class StatMessageListener {
  private final ClientInfo clientInfo;

  public StatMessageListener(ClientInfo clientInfo) {
    this.clientInfo = clientInfo;
  }

  @Subscribe
  public void onStatMessageReceive(ChatReceiveEvent event) {
    if (!this.clientInfo.isOnFreakyVille()) {
      return;
    }
  }
}
