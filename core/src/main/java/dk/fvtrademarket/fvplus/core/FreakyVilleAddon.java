package dk.fvtrademarket.fvplus.core;

import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.chat.command.CommandService;
import net.labymod.api.event.EventBus;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.util.I18n;
import dk.fvtrademarket.fvplus.core.commands.internal.CheckRollCommand;
import dk.fvtrademarket.fvplus.core.commands.internal.FreakyvillePlusHelpCommand;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.integrations.WaypointsIntegration;
import dk.fvtrademarket.fvplus.core.internal.PoiList;
import dk.fvtrademarket.fvplus.core.listeners.internal.PrisonNavigationListener;
import dk.fvtrademarket.fvplus.core.listeners.internal.ScoreBoardListener;
import dk.fvtrademarket.fvplus.core.listeners.internal.ServerNavigationListener;
import dk.fvtrademarket.fvplus.core.listeners.internal.ModuleListener;
import dk.fvtrademarket.fvplus.core.module.ModuleService;
import dk.fvtrademarket.fvplus.core.module.general.RPCModule;
import dk.fvtrademarket.fvplus.core.module.nprison.NPrisonModule;
import dk.fvtrademarket.fvplus.core.util.Messaging;

@AddonMain
public class FreakyVilleAddon extends LabyAddon<FreakyVillePlusConfiguration> {

  @Override
  protected void enable() {
    this.registerSettingCategory();
    LabyAPI labyAPI = this.labyAPI();
    ClientInfo clientInfo = new ClientInfo(labyAPI.serverController(), labyAPI.minecraft().getClientPlayer());
    EventBus eventBus = labyAPI.eventBus();
    CommandService commandService = labyAPI.commandService();
    Messaging.setExecutor(labyAPI.minecraft().chatExecutor());
    PoiList poiList = new PoiList();

    Laby.references().addonIntegrationService()
        .registerIntegration("labyswaypoints", WaypointsIntegration.class);
    this.registerListener(new ScoreBoardListener(clientInfo));
    this.registerListener(new ServerNavigationListener(clientInfo));
    this.registerListener(new PrisonNavigationListener(clientInfo));

    this.registerCommand(new CheckRollCommand(clientInfo));
    this.registerCommand(new FreakyvillePlusHelpCommand());

    ModuleService moduleService = new ModuleService(this.logger());
    moduleService.registerModules(
        new RPCModule(eventBus, clientInfo, labyAPI(), configuration().getDiscordSubSettings()),
        new NPrisonModule(moduleService, commandService, eventBus, clientInfo, configuration().getPrisonSubSettings(), poiList)
    );

    this.registerListener(new ModuleListener(moduleService));

    this.logger().info(I18n.translate("fvplus.logging.enabled"));
  }

  @Override
  protected Class<FreakyVillePlusConfiguration> configurationClass() {
    return FreakyVillePlusConfiguration.class;
  }
}
