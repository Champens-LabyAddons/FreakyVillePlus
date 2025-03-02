package dk.fvtrademarket.fvplus.api.event.prison.skill;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.enums.SkillType;
import dk.fvtrademarket.fvplus.api.event.prison.SectoredEvent;
import net.labymod.api.event.Event;

public class SkillExperienceGainEvent extends SectoredEvent implements Event {
  private final double experience;
  private final SkillType type;
  private final boolean xpScrollUsed;
  private final boolean treasureDrop;

  public SkillExperienceGainEvent(PrisonSector sector, double experience, SkillType type, boolean xpScrollUsed, boolean treasureDrop) {
    super(sector);
    this.experience = experience;
    this.type = type;
    this.xpScrollUsed = xpScrollUsed;
    this.treasureDrop = treasureDrop;
  }

  public double getExperience() {
    return this.experience;
  }

  public SkillType getType() {
    return this.type;
  }

  public boolean isXpScrollUsed() {
    return this.xpScrollUsed;
  }

  public boolean isTreasureDrop() {
    return this.treasureDrop;
  }
}
