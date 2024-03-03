package org.champenslabyaddons.fvplus.commands;

import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import org.champenslabyaddons.fvplus.connection.ClientInfo;
import org.champenslabyaddons.fvplus.event.CreateLocationWaypointEvent;
import org.champenslabyaddons.fvplus.integrations.WaypointsIntegration;
import org.champenslabyaddons.fvplus.internal.CellList;
import org.champenslabyaddons.fvplus.objects.CellBlock;
import org.champenslabyaddons.fvplus.util.FreakyVilleServer;

public class CellWaypointCommand extends Command {
  private final ClientInfo clientInfo;
  private final CellList cellList;

  public CellWaypointCommand(ClientInfo clientInfo, CellList cellList) {
    super("ce");
    this.clientInfo = clientInfo;
    this.cellList = cellList;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (clientInfo.getCurrentServer() != FreakyVilleServer.PRISON) return false;
    if (arguments.length != 2) return false;
    if (!arguments[0].equalsIgnoreCase("waypoint")
        && !arguments[0].equalsIgnoreCase("w")) return false;
    if (!WaypointsIntegration.isEnabled()) {
      displayMessage(Component.translatable("fvplus.server.prison.cell.commands.waypoint.disabled",
          NamedTextColor.RED));
      return true;
    }
    if (!cellList.isCellListed(arguments[1])) {
      displayMessage(Component.translatable("fvplus.server.prison.cell.commands.waypoint.notListed",
          NamedTextColor.RED));
      return true;
    }
    if (cellList.getCorrespondingCellBlock(arguments[1]).isEmpty()) {
      displayMessage(Component.translatable("fvplus.server.prison.cell.commands.waypoint.emptyResult",
          NamedTextColor.RED));
      return true;
    }
    CellBlock cellBlock = cellList.getCorrespondingCellBlock(arguments[1]).get();
    Laby.fireEvent(new CreateLocationWaypointEvent(cellBlock.getLocationDescription(), cellBlock.getMinecraftLocation()));
    displayMessage(Component.translatable("fvplus.server.prison.cell.commands.waypoint.success",
      NamedTextColor.GREEN));
    return true;
  }
}
