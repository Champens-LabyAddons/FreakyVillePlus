package dk.fvtrademarket.fvplus.core.module;

import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import java.util.ArrayList;
import java.util.List;

/**
 * En superklasse for alle moduler der har en intention om at have en masse undermoduler.
 * <p>
 * Dette kan være en god idé hvis man har en masse moduler der kun relatere sig til en {@link FreakyVilleServer}.
 * @since 1.0.0
 */
public abstract class BigModule implements Module {
  private final ModuleService moduleService;
  protected List<Module> internalModules;
  private boolean registered;

  public BigModule(ModuleService moduleService) {
    this.moduleService = moduleService;
    this.registered = false;
  }

  @Override
  public void register() {
    moduleService.registerModules(internalModules.toArray(new Module[0]));
    this.registered = true;
  }

  @Override
  public void unregister() {
    for (Module module : internalModules) {
      moduleService.unregisterModule(module);
    }
    this.registered = false;
  }

  /**
   * Returnerer en oversigt over alle moduler i modulet.
   *
   * @return en liste af moduler i modulet
   */
  protected abstract ArrayList<Module> internalModulesOverview();

  public final List<Module> getInternalModules() {
    return List.copyOf(internalModules);
  }

  @Override
  public abstract boolean shouldRegisterAutomatically();

  public boolean isRegistered() {
    return this.registered;
  }
}
