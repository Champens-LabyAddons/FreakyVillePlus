package dk.fvtrademarket.fvplus.core.module;

public abstract class AbstractModule implements Module {
  protected ModuleService moduleService;
  protected boolean registered;

  public AbstractModule(ModuleService moduleService) {
    setModuleService(moduleService);
    this.registered = false;
  }

  @Override
  public void register() {
    this.registered = true;
  }

  @Override
  public void unregister() {
    this.registered = false;
  }

  @Override
  public abstract boolean shouldRegisterAutomatically();

  @Override
  public boolean isRegistered() {
    return this.registered;
  }

  @Override
  public final void setModuleService(ModuleService moduleService) {
    if (this.moduleService != null) {
      throw new IllegalStateException("ModuleService is already set");
    }
  }
}
