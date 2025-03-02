package dk.fvtrademarket.fvplus.core;

import dk.fvtrademarket.fvplus.api.FreakyVillePlus;
import dk.fvtrademarket.fvplus.core.activatable.DefaultActivatableService;
import dk.fvtrademarket.fvplus.core.commands.internal.BlockCommand;
import dk.fvtrademarket.fvplus.core.commands.internal.FreakyVillePlusDebugCommand;
import dk.fvtrademarket.fvplus.core.commands.internal.IgnoreCommand;
import dk.fvtrademarket.fvplus.core.commands.waypoint.LivingAreaWaypointCommand;
import dk.fvtrademarket.fvplus.core.commands.timers.TimerCommand;
import dk.fvtrademarket.fvplus.core.listeners.ChatListener;
import dk.fvtrademarket.fvplus.core.listeners.activatable.GangAreaListener;
import dk.fvtrademarket.fvplus.core.listeners.activatable.GuardVaultListener;
import dk.fvtrademarket.fvplus.core.listeners.LivingAreaListener;
import dk.fvtrademarket.fvplus.core.listeners.MessageRecognizedListener;
import dk.fvtrademarket.fvplus.core.listeners.internal.GameShutdownListener;
import dk.fvtrademarket.fvplus.core.listeners.skill.SkillListener;
import dk.fvtrademarket.fvplus.core.util.WidgetUpdater;
import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.addon.integration.AddonIntegration;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.generated.ReferenceStorage;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.util.I18n;
import dk.fvtrademarket.fvplus.core.commands.internal.FreakyvillePlusHelpCommand;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.integrations.WaypointsIntegration;
import dk.fvtrademarket.fvplus.core.listeners.internal.ScoreBoardListener;
import dk.fvtrademarket.fvplus.core.listeners.internal.ServerNavigationListener;
import dk.fvtrademarket.fvplus.core.listeners.internal.ModuleListener;
import dk.fvtrademarket.fvplus.core.module.ModuleService;
import dk.fvtrademarket.fvplus.core.module.general.RPCModule;
import dk.fvtrademarket.fvplus.core.util.Messaging;
import net.labymod.api.util.Pair;

@AddonMain
public class FreakyVilleAddon extends LabyAddon<FreakyVillePlusConfiguration> {

  private static FreakyVilleAddon INSTANCE;

  private HudWidgetCategory[] getWidgetCategories() {
    return new HudWidgetCategory[] {
        new HudWidgetCategory(this,"fvplus_guard_vault_timer_category"),
        new HudWidgetCategory(this,"fvplus_gang_area_timer_category"),
        new HudWidgetCategory(this,"fvplus_misc_timer_category")
    };
  }

  private Pair<String, Class<? extends AddonIntegration>>[] getAddonIntegrations() {
    return new Pair[]{
        Pair.of("labyswaypoints", WaypointsIntegration.class)
    };
  }

  private Object[] getStandardListeners(ClientInfo clientInfo, LabyAPI labyAPI, ReferenceStorage referenceStorage) {
    return new Object[] {
        new GameShutdownListener(),
        new ScoreBoardListener(clientInfo),
        new ServerNavigationListener(clientInfo),
        new ChatListener(clientInfo, FreakyVillePlus.getReferences().messageService(), configuration().getPrisonSubSettings().getSkillConfiguration()),
        new GuardVaultListener(clientInfo, labyAPI,
            (DefaultActivatableService) FreakyVillePlus.getReferences().activatableService()),
        new GangAreaListener(clientInfo, labyAPI,
            (DefaultActivatableService) FreakyVillePlus.getReferences().activatableService()),
        new LivingAreaListener(clientInfo, referenceStorage.chatExecutor(),
            FreakyVillePlus.getReferences().housingService()),
        new MessageRecognizedListener(clientInfo, referenceStorage.chatExecutor()),
        new SkillListener(clientInfo, labyAPI, configuration().getPrisonSubSettings().getSkillConfiguration(), FreakyVillePlus.getReferences().skillService())
    };
  }

  @Override
  protected void enable() {
    INSTANCE = this;
    this.registerSettingCategory();
    LabyAPI labyAPI = this.labyAPI();

    for (HudWidgetCategory widgetCategory : getWidgetCategories()) {
      labyAPI.hudWidgetRegistry().categoryRegistry().register(widgetCategory);
    }
    registerListener(new WidgetUpdater(labyAPI().hudWidgetRegistry()));

    ReferenceStorage labyReferences = Laby.references();

    ClientInfo clientInfo = new ClientInfo(labyAPI.serverController());

    FreakyVillePlus.init(this.referenceStorageAccessor());

    Messaging.setExecutor(labyAPI.minecraft().chatExecutor());

    for (Pair<String, Class<? extends AddonIntegration>> integration : getAddonIntegrations()) {
      labyReferences.addonIntegrationService().registerIntegration(integration.getFirst(), integration.getSecond());
    }
    for (Object listener : getStandardListeners(clientInfo, labyAPI, labyReferences)) {
      labyAPI.eventBus().registerListener(listener);
    }

    this.registerCommand(new FreakyvillePlusHelpCommand());
    this.registerCommand(new FreakyVillePlusDebugCommand(clientInfo));
    this.registerCommand(new LivingAreaWaypointCommand(clientInfo, FreakyVillePlus.getReferences().housingService()));
    this.registerCommand(new TimerCommand(clientInfo, FreakyVillePlus.getReferences().activatableService()));
    this.registerCommand(new BlockCommand(clientInfo, FreakyVillePlus.getReferences().messageService()));
    this.registerCommand(new IgnoreCommand(clientInfo, FreakyVillePlus.getReferences().messageService()));

    ModuleService moduleService = new ModuleService(labyAPI, labyAPI.eventBus(),
        labyAPI.commandService(), clientInfo);
    moduleService.registerModules(
        new RPCModule(moduleService, configuration().getDiscordSubSettings())//,
        //new NPrisonModule(moduleService, configuration().getPrisonSubSettings())
    );

    this.registerListener(new ModuleListener(moduleService));

    this.logger().info(I18n.translate("fvplus.logging.enabled"));
  }

  @Override
  protected Class<FreakyVillePlusConfiguration> configurationClass() {
    return FreakyVillePlusConfiguration.class;
  }

  public static FreakyVilleAddon get() {
    return INSTANCE;
  }
}
