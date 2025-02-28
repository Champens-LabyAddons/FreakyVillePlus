package dk.fvtrademarket.fvplus.core.module.general;

import dk.fvtrademarket.fvplus.core.module.ModuleService;
import dk.fvtrademarket.fvplus.core.configuration.DiscordSubConfiguration;
import dk.fvtrademarket.fvplus.core.listeners.misc.DiscordRPCListener;
import dk.fvtrademarket.fvplus.core.module.ListenerModule;
import java.util.ArrayList;

public class RPCModule extends ListenerModule {
  private final DiscordSubConfiguration configuration;

  public RPCModule(ModuleService moduleService, DiscordSubConfiguration configuration) {
    super(moduleService);
    this.configuration = configuration;
    this.moduleListeners = moduleListenersOverview();
  }

  @Override
  protected ArrayList<Object> moduleListenersOverview() {
    ArrayList<Object> listeners = new ArrayList<>();
    listeners.add(new DiscordRPCListener(this.moduleService.getClientInfo(),
        this.moduleService.getLabyAPI(), configuration));
    return listeners;
  }

  @Override
  public boolean shouldRegisterAutomatically() {
    return this.configuration.enabled().get();
  }

  @Override
  public String toString() {
    return "RPCModule";
  }
}
