package dk.fvtrademarket.fvplus.core.event;

import net.labymod.api.event.Event;

public class StatChangeEvent implements Event {
  private final Stat stat;
  private final Object value;

  public StatChangeEvent(Stat stat, Number value) {
    this.stat = stat;
    this.value = value;
  }

  public StatChangeEvent(Stat stat, Boolean value) {
    this.stat = stat;
    this.value = value;
  }

  public Stat getStat() {
    return this.stat;
  }

  public Object getValue() {
    return this.value;
  }

  public enum Stat {
    BALANCE_DIFFERENCE,
    BO_REWARDS_CLAIMED,
    HEADS_GOTTEN
  }
}
