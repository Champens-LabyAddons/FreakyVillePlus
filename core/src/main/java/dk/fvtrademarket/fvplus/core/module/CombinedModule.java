package dk.fvtrademarket.fvplus.core.module;

import net.labymod.api.client.chat.command.Command;
import java.util.ArrayList;
import java.util.List;

/**
 * En superklasse for alle moduler der blander både commands og listeners.
 *
 * @since 1.0.0
 */
public abstract class CombinedModule extends AbstractModule {
  protected List<Command> moduleCommands;
  protected List<Object> moduleListeners;

  public CombinedModule(ModuleService moduleService) {
    super(moduleService);
  }

  @Override
  public void register() {
    for (Command command : this.moduleCommands) {
      this.moduleService.getCommandService().register(command);
    }
    for (Object listener : this.moduleListeners) {
      this.moduleService.getEventBus().registerListener(listener);
    }
    super.register();
  }

  @Override
  public void unregister() {
    for (Command command : this.moduleCommands) {
      this.moduleService.getCommandService().unregister(command);
    }
    for (Object listener : this.moduleListeners) {
      this.moduleService.getEventBus().unregisterListener(listener);
    }
    super.unregister();
  }

  /**
   * Returnerer en oversigt over alle commands i modulet.
   *
   * @return en liste af commands i modulet
   */
  protected abstract ArrayList<Command> moduleCommandsOverview();

  /**
   * Returnerer en oversigt over alle listeners i modulet.
   *
   * @return en liste af listeners i modulet
   */
  protected abstract ArrayList<Object> moduleListenersOverview();

  @Override
  public abstract boolean shouldRegisterAutomatically();
}
