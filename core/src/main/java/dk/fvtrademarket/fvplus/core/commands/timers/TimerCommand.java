package dk.fvtrademarket.fvplus.core.commands.timers;

import net.labymod.api.client.component.format.NamedTextColor;
import dk.fvtrademarket.fvplus.core.commands.FreakyVillePlusCommand;
import dk.fvtrademarket.fvplus.core.commands.timers.sub.TimerGlobalCommand;
import dk.fvtrademarket.fvplus.core.commands.timers.sub.TimerPersonalCommand;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.internal.PoiList;
import java.util.List;

public class TimerCommand extends FreakyVillePlusCommand {
  private final ClientInfo clientInfo;

  public TimerCommand(ClientInfo clientInfo, PoiList poiList) {
    super("timer", "", "tim");
    this.clientInfo = clientInfo;
    this.withSubCommand(new TimerGlobalCommand(getServerAndCategoryKey(), this.getPrefix(), clientInfo, poiList));
    this.withSubCommand(new TimerPersonalCommand(getServerAndCategoryKey(), this.getPrefix(), clientInfo, poiList));
  }

  @Override
  public List<String> complete(String[] arguments) {
    return super.subCommandPrefixes();
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.clientInfo.isOnFreakyVille()) return false;
    if (arguments.length < 1) {
      displayTranslatable("usage", NamedTextColor.RED);
      return true;
    }
    return true;
  }
}
