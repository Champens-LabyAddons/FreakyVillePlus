package dk.fvtrademarket.fvplus.api.service.skill;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.enums.SkillType;
import dk.fvtrademarket.fvplus.api.service.Service;
import net.labymod.api.reference.annotation.Referenceable;
import java.util.Map;

@Referenceable
public interface SkillService extends Service {
  void registerSkill(PrisonSector sector,SkillType skillType, int[] experienceRequirements);

  int getExperienceRequirement(PrisonSector sector, SkillType skillType, int level);

  Map<SkillType, int[]> getExperienceRequirements(PrisonSector sector);

  void increaseRecentGain(PrisonSector sector, SkillType skillType, double gain);

  double getRecentGain(PrisonSector sector, SkillType skillType);
}
