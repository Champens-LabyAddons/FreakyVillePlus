package dk.fvtrademarket.fvplus.core.listeners;

import net.labymod.api.client.component.Component;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.util.Pair;

public abstract class HousingListener {

  public abstract void onChatReceived(ChatReceiveEvent event);

  protected abstract Component objectLocation(String header);

  protected abstract Component markComponent(Component component);

  protected abstract Pair<String, String> headerDecoration();
}
