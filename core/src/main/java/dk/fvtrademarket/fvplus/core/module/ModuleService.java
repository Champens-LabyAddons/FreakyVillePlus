package dk.fvtrademarket.fvplus.core.module;

import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import net.labymod.api.LabyAPI;
import net.labymod.api.client.chat.command.CommandService;
import net.labymod.api.event.EventBus;
import net.labymod.api.util.I18n;
import net.labymod.api.util.logging.Logging;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Denne klasse håndtere alle {@link Module} objekterne.
 * <p>
 * Denne klasse er en del af et modulært system, som gør det muligt at registrere og fjerne moduler
 * fra en liste. Dette er en del af en større struktur, som gør det muligt at håndtere moduler på en
 * nem og overskuelig måde.
 * <p>
 * Grunden til at vi har denne klasse er fordi vi vil have en centraliseret måde at håndtere moduler
 * på, så vi ikke skal lave for meget kode for at håndtere moduler. Hele systemet er bygget op
 * omkring denne klasse, og det er derfor vigtigt at den ikke modtager for mange ændringer uden at
 * det er nødvendigt.
 * <p>
 * Efter 2.0.0 versionen af FreakyVillePlus er alle resourcer som skal være tilgængelige for moduler
 * blevet mulige at hente gennem denne klasse. Dette er for at gøre det nemmere at skabe moduler
 * der skal bruge forskellige resourcer, som f.eks. {@link LabyAPI}, {@link EventBus} og {@link dk.fvtrademarket.fvplus.core.connection.ClientInfo}.
 * @since 1.0.0
 */
public class ModuleService {

  private final LabyAPI labyAPI;
  private final EventBus eventBus;
  private final CommandService commandService;
  private final ClientInfo clientInfo;

  private final Logging logger = Logging.create(this.getClass());
  private final List<Module> modules;

  public ModuleService(LabyAPI labyAPI, EventBus eventBus, CommandService commandService, ClientInfo clientInfo) {
    this.modules = new ArrayList<>();

    this.labyAPI = labyAPI;
    this.eventBus = eventBus;
    this.commandService = commandService;
    this.clientInfo = clientInfo;
  }

  /**
   * Registrere et enkelt modul.
   *
   * @param module Modulet du vil registrere
   */
  public void registerModule(Module module) {
    if (module.isRegistered()) {
      return;
    }
    module.setModuleService(this);
    module.register();
    if (!modules.contains(module)) {
      modules.add(module);
    }
    String registrationMessage = I18n.translate("fvplus.logging.info.registeredModule") +  " | " + module;
    if (module instanceof BigModule) {
      registrationMessage += (" " + Arrays.toString(
          ((BigModule) module).getInternalModules().toArray(new Module[0])));
    }
    logger.info(registrationMessage);
  }

  /**
   * Registrere flere moduler på én gang, hvis de bør registres.
   *
   * @param modules Modulerne du vil registrere
   */
  public void registerModules(Module... modules) {
    for (Module module : modules) {
      if (module.shouldRegisterAutomatically()) {
        this.registerModule(module);
      }
    }
  }

  /**
   * Fjerner et modul fra listen over registrerede moduler.
   *
   * @param module Modulet du vil fjerne
   * @apiNote Lad være med at fjerne et registreret modul hvis det ikke er meningen.
   */
  public void unregisterModule(Module module) {
    if (!module.isRegistered()) {
      return;
    }
    module.unregister();
    logger.info(I18n.translate("fvplus.logging.info.unregisteredModule") + " | " + module.getClass().getTypeName());
  }

  /**
   * Fjerner alle registrerede moduler fra listen.
   *
   * @deprecated Det er ikke sikkert der vil være et behov for metoden. Vi har den her til hvis den
   * skulle blive nødvendig.
   */
  @Deprecated(since = "pre-1.0.0")
  public void unregisterAllModules() {
    for (Module module : modules) {
      unregisterModule(module);
    }
  }

  public List<Module> getModules() {
    return List.copyOf(modules);
  }

  public LabyAPI getLabyAPI() {
    return labyAPI;
  }

  public EventBus getEventBus() {
    return eventBus;
  }

  public CommandService getCommandService() {
    return commandService;
  }

  public ClientInfo getClientInfo() {
    return clientInfo;
  }
}
