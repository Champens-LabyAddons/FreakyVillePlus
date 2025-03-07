package dk.fvtrademarket.fvplus.api.service.skill;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.enums.SkillType;
import dk.fvtrademarket.fvplus.api.service.Service;
import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
public interface SkillService extends Service {

  void updateExperience(SkillType skillType, double experience);

  void updateRequirement(SkillType skillType, double requirement);

  void updateLevel(SkillType skillType, byte level);

  double getExperience(SkillType skillType);

  double getExperienceRequirement(SkillType skillType);

  byte getLevel(SkillType skillType);

  void increaseRecentGain(SkillType skillType, double gain);

  double getRecentGain(SkillType skillType);

  byte getMaxLevel(PrisonSector sector, SkillType skillType);
}
