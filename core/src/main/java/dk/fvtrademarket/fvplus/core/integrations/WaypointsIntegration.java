package dk.fvtrademarket.fvplus.core.integrations;

import net.labymod.addons.waypoints.WaypointService;
import net.labymod.addons.waypoints.Waypoints;
import net.labymod.addons.waypoints.waypoint.WaypointMeta;
import net.labymod.addons.waypoints.waypoint.WaypointType;
import net.labymod.api.Laby;
import net.labymod.api.addon.integration.AddonIntegration;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.util.Color;
import net.labymod.api.util.math.vector.FloatVector3;
import dk.fvtrademarket.fvplus.core.event.CreateLocationWaypointEvent;
import dk.fvtrademarket.fvplus.core.event.RequestEvent;
import java.util.ArrayList;
import java.util.List;

public class WaypointsIntegration implements AddonIntegration {
  private static boolean enabled = false;
  private final List<WaypointMeta> fvpWaypoints = new ArrayList<>();

  @Subscribe
  public void onWaypointRequest(CreateLocationWaypointEvent event) {
    WaypointService waypointService = Waypoints.getReferences().waypointService();
    WaypointMeta waypointMeta = new WaypointMeta(
        Component.text(event.getDisplayName()),
        Color.ORANGE,
        WaypointType.SERVER_SESSION,
        new FloatVector3(
            event.getRequestedLocation().getX(),
            event.getRequestedLocation().getY(),
            event.getRequestedLocation().getZ()
        ),
        true,
        waypointService.actualWorld(),
        waypointService.actualServer(),
        waypointService.actualDimension()
    );
    fvpWaypoints.add(waypointMeta);
    waypointService.addWaypoint(waypointMeta);
    waypointService.refreshWaypoints();
  }

  @Subscribe
  public void onRemoveWaypointsRequest(RequestEvent event) {
    if (event.getRequestType() != RequestEvent.RequestType.REMOVE_WAYPOINTS) {
      return;
    }
    WaypointService waypointService = Waypoints.getReferences().waypointService();
    for (WaypointMeta waypointMeta : fvpWaypoints) {
      waypointService.removeWaypoint(waypointMeta);
    }
    fvpWaypoints.clear();
    waypointService.refreshWaypoints();
  }

  @Override
  public void onIntegratedAddonEnable() {
    enabled = true;
    Laby.labyAPI().eventBus().registerListener(this);
  }

  @Override
  public void onIntegratedAddonDisable() {
    enabled = false;
    Laby.labyAPI().eventBus().unregisterListener(this);
  }

  @Override
  public void load() {}

  public static boolean isEnabled() {
    return enabled;
  }
}
