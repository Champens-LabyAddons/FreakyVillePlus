package dk.fvtrademarket.fvplus.api.activatable.misc;

import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.NamedTextColor;

public class DefaultGangArea implements GangArea {
  private final PrisonSector prisonSector;

  private final int expectedActivationTime, cooldownUponFailure, personalCooldown, sharedCooldown;

  public DefaultGangArea(PrisonSector prisonSector,
      int expectedActivationTime, int cooldownUponFailure, int personalCooldown, int sharedCooldown) {
    this.prisonSector = prisonSector;
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
    return TextComponent.builder()
            .append(getPrisonSector().toComponent())
            .append(Component.space())
            .append(Component.translatable("fvplus.activatable.gangArea", NamedTextColor.GRAY))
            .build();
  }
}
