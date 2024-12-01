package dk.fvtrademarket.fvplus.core.module;

/**
 * Et registrerbart modul, som kan registreres og fjernes fra en liste.
 * <p>
 * Ideen med et modul er at det skal være en samlende klasse for en masse andre klasser der håndtere
 * den samme information, eller som bare hører under samme kategori. Derfor vil man når man slår et
 * modul til og frå nemt kunne ændre statussen for alle de registrerede komponenter i et modul.
 * <p>
 * Et modul håndtere selv hvad der internt skal ske med det når dets status bliver ændret. Dette
 * sker i modulets egne repræsentationer af {@link Module#register()}. og
 * {@link Module#unregister()}.
 *
 * @since 1.0.0
 */
public interface Module {

  /**
   * Registrere alle komponenter i modulet.
   */
  void register();

  /**
   * Afregistrere alle komponenter i modulet.
   */
  void unregister();

  /**
   * Returnere om modulet skal registreres automatisk.
   *
   * @return om modulet skal registreres automatisk.
   */
  boolean shouldRegisterAutomatically();

  /**
   * Returnere om modulet er registreret.
   *
   * @return om modulet er registreret.
   */
  boolean isRegistered();

  /**
   * Henviser den moduleservice som modulet skal trække informationer fra.
   *
   * @param moduleService moduleservicen som modulet skal trække informationer fra.
   * @since 2.0.0
   */
  void setModuleService(ModuleService moduleService);
}
