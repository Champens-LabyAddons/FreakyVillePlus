package org.champenslabyaddons.fvplus.listeners.misc;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.champenslabyaddons.fvplus.connection.ClientInfo;

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
