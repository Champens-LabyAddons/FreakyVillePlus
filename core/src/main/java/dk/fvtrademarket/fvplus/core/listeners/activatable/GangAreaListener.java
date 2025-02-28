package dk.fvtrademarket.fvplus.core.listeners.activatable;

import dk.fvtrademarket.fvplus.api.activatable.prison.GangArea;
import dk.fvtrademarket.fvplus.api.activatable.prison.GuardVault;
import dk.fvtrademarket.fvplus.api.event.prison.gangarea.GangAreaFinishEvent;
import dk.fvtrademarket.fvplus.api.event.prison.gangarea.GangAreaTryEvent;
import dk.fvtrademarket.fvplus.api.event.prison.gangarea.GangAreaUpdateEvent;
import dk.fvtrademarket.fvplus.core.activatable.DefaultActivatableService;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;
import net.labymod.api.event.Subscribe;
import net.labymod.api.util.concurrent.task.Task;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

public class GangAreaListener extends AbstractPrisonActivatableListener<GangArea> {

  public GangAreaListener(ClientInfo clientInfo, LabyAPI labyAPI, DefaultActivatableService activatableService) {
    super(clientInfo, labyAPI, activatableService, GangArea.class);
  }

  @Subscribe
  public void onGangAreaTryEvent(GangAreaTryEvent event) {
    GangArea gangArea = parsePrisonActivatable(event);
    if (gangArea == null) {
      return;
    }
    this.activatableService.putActivatableInLimbo(gangArea);
    Task robbingTask = Task.builder(
        () -> {
          if (activatableService.getLimboingActivatables().contains(gangArea)) {
            activatableService.removeActivatableFromLimbo(gangArea);
            Laby.fireEvent(new GangAreaFinishEvent(event.getSector(), event.getTakerName(), false));
          }
        }
    ).delay(gangArea.getExpectedActivationTime() + 1, TimeUnit.SECONDS)
    .build();
    robbingTask.execute();
  }

  @Subscribe
  public void onGangAreaFinishEvent(GangAreaUpdateEvent event) {
    GangArea gangArea = parsePrisonActivatable(event);
    if (gangArea == null) {
      return;
    }

    LocalDateTime endTime = LocalDateTime.now()
        .plusHours(event.getHoursLeft())
        .plusMinutes(event.getMinutesLeft())
        .plusSeconds(event.getSecondsLeft());
    ZonedDateTime zonedDateTime = endTime.atZone(ZoneId.systemDefault());

    boolean personal = this.activatableService.isOnPersonalCooldown(gangArea);

    this.activatableService.putActivatableOnCooldown(gangArea, zonedDateTime.toInstant().toEpochMilli(), personal);
  }

  @Subscribe
  public void onGangAreaFinishEvent(GangAreaFinishEvent event) {
    GangArea gangArea = parsePrisonActivatable(event);
    if (gangArea == null) {
      return;
    }
    if (!event.wasSuccessFull()) {
      activatableService.putActivatableOnFailureCooldown(gangArea);
    } else {
      activatableService.putActivatableOnCooldown(gangArea, wasPersonal(event.getTakerName()));
    }
    activatableService.removeActivatableFromLimbo(gangArea);
  }
}
