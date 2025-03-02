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
public abstract class BigModule extends AbstractModule {
  protected List<Module> internalModules;

  public BigModule(ModuleService moduleService) {
    super(moduleService);
  }

  @Override
  public void register() {
    this.moduleService.registerModules(internalModules.toArray(new Module[0]));
    super.register();
  }

  @Override
  public void unregister() {
    for (Module module : internalModules) {
      this.moduleService.unregisterModule(module);
    }
    super.unregister();
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
}
