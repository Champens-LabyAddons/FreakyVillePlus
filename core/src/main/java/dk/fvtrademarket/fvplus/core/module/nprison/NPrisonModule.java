package dk.fvtrademarket.fvplus.core.module.nprison;

import dk.fvtrademarket.fvplus.core.configuration.PrisonSubConfiguration;
import dk.fvtrademarket.fvplus.core.module.BigModule;
import dk.fvtrademarket.fvplus.core.module.Module;
import dk.fvtrademarket.fvplus.core.module.ModuleService;
import java.util.ArrayList;

public class NPrisonModule extends BigModule {

  private final PrisonSubConfiguration prisonSubConfiguration;

  public NPrisonModule(ModuleService moduleService, PrisonSubConfiguration prisonSubConfiguration) {
    super(moduleService);
    this.prisonSubConfiguration = prisonSubConfiguration;
    this.internalModules = internalModulesOverview();
  }

  @Override
  protected ArrayList<Module> internalModulesOverview() {
    ArrayList<Module> modules = new ArrayList<>();
    return modules;
  }

  @Override
  public boolean shouldRegisterAutomatically() {
    return this.prisonSubConfiguration.enabled().get();
  }

  @Override
  public String toString() {
    return "NPrisonModule";
  }
}