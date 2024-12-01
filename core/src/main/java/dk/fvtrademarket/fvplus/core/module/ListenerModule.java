package dk.fvtrademarket.fvplus.core.module;

import java.util.ArrayList;
import java.util.List;

/**
 * En superklasse for alle moduler der har en intention om at lytte til events.
 *
 * @since 1.0.0
 */
public abstract class ListenerModule extends AbstractModule {

  protected List<Object> moduleListeners;

  public ListenerModule(ModuleService moduleService) {
    super(moduleService);
  }

  @Override
  public void register() {
    for (Object listener : this.moduleListeners) {
      this.moduleService.getEventBus().registerListener(listener);
    }
    super.register();
  }

  @Override
  public void unregister() {
    for (Object listener : this.moduleListeners) {
      this.moduleService.getEventBus().unregisterListener(listener);
    }
    super.unregister();
  }

  /**
   * Returnerer en oversigt over alle listeners i modulet.
   *
   * @return en liste af listeners i modulet
   */
  protected abstract ArrayList<Object> moduleListenersOverview();

  @Override
  public abstract boolean shouldRegisterAutomatically();
}
