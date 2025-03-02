package dk.fvtrademarket.fvplus.core.skill;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.enums.SkillType;
import dk.fvtrademarket.fvplus.api.service.skill.SkillService;
import dk.fvtrademarket.fvplus.core.util.DataFormatter;
import dk.fvtrademarket.fvplus.core.util.Resource;
import net.labymod.api.models.Implements;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Singleton
@Implements(SkillService.class)
public class DefaultSkillService implements SkillService {

  private final Map<SkillType, int[]> experienceRequirementsC = new HashMap<>(3);
  private final Map<SkillType, int[]> experienceRequirementsB = new HashMap<>(3);
  private final Map<SkillType, int[]> experienceRequirementsA = new HashMap<>(3);

  private final Map<SkillType, BlockingQueue<GainEntry>> recentGainsC = new HashMap<>();
  private final Map<SkillType, BlockingQueue<GainEntry>> recentGainsB = new HashMap<>();
  private final Map<SkillType, BlockingQueue<GainEntry>> recentGainsA = new HashMap<>();

  private static final long GAIN_EXPIRY_TIME = TimeUnit.SECONDS.toMillis(5);

  private boolean initialized;

  @Override
  public void registerSkill(PrisonSector sector, SkillType skillType, int[] experienceRequirements) {
    switch (sector) {
      case C:
        experienceRequirementsC.put(skillType, experienceRequirements);
        break;
      case B:
        experienceRequirementsB.put(skillType, experienceRequirements);
        break;
      case A:
        experienceRequirementsA.put(skillType, experienceRequirements);
        break;
    }
  }

  @Override
  public int getExperienceRequirement(PrisonSector sector, SkillType skillType, int level) {
    if (level == 10) {
      return -1;
    }
    return switch (sector) {
      case C -> experienceRequirementsC.get(skillType)[level];
      case B -> experienceRequirementsB.get(skillType)[level];
      case A -> experienceRequirementsA.get(skillType)[level];
      default -> 0;
    };
  }

  @Override
  public Map<SkillType, int[]> getExperienceRequirements(PrisonSector sector) {
    return Map.copyOf(switch (sector) {
      case C -> experienceRequirementsC;
      case B -> experienceRequirementsB;
      case A -> experienceRequirementsA;
      default -> new HashMap<SkillType, int[]>();
    });
  }

  @Override
  public void increaseRecentGain(PrisonSector sector, SkillType skillType, double gain) {
    GainEntry gainEntry = new GainEntry(gain, System.currentTimeMillis());
    getRecentGainsQueue(sector, skillType).add(gainEntry);
  }

  @Override
  public double getRecentGain(PrisonSector sector, SkillType skillType) {
    BlockingQueue<GainEntry> queue = getRecentGainsQueue(sector, skillType);
    long currentTime = System.currentTimeMillis();
    double totalGain = 0;

    for (GainEntry entry : queue) {
      if (currentTime - entry.timestamp <= GAIN_EXPIRY_TIME) {
        totalGain += entry.gain;
      } else {
        queue.remove(entry);
      }
    }

    return totalGain;
  }

  @Override
  public void initialize() {
    if (initialized) {
      throw new IllegalStateException("Service already initialized");
    }
    ArrayList<String[]> cSkillRequirements = DataFormatter.csv(Resource.C_SKILL_REQUIREMENTS.toString(), true);
    ArrayList<String[]> bSkillRequirements = DataFormatter.csv(Resource.B_SKILL_REQUIREMENTS.toString(), true);
    ArrayList<String[]> aSkillRequirements = DataFormatter.csv(Resource.A_SKILL_REQUIREMENTS.toString(), true);

    readSkillRequirements(PrisonSector.C, cSkillRequirements);
    readSkillRequirements(PrisonSector.B, bSkillRequirements);
    readSkillRequirements(PrisonSector.A, aSkillRequirements);

    initialized = true;
  }

  @Override
  public void shutdown() {

  }

  private void readSkillRequirements(PrisonSector sector, ArrayList<String[]> skillRequirements) {
    for (String[] line : skillRequirements) {
      SkillType skillType = SkillType.fromString(line[0]);
      int[] requirements = new int[line.length - 1];

      for (int i = 1; i < line.length; i++) {
        if (line[i].equals("x")) {
          requirements[i - 1] = 0;
        } else {
          requirements[i - 1] = Integer.parseInt(line[i]);
        }
      }

      registerSkill(sector, skillType, requirements);
    }
  }

  private BlockingQueue<GainEntry> getRecentGainsQueue(PrisonSector sector, SkillType skillType) {
    return switch (sector) {
      case C -> recentGainsC.computeIfAbsent(skillType, k -> new LinkedBlockingQueue<>());
      case B -> recentGainsB.computeIfAbsent(skillType, k -> new LinkedBlockingQueue<>());
      case A -> recentGainsA.computeIfAbsent(skillType, k -> new LinkedBlockingQueue<>());
      default -> throw new IllegalArgumentException("Unknown sector: " + sector);
    };
  }

  private static class GainEntry {
    final double gain;
    final long timestamp;

    GainEntry(double gain, long timestamp) {
      this.gain = gain;
      this.timestamp = timestamp;
    }
  }
}
