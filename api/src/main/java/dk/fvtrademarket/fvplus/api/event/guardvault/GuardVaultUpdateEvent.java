package dk.fvtrademarket.fvplus.api.event.guardvault;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import net.labymod.api.event.Event;

public class GuardVaultUpdateEvent extends GuardVaultEvent {
  private final PrisonSector prisonSector;
  private final short hoursLeft;
  private final short minutesLeft;
  private final short secondsLeft;

  public GuardVaultUpdateEvent(PrisonSector prisonSector, short hoursLeft, short minutesLeft, short secondsLeft) {
    super(prisonSector, "");
    this.prisonSector = prisonSector;
    this.hoursLeft = hoursLeft;
    this.minutesLeft = minutesLeft;
    this.secondsLeft = secondsLeft;
  }

  public PrisonSector getPrisonSector() {
    return prisonSector;
  }

  public short getHoursLeft() {
    return hoursLeft;
  }

  public short getMinutesLeft() {
    return minutesLeft;
  }

  public short getSecondsLeft() {
    return secondsLeft;
  }
}
