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
import net.labymod.api.event.Subscribe;
import net.labymod.api.util.concurrent.task.Task;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuardVaultListener {

  private final ClientInfo clientInfo;
  private final DefaultActivatableService activatableService;
  private final Pattern hoursPattern, minutesPattern, secondsPattern;

  public GuardVaultListener(ClientInfo clientInfo, DefaultActivatableService activatableService) {
    this.clientInfo = clientInfo;
    this.activatableService = activatableService;
    this.hoursPattern = Pattern.compile("(\\d+) time");
    this.minutesPattern = Pattern.compile("(\\d+) minut");
    this.secondsPattern = Pattern.compile("(\\d+) sekund");
  }

  @Subscribe
  public void onGuardVaultTryEvent(GuardVaultTryEvent event) {
    GuardVault guardVault = parseGuardVault(event);
    if (guardVault == null) {
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
    String message = event.getTimeLeftStr();
    int hours = readTimeFromString(message, this.hoursPattern);
    int minutes = readTimeFromString(message, this.minutesPattern);
    int seconds = readTimeFromString(message, this.secondsPattern);

    LocalDateTime endTime = LocalDateTime.now().plusHours(hours).plusMinutes(minutes).plusSeconds(seconds);
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
    String playerName = clientInfo.getClientPlayer().orElseThrow().getName();
    return playerName.equals(robberName);
  }

  private int readTimeFromString(String timer, Pattern pattern) {
    int amount = 0;
    Matcher matcher = pattern.matcher(timer);
    if (matcher.find()) {
      amount = Integer.parseInt(matcher.group(1));
    }
    return amount;
  }
}
