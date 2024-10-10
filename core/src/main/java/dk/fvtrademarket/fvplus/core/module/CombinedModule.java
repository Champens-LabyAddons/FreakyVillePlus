package dk.fvtrademarket.fvplus.core.module;

import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.chat.command.CommandService;
import net.labymod.api.event.EventBus;
import java.util.ArrayList;
import java.util.List;

/**
 * En superklasse for alle moduler der blander både commands og listeners.
 *
 * @since 1.0.0
 */
public abstract class CombinedModule implements Module {
  private final CommandService commandService;
  protected List<Command> moduleCommands;
  private final EventBus eventBus;
  protected List<Object> moduleListeners;
  private boolean registered;

  public CombinedModule(CommandService commandService, EventBus eventBus) {
    this.commandService = commandService;
    this.eventBus = eventBus;
    this.registered = false;
  }

  @Override
  public void register() {
    for (Command command : this.moduleCommands) {
      this.commandService.register(command);
    }
    for (Object listener : this.moduleListeners) {
      this.eventBus.registerListener(listener);
    }
    this.registered = true;
  }

  @Override
  public void unregister() {
    for (Command command : this.moduleCommands) {
      this.commandService.unregister(command);
    }
    for (Object listener : this.moduleListeners) {
      this.eventBus.unregisterListener(listener);
    }
    this.registered = false;
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

  public boolean isRegistered() {
    return this.registered;
  }
}
