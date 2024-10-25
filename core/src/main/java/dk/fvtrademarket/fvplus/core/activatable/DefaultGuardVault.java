package dk.fvtrademarket.fvplus.core.activatable;

import dk.fvtrademarket.fvplus.api.activatable.guardvault.GuardVault;
import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.util.I18n;

public class DefaultGuardVault implements GuardVault {
  private final PrisonSector prisonSector;
  private final PrisonSector[] visiblePrisonSectors;

  private final int expectedActivationTime, cooldownUponFailure, personalCooldown, sharedCooldown;

  public DefaultGuardVault(PrisonSector prisonSector, PrisonSector[] visiblePrisonSectors,
      int expectedActivationTime, int cooldownUponFailure, int personalCooldown, int sharedCooldown) {
    this.prisonSector = prisonSector;
    this.visiblePrisonSectors = visiblePrisonSectors;
    this.expectedActivationTime = expectedActivationTime;
    this.cooldownUponFailure = cooldownUponFailure;
    this.personalCooldown = personalCooldown;
    this.sharedCooldown = sharedCooldown;
  }

  @Override
  public PrisonSector getPrisonSector() {
    return this.prisonSector;
  }

  @Override
  public PrisonSector[] getVisiblePrisonSectors() {
    return this.visiblePrisonSectors;
  }

  @Override
  public FreakyVilleServer getAssociatedServer() {
    return FreakyVilleServer.PRISON;
  }

  @Override
  public int getExpectedActivationTime() {
    return this.expectedActivationTime;
  }

  @Override
  public int getCooldownUponFailure() {
    return this.cooldownUponFailure;
  }

  @Override
  public int getPersonalCooldown() {
    return this.personalCooldown;
  }

  @Override
  public int getSharedCooldown() {
    return this.sharedCooldown;
  }

  @Override
  public Component toComponent() {
    String key = "fvplus.activatable.";
    if (this.prisonSector == PrisonSector.A_PLUS || this.prisonSector == PrisonSector.B_PLUS) {
      key += "bank";
    } else {
      key += "guardVault";
    }
    return TextComponent.builder()
            .append(getPrisonSector().toComponent())
            .append(Component.space())
            .append(Component.text(I18n.translate(key), NamedTextColor.GRAY))
            .build();
  }
}
