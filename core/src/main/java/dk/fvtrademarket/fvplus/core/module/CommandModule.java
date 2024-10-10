package dk.fvtrademarket.fvplus.core.module;

import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.chat.command.CommandService;
import java.util.ArrayList;
import java.util.List;

/**
 * En superklasse for alle moduler der har en intention om at registrere kommandoer.
 *
 * @since 1.0.0
 */
public abstract class CommandModule implements Module {

  private final CommandService commandService;
  protected List<Command> moduleCommands;
  private boolean registered;

  public CommandModule(CommandService commandService) {
    this.commandService = commandService;
    this.moduleCommands = this.moduleCommandsOverview();
    this.registered = false;
  }

  @Override
  public void register() {
    for (Command command : this.moduleCommands) {
      this.commandService.register(command);
    }
    this.registered = true;
  }

  @Override
  public void unregister() {
    for (Command command : this.moduleCommands) {
      this.commandService.unregister(command);
    }
    this.registered = false;
  }

  /**
   * Returnerer en oversigt over alle kommandoer i modulet.
   *
   * @return en liste af kommandoer i modulet
   */
  protected abstract ArrayList<Command> moduleCommandsOverview();

  @Override
  public abstract boolean shouldRegisterAutomatically();

  public boolean isRegistered() {
    return this.registered;
  }
}
