package dk.fvtrademarket.fvplus.core.commands.timers.sub;

import dk.fvtrademarket.fvplus.api.activatable.Activatable;
import dk.fvtrademarket.fvplus.api.service.activatable.ActivatableService;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import java.util.ArrayList;
import java.util.List;

public class TimerPersonalCommand extends TimerSubCommand {
  public TimerPersonalCommand(String serverAndCategoryKey, String parentPrefix,
      ClientInfo clientInfo, ActivatableService activatableService) {
      super("personal", serverAndCategoryKey, parentPrefix, clientInfo, activatableService,  "personlig", "p");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!clientInfo.isOnFreakyVille()) return false;
    List<Activatable> activatablesToShow = new ArrayList<>();
    for (Activatable activatable : this.activatableService.getAllActivatables()) {
      if (this.activatableService.isOnPersonalCooldown(activatable)) {
        activatablesToShow.add(activatable);
      }
    }
    return howToExecute(arguments, activatablesToShow);
  }
}
