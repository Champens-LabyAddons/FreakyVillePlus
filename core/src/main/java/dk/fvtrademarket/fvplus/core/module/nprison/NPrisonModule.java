package dk.fvtrademarket.fvplus.core.module.nprison;

import net.labymod.api.client.chat.command.CommandService;
import net.labymod.api.event.EventBus;
import dk.fvtrademarket.fvplus.core.configuration.PrisonSubConfiguration;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.internal.PoiList;
import dk.fvtrademarket.fvplus.core.module.BigModule;
import dk.fvtrademarket.fvplus.core.module.Module;
import dk.fvtrademarket.fvplus.core.module.ModuleService;
import java.util.ArrayList;

import static net.labymod.api.Laby.labyAPI;

public class NPrisonModule extends BigModule {

  private final CommandService commandService;
  private final EventBus eventBus;
  private final ClientInfo clientInfo;
  private final PrisonSubConfiguration prisonSubConfiguration;
  private final PoiList poiList;

  public NPrisonModule(ModuleService moduleService, CommandService commandService,
      EventBus eventBus, ClientInfo clientInfo, PrisonSubConfiguration prisonSubConfiguration,
      PoiList poiList) {
    super(moduleService);
    this.commandService = commandService;
    this.eventBus = eventBus;
    this.clientInfo = clientInfo;
    this.prisonSubConfiguration = prisonSubConfiguration;
    this.poiList = poiList;
    this.internalModules = internalModulesOverview();
  }

  @Override
  protected ArrayList<Module> internalModulesOverview() {
    ArrayList<Module> modules = new ArrayList<>();
    modules.add(new CellModule(commandService, eventBus, clientInfo, prisonSubConfiguration));
    modules.add(new PoiModule(commandService, eventBus, clientInfo, labyAPI().hudWidgetRegistry(), poiList, prisonSubConfiguration));
    return modules;
  }

  @Override
  public boolean shouldRegisterAutomatically() {
    return prisonSubConfiguration.enabled().get();
  }
}