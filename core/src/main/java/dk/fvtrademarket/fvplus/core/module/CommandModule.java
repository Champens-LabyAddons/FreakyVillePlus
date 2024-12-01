package dk.fvtrademarket.fvplus.core.module;

import net.labymod.api.client.chat.command.Command;
import java.util.ArrayList;
import java.util.List;

/**
 * En superklasse for alle moduler der har en intention om at registrere kommandoer.
 *
 * @since 1.0.0
 */
public abstract class CommandModule extends AbstractModule {

  protected List<Command> moduleCommands;

  public CommandModule(ModuleService moduleService) {
    super(moduleService);
  }

  @Override
  public void register() {
    for (Command command : this.moduleCommands) {
      this.moduleService.getCommandService().register(command);
    }
    super.register();
  }

  @Override
  public void unregister() {
    for (Command command : this.moduleCommands) {
      this.moduleService.getCommandService().unregister(command);
    }
    super.unregister();
  }

  /**
   * Returnerer en oversigt over alle kommandoer i modulet.
   *
   * @return en liste af kommandoer i modulet
   */
  protected abstract ArrayList<Command> moduleCommandsOverview();

  @Override
  public abstract boolean shouldRegisterAutomatically();
}
