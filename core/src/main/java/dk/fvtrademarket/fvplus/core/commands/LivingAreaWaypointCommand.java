package dk.fvtrademarket.fvplus.core.commands;

import dk.fvtrademarket.fvplus.api.housing.LivingArea;
import dk.fvtrademarket.fvplus.api.service.housing.HousingService;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.event.CreateLocationWaypointEvent;
import dk.fvtrademarket.fvplus.core.integrations.WaypointsIntegration;
import java.util.Optional;

public class LivingAreaWaypointCommand extends Command {
  private final ClientInfo clientInfo;
  private final HousingService housingService;

  public LivingAreaWaypointCommand(ClientInfo clientInfo, HousingService housingService) {
    super("ce");
    this.clientInfo = clientInfo;
    this.housingService = housingService;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.clientInfo.isOnFreakyVille()) return false;
    if (arguments.length != 2) return false;
    if (!arguments[0].equalsIgnoreCase("waypoint")
        && !arguments[0].equalsIgnoreCase("w")) return false;
    if (!WaypointsIntegration.isEnabled()) {
      displayMessage(Component.translatable("fvplus.server.prison.cell.commands.waypoint.disabled",
          NamedTextColor.RED));
      return true;
    }
    Optional<LivingArea> livingArea =
        this.housingService.getLivingArea(this.clientInfo.getCurrentServer(), arguments[1]);
    if (livingArea.isEmpty()) {
      displayMessage(Component.translatable("fvplus.server.prison.cell.commands.waypoint.emptyResult",
          NamedTextColor.RED));
      return true;
    }
    Laby.fireEvent(new CreateLocationWaypointEvent(livingArea.get().getDescription(), livingArea.get().getLocation()));
    displayMessage(Component.translatable("fvplus.server.prison.cell.commands.waypoint.success",
      NamedTextColor.GREEN));
    return true;
  }
}
