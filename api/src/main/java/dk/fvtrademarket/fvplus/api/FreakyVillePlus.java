package dk.fvtrademarket.fvplus.api;

import dk.fvtrademarket.fvplus.api.generated.ReferenceStorage;

/**
 * Brugergrænseflade til FreakyVillePlus.
 * <p>
 * Denne klasse giver adgang til alle FreakyVillePlus-tjenester.
 *
 * @since 2.0.0
 */
public class FreakyVillePlus {

  private static ReferenceStorage references;

  private FreakyVillePlus() {}

  public static ReferenceStorage getReferences() {
    return references;
  }

  public static void init(ReferenceStorage references) {
    if (FreakyVillePlus.references != null) {
      throw new IllegalStateException("FreakyVillePlus is already initialized");
    } else {
      FreakyVillePlus.references = references;
      initRefs(references);
    }
  }

  public static void refresh() {

  }

  private static void initRefs(ReferenceStorage references) {
    references.messageService().initialize();
    references.activatableService().initialize();
    references.housingService().initialize();
  }
}
