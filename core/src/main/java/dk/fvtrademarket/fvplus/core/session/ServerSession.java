package dk.fvtrademarket.fvplus.core.session;

import net.labymod.api.util.Pair;
import java.util.ArrayList;
import java.util.Objects;

public abstract class ServerSession {
  private long startTime;
  private long duration;
  private final ArrayList<Pair<Long, Long>> sessionIntervals;

  public ServerSession() {
    this.startTime = System.currentTimeMillis();
    this.sessionIntervals = new ArrayList<>();
  }

  public long getStartTime() {
    return this.startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long getDuration() {
    return this.duration;
  }

  public void endSession() {
    addSessionInterval(this.startTime, System.currentTimeMillis());
    updateDuration();
  }

  public void addSessionInterval(Long start, Long end) {
    this.sessionIntervals.add(Pair.of(start, end));
  }

  public void updateDuration() {
    long duration = 0;
    for (Pair<Long, Long> interval : this.sessionIntervals) {
      duration += Objects.requireNonNull(interval.getSecond()) - Objects.requireNonNull(interval.getFirst());
    }
    this.duration = duration;
  }
}
