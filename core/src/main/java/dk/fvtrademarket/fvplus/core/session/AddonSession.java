package dk.fvtrademarket.fvplus.core.session;

import dk.fvtrademarket.fvplus.core.session.server.PrisonSession;
import java.util.Optional;

public class AddonSession {
  private final long startTime;
  private long endTime;
  private long duration;
  private PrisonSession prisonSession;

  public AddonSession() {
    this.startTime = System.currentTimeMillis();
  }

  public long getStartTime() {
    return this.startTime;
  }

  public long getEndTime() {
    return this.endTime;
  }

  public long getDuration() {
    return this.duration;
  }

  public Optional<PrisonSession> getPrisonSession() {
    return Optional.ofNullable(this.prisonSession);
  }

  public void endSession() {
    this.endTime = System.currentTimeMillis();
    this.duration = this.endTime - this.startTime;
  }

  public void initPrisonSession() {
    if (this.prisonSession != null) {
      return;
    }
    this.prisonSession = new PrisonSession();
  }
}
