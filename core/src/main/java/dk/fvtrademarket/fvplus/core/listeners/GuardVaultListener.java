package dk.fvtrademarket.fvplus.core.listeners;

import dk.fvtrademarket.fvplus.api.event.guardvault.GuardVaultFinishEvent;
import dk.fvtrademarket.fvplus.api.event.guardvault.GuardVaultTryEvent;
import dk.fvtrademarket.fvplus.api.event.guardvault.GuardVaultUpdateEvent;
import dk.fvtrademarket.fvplus.api.service.activatable.ActivatableService;
import net.labymod.api.event.Subscribe;
import java.util.regex.Pattern;

public class GuardVaultListener {
  private final ActivatableService activatableService;
  private final Pattern hoursPattern, minutesPattern, secondsPattern;

  public GuardVaultListener(ActivatableService activatableService) {
    this.activatableService = activatableService;
    this.hoursPattern = Pattern.compile("(\\d+) time");
    this.minutesPattern = Pattern.compile("(\\d+) minut");
    this.secondsPattern = Pattern.compile("(\\d+) sekund");
  }

  @Subscribe
  public void onGuardVaultTryEvent(GuardVaultTryEvent event) {

  }

  @Subscribe
  public void onGuardVaultUpdateEvent(GuardVaultUpdateEvent event) {

  }

  @Subscribe
  public void onGuardVaultFinishEvent(GuardVaultFinishEvent event) {

  }
}
