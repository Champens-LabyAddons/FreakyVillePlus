package dk.fvtrademarket.fvplus.core.module;

import net.labymod.api.event.EventBus;
import java.util.ArrayList;
import java.util.List;

/**
 * En superklasse for alle moduler der har en intention om at lytte til events.
 *
 * @since 1.0.0
 */
public abstract class ListenerModule implements Module {

  private final EventBus eventBus;
  protected List<Object> moduleListeners;
  private boolean registered;

  public ListenerModule(EventBus eventBus) {
    this.eventBus = eventBus;
    this.registered = false;
  }

  @Override
  public void register() {
    for (Object listener : this.moduleListeners) {
      this.eventBus.registerListener(listener);
    }
    this.registered = true;
  }

  @Override
  public void unregister() {
    for (Object listener : this.moduleListeners) {
      this.eventBus.unregisterListener(listener);
    }
    this.registered = false;
  }

  /**
   * Returnerer en oversigt over alle listeners i modulet.
   *
   * @return en liste af listeners i modulet
   */
  protected abstract ArrayList<Object> moduleListenersOverview();

  @Override
  public abstract boolean shouldRegisterAutomatically();

  public boolean isRegistered() {
    return this.registered;
  }
}
