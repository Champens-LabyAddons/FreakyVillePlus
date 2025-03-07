package dk.fvtrademarket.fvplus.api.event.prison.guardvault;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;

public class GuardVaultUpdateEvent extends GuardVaultEvent {
  private final PrisonSector prisonSector;
  private final byte hoursLeft;
  private final byte minutesLeft;
  private final byte secondsLeft;

  public GuardVaultUpdateEvent(PrisonSector prisonSector, byte hoursLeft, byte minutesLeft, byte secondsLeft) {
    super(prisonSector, "");
    this.prisonSector = prisonSector;
    this.hoursLeft = hoursLeft;
    this.minutesLeft = minutesLeft;
    this.secondsLeft = secondsLeft;
  }

  public PrisonSector getPrisonSector() {
    return prisonSector;
  }

  public byte getHoursLeft() {
    return hoursLeft;
  }

  public byte getMinutesLeft() {
    return minutesLeft;
  }

  public byte getSecondsLeft() {
    return secondsLeft;
  }
}
