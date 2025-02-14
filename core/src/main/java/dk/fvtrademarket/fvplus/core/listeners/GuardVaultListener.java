package dk.fvtrademarket.fvplus.core.listeners;

import dk.fvtrademarket.fvplus.api.activatable.guardvault.GuardVault;
import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.event.guardvault.GuardVaultEvent;
import dk.fvtrademarket.fvplus.api.event.guardvault.GuardVaultFinishEvent;
import dk.fvtrademarket.fvplus.api.event.guardvault.GuardVaultTryEvent;
import dk.fvtrademarket.fvplus.api.event.guardvault.GuardVaultUpdateEvent;
import dk.fvtrademarket.fvplus.core.activatable.DefaultActivatableService;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;
import net.labymod.api.event.Subscribe;
import net.labymod.api.util.concurrent.task.Task;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class GuardVaultListener {

  private final ClientInfo clientInfo;
  private final LabyAPI labyAPI;
  private final DefaultActivatableService activatableService;

  public GuardVaultListener(ClientInfo clientInfo, LabyAPI labyAPI, DefaultActivatableService activatableService) {
    this.clientInfo = clientInfo;
    this.labyAPI = labyAPI;
    this.activatableService = activatableService;
  }

  @Subscribe
  public void onGuardVaultTryEvent(GuardVaultTryEvent event) {
    GuardVault guardVault = parseGuardVault(event);
    if (guardVault == null) {
      return;
    }
    if (guardVault.getPrisonSector() == PrisonSector.C || guardVault.getPrisonSector() == PrisonSector.B) {
      Laby.fireEvent(new GuardVaultFinishEvent(event.getSector(), event.getRobber(), true));
      return;
    }
    this.activatableService.putActivatableInLimbo(guardVault);
    Task robbingTask = Task.builder(
            () -> {
              if (activatableService.getLimboingActivatables().contains(guardVault)) {
                activatableService.removeActivatableFromLimbo(guardVault);
                Laby.fireEvent(new GuardVaultFinishEvent(event.getSector(), event.getRobber(), false));
              }
            }
        ).delay(guardVault.getExpectedActivationTime() + 1, TimeUnit.SECONDS)
        .build();
    robbingTask.execute();
  }

  @Subscribe
  public void onGuardVaultUpdateEvent(GuardVaultUpdateEvent event) {
    GuardVault guardVault = parseGuardVault(event);
    if (guardVault == null) {
      return;
    }

    LocalDateTime endTime = LocalDateTime.now()
        .plusHours(event.getHoursLeft())
        .plusMinutes(event.getMinutesLeft())
        .plusSeconds(event.getSecondsLeft());
    ZonedDateTime zonedDateTime = endTime.atZone(ZoneId.systemDefault());

    boolean personal = this.activatableService.isOnPersonalCooldown(guardVault);

    this.activatableService.putActivatableOnCooldown(guardVault, zonedDateTime.toInstant().toEpochMilli(), personal);
  }

  @Subscribe
  public void onGuardVaultFinishEvent(GuardVaultFinishEvent event) {
    GuardVault guardVault = parseGuardVault(event);
    if (guardVault == null) {
      return;
    }
    if (!event.wasSuccessFull()) {
      activatableService.putActivatableOnFailureCooldown(guardVault);
    } else {
      activatableService.putActivatableOnCooldown(guardVault, wasPersonal(event.getRobber()));
    }
    activatableService.removeActivatableFromLimbo(guardVault);
  }

  private GuardVault parseGuardVault(GuardVaultEvent event) {
    Optional<GuardVault> guardVault = getGuardVaultBySector(event.getSector());
    if (guardVault.isEmpty()) {
      return null;
    }
    if (this.clientInfo.getCurrentServer() != guardVault.get().getAssociatedServer()) {
      return null;
    }
    if (this.clientInfo.getCurrentServer() == FreakyVilleServer.PRISON && clientInfo.getPrisonSector().isEmpty()) {
      return null;
    }
    for (PrisonSector prisonSector : guardVault.get().getVisiblePrisonSectors()) {
      if (prisonSector == clientInfo.getPrisonSector().get()) {
        return guardVault.get();
      }
    }
    return null;
  }

  private Optional<GuardVault> getGuardVaultBySector(PrisonSector sector) {
    for (GuardVault guardVault : activatableService.getActivatables(GuardVault.class)) {
      if (guardVault.getPrisonSector() == sector) {
        return Optional.of(guardVault);
      }
    }
    return Optional.empty();
  }

  private boolean wasPersonal(String robberName) {
    String playerName = this.labyAPI.getName();
    return playerName.equals(robberName);
  }
}
