package dk.fvtrademarket.fvplus.core.util;

import net.labymod.api.Laby;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import dk.fvtrademarket.fvplus.core.event.module.ModuleEvent;
import dk.fvtrademarket.fvplus.core.module.Module;

public final class Setting {
  private Setting() {}

  public static void addModuleListener (ConfigProperty<Boolean> to, Class<? extends Module> moduleClass) {
    to.addChangeListener((enabled) -> {
      if (enabled) {
        Laby.fireEvent(new ModuleEvent(moduleClass, ModuleEvent.Type.ACTIVATE));
      } else {
        Laby.fireEvent(new ModuleEvent(moduleClass, ModuleEvent.Type.DEACTIVATE));
      }
    });
  }
}
