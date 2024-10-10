package dk.fvtrademarket.fvplus.core.module.general;

import net.labymod.api.event.EventBus;
import dk.fvtrademarket.fvplus.core.configuration.SessionSubConfiguration;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.listeners.nprison.PrisonSessionListener;
import dk.fvtrademarket.fvplus.core.module.ListenerModule;
import dk.fvtrademarket.fvplus.core.session.AddonSession;
import java.util.ArrayList;

public class SessionModule extends ListenerModule {
  private final ClientInfo clientInfo;
  private final AddonSession addonSession;
  private final SessionSubConfiguration configuration;

  public SessionModule(EventBus eventBus, ClientInfo clientInfo, AddonSession addonSession, SessionSubConfiguration configuration) {
    super(eventBus);
    this.clientInfo = clientInfo;
    this.addonSession = addonSession;
    this.configuration = configuration;
    this.moduleListeners = moduleListenersOverview();
  }

  @Override
  protected ArrayList<Object> moduleListenersOverview() {
    ArrayList<Object> listeners = new ArrayList<>();
    listeners.add(new PrisonSessionListener(addonSession, clientInfo, configuration));
    return listeners;
  }

  @Override
  public boolean shouldRegisterAutomatically() {
    return this.configuration.enabled().get();
  }
}
