package dk.fvtrademarket.fvplus.api.event.prison.skill;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.enums.SkillType;
import dk.fvtrademarket.fvplus.api.event.prison.SectoredEvent;
import net.labymod.api.event.Event;

public class SkillLevelUpEvent extends SectoredEvent implements Event {
  private final SkillType skillType;
  private final byte newLevel;

  public SkillLevelUpEvent(PrisonSector sector, SkillType skillType, byte newLevel) {
    super(sector);
    this.skillType = skillType;
    this.newLevel = newLevel;
  }

  public SkillType getSkillType() {
    return this.skillType;
  }

  public byte getNewLevel() {
    return this.newLevel;
  }
}
