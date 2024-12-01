package dk.fvtrademarket.fvplus.core.module.nprison;

import dk.fvtrademarket.fvplus.core.module.ModuleService;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.chat.command.CommandService;
import net.labymod.api.event.EventBus;
import net.labymod.api.util.I18n;
import net.labymod.api.util.logging.Logging;
import dk.fvtrademarket.fvplus.core.commands.CellWaypointCommand;
import dk.fvtrademarket.fvplus.core.configuration.PrisonSubConfiguration;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.internal.CellList;
import dk.fvtrademarket.fvplus.core.listeners.nprison.CellListener;
import dk.fvtrademarket.fvplus.core.module.CombinedModule;
import java.io.IOException;
import java.util.ArrayList;

public class CellModule extends CombinedModule {

  private final PrisonSubConfiguration prisonSubConfiguration;

  public CellModule(ModuleService moduleService, PrisonSubConfiguration prisonSubConfiguration) {
    super(moduleService);
    this.prisonSubConfiguration = prisonSubConfiguration;
    this.moduleCommands = moduleCommandsOverview();
    this.moduleListeners = moduleListenersOverview();
  }

  @Override
  protected ArrayList<Command> moduleCommandsOverview() {
    ArrayList<Command> commands = new ArrayList<>();
    //commands.add(new CellWaypointCommand(this.moduleService.getClientInfo()));
    return commands;
  }

  @Override
  protected ArrayList<Object> moduleListenersOverview() {
    ArrayList<Object> listeners = new ArrayList<>();
    //listeners.add(new CellListener(this.moduleService.getClientInfo(), cellList));
    return listeners;
  }

  @Override
  public boolean shouldRegisterAutomatically() {
    return this.prisonSubConfiguration.getEnabledCellModule().get();
  }

  @Override
  public String toString() {
    return "CellModule";
  }
}
