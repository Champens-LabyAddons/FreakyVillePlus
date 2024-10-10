package dk.fvtrademarket.fvplus.core.listeners.nprison;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.util.Pair;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.internal.CellList;
import dk.fvtrademarket.fvplus.core.listeners.HousingListener;
import dk.fvtrademarket.fvplus.core.objects.CellBlock;
import dk.fvtrademarket.fvplus.core.util.FreakyVilleServer;
import dk.fvtrademarket.fvplus.core.util.Messaging;

import java.util.Objects;

public final class CellListener extends HousingListener {
  private final ClientInfo clientInfo;
  private final CellList cellList;

  public CellListener(ClientInfo clientInfo, CellList cellList) {
    this.clientInfo = clientInfo;
    this.cellList = cellList;
  }

  @Subscribe
  @Override
  public void onChatReceived(ChatReceiveEvent event) {
    if (!this.clientInfo.isOnFreakyVille()
        && this.clientInfo.getCurrentServer() == FreakyVilleServer.PRISON) {
      return;
    }
    if (event.message().toBuilder().hasHoverEvent()) {
      return;
    }
    String plainMessage = event.chatMessage().getPlainText().trim();
    if (plainMessage.startsWith(Objects.requireNonNull(headerDecoration().getFirst())) &&
        plainMessage.endsWith(Objects.requireNonNull(headerDecoration().getSecond()))) {
      event.setCancelled(true);
      Messaging.executor().displayClientMessage(markComponent(event.message()));
      Messaging.executor().displayClientMessage(objectLocation(plainMessage));
    } else if (plainMessage.equals("[/ce find <spiller>] Se hvem spilleren har add hos")) {
      event.setCancelled(true);
      Messaging.executor().displayClientMessage(markComponent(event.message()));
      Messaging.executor().displayClientMessage(waypointHelp());
    }
  }

  private Component waypointHelp() {
    TextComponent syntax = Component.text("[", NamedTextColor.DARK_GRAY)
        .append(Component.text("/ce waypoint <celleID>", NamedTextColor.RED))
        .append(Component.text("]", NamedTextColor.DARK_GRAY));
    return Component.translatable("fvplus.server.prison.cell.commands.waypoint.description", NamedTextColor.WHITE, syntax);
  }

  @Override
  protected Component objectLocation(String header) {
    String restProduct = header
        .replace(Objects.requireNonNull(headerDecoration().getFirst()), "")
        .replace(Objects.requireNonNull(headerDecoration().getSecond()), "")
        .trim();
    if (restProduct.isEmpty() || restProduct.equals("Celle Kommandoer")) {
      return Component.empty();
    }
    if (cellList.isCellListed(restProduct)) {
      CellBlock cellBlock;
      if (cellList.getCorrespondingCellBlock(restProduct).isPresent()) {
        cellBlock = cellList.getCorrespondingCellBlock(restProduct).get();
      } else {
        return Component.empty();
      }
      TextComponent cellComponent = Component.text(cellBlock.getLocationDescription(), NamedTextColor.GRAY);
      return Component.translatable("fvplus.server.prison.cell.location", NamedTextColor.AQUA, cellComponent);
    }
    return Component.empty();
  }

  @Override
  protected Component markComponent(Component component) {
    return component.hoverEvent(HoverEvent.showText(
        Component.text("Ω", NamedTextColor.GOLD)
    ));
  }

  protected Pair<String, String> headerDecoration() {
    return Pair.of("------------={", "}=------------");
  }
}
