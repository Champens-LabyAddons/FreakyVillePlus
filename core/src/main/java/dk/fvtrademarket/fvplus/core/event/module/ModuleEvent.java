package dk.fvtrademarket.fvplus.core.event.module;

import net.labymod.api.event.Event;
import dk.fvtrademarket.fvplus.core.module.Module;

public class ModuleEvent implements Event {
  public enum Type {
    ACTIVATE,
    DEACTIVATE
  }

  private final Class<? extends Module> moduleClass;
  private final Type type;

  public ModuleEvent(Class<? extends Module> moduleClass, Type type) {
    this.moduleClass = moduleClass;
    this.type = type;
  }

  public Class<? extends Module> getModuleClass() {
    return this.moduleClass;
  }

  public Type getType() {
    return this.type;
  }
}
