package dk.fvtrademarket.fvplus.api.event.housing;

import net.labymod.api.event.Event;

public class LivingAreaLookupEvent implements Event {
  private final String areaIdentifier;

  public LivingAreaLookupEvent(String areaIdentifier) {
    this.areaIdentifier = areaIdentifier;
  }

  public String getAreaIdentifier() {
    return areaIdentifier;
  }
}
