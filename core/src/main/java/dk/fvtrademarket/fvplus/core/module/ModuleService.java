package dk.fvtrademarket.fvplus.core.module;

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
 *
 * @since 1.0.0
 */
public class ModuleService {

  private final Logging logger;
  private final List<Module> modules;

  public ModuleService(Logging logger) {
    this.logger = logger;
    this.modules = new ArrayList<>();
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
    module.register();
    if (!modules.contains(module)) {
      modules.add(module);
    }
    String registrationMessage = I18n.translate("fvplus.logging.info.registeredModule") +  " | " + module.getClass().getTypeName();
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
}
