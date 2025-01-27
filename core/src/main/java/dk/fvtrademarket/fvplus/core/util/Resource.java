package dk.fvtrademarket.fvplus.core.util;

public class Resource {

  private final String specifics;
  private final String location;

  private static final String DATA_HUB = "https://raw.githubusercontent.com/FreakyVille-Trademarket/Public-Freakyville-Datahub/refs/heads/main/";

  private Resource(String specifics, String location) {
    this.specifics = specifics;
    this.location = location;
  }

  public static final Resource GUARD_VAULTS = new Resource("activatable/guardvaults.csv", DATA_HUB);
  public static final Resource GANG_AREAS = new Resource("activatable/gangarea.csv", DATA_HUB);

  public static final Resource MESSAGES = new Resource("message/messages.csv", DATA_HUB);
  public static final Resource ADVANCED_MESSAGES = new Resource("message/advancedmessages.csv", DATA_HUB);
  public static final Resource END_VAR_MESSAGES = new Resource("message/endvariablemessages.csv", DATA_HUB);

  public static final Resource CELL_LIST = new Resource("livingarea/nprison.csv", DATA_HUB);
  public static final Resource HOMES_LIST = new Resource("livingarea/friheden.csv", DATA_HUB);

  @Override
  public String toString() {
    return this.location + this.specifics;
  }
}
