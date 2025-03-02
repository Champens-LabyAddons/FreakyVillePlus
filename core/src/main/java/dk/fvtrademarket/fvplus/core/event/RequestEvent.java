package dk.fvtrademarket.fvplus.core.event;

import net.labymod.api.event.Event;

/**
 * Repræsenterer en forespørgsel om en {@link RequestType}
 */
public class RequestEvent implements Event {

  private final RequestType requestType;

  public RequestEvent(RequestType requestType) {
    this.requestType = requestType;
  }

  public RequestType getRequestType() {
    return requestType;
  }

  /**
   * Repræsenterer en forespørgselstype. Dette benyttes til events, som vi gerne vil lytte efter
   * men som ikke har nogen parametre og derfor er unødvendige at lave en ny klasse til.
   */
  public enum RequestType {
    DISCORD_RPC,
    REMOVE_WAYPOINTS
  }
}
