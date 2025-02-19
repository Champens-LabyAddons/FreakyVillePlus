package dk.fvtrademarket.fvplus.core.commands.timers.sub;

import dk.fvtrademarket.fvplus.api.activatable.Activatable;
import dk.fvtrademarket.fvplus.api.service.activatable.ActivatableService;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import java.util.ArrayList;
import java.util.List;

public class TimerGlobalCommand extends TimerSubCommand {

  public TimerGlobalCommand(String serverAndCategoryKey, String parentPrefix,
      ClientInfo clientInfo, ActivatableService activatableService) {
    super("global", serverAndCategoryKey,  parentPrefix, clientInfo, activatableService, "globale", "g");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!clientInfo.isOnFreakyVille()) return false;
    List<Activatable> activatablesToShow = new ArrayList<>();
    for (Activatable activatable : this.activatableService.getAllActivatables()) {
      if (this.activatableService.isOnCooldown(activatable)) {
        activatablesToShow.add(activatable);
      }
    }
    return howToExecute(arguments, activatablesToShow);
  }
}
