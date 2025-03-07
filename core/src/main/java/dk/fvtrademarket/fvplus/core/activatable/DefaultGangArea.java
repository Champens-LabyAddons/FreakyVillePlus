package dk.fvtrademarket.fvplus.core.activatable;

import dk.fvtrademarket.fvplus.api.activatable.prison.GangArea;
import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.util.I18n;

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
            .append(Component.text(I18n.translate("fvplus.activatable.gangArea"), NamedTextColor.GRAY))
            .build();
  }
}
