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

  private byte maxFishingLevel_C = 0;
  private byte maxMiningLevel_C = 0;

  private byte maxFishingLevel_B = 0;
  private byte maxMiningLevel_B = 0;
  private byte maxRespectLevel_B = 0;

  private byte maxFishingLevel_A = 0;
  private byte maxRespectLevel_A = 0;

  private double fishingExperience = 0;
  private double miningExperience = 0;
  private double respectExperience = 0;

  private double fishingRequirement = -1;
  private double miningRequirement = -1;
  private double respectRequirement = -1;

  private byte fishingLevel = 0;
  private byte miningLevel = 0;
  private byte respectLevel = 0;

  private final Map<SkillType, BlockingQueue<GainEntry>> recentGains = new HashMap<>();

  private static final long GAIN_EXPIRY_TIME = TimeUnit.SECONDS.toMillis(2);

  private boolean initialized;

  @Override
  public void updateExperience(SkillType skillType, double experience) {
    switch (skillType) {
      case FISHING -> fishingExperience = experience;
      case MINING -> miningExperience = experience;
      case RESPECT -> respectExperience = experience;
    }
  }

  @Override
  public void updateRequirement(SkillType skillType, double requirement) {
    switch (skillType) {
      case FISHING -> fishingRequirement = requirement;
      case MINING -> miningRequirement = requirement;
      case RESPECT -> respectRequirement = requirement;
    }
  }

  @Override
  public void updateLevel(SkillType skillType, byte level) {
    switch (skillType) {
      case FISHING -> fishingLevel = level;
      case MINING -> miningLevel = level;
      case RESPECT -> respectLevel = level;
    }
  }

  @Override
  public double getExperience(SkillType skillType) {
    return switch (skillType) {
      case FISHING -> fishingExperience;
      case MINING -> miningExperience;
      case RESPECT -> respectExperience;
    };
  }

  @Override
  public double getExperienceRequirement(SkillType skillType) {
    return switch (skillType) {
      case FISHING -> (int) fishingRequirement;
      case MINING -> (int) miningRequirement;
      case RESPECT -> (int) respectRequirement;
    };
  }

  @Override
  public byte getLevel(SkillType skillType) {
    return switch (skillType) {
      case FISHING -> fishingLevel;
      case MINING -> miningLevel;
      case RESPECT -> respectLevel;
    };
  }

  @Override
  public void increaseRecentGain(SkillType skillType, double gain) {
    GainEntry gainEntry = new GainEntry(gain, System.currentTimeMillis());
    getRecentGainsQueue(skillType).add(gainEntry);
  }

  @Override
  public double getRecentGain(SkillType skillType) {
    BlockingQueue<GainEntry> queue = getRecentGainsQueue(skillType);
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
  public byte getMaxLevel(PrisonSector sector, SkillType skillType) {
    return switch (sector) {
      case C -> switch (skillType) {
        case FISHING -> maxFishingLevel_C;
        case MINING -> maxMiningLevel_C;
        default -> 0;
      };
      case B -> switch (skillType) {
        case FISHING -> maxFishingLevel_B;
        case MINING -> maxMiningLevel_B;
        case RESPECT -> maxRespectLevel_B;
        default -> 0;
      };
      case A -> switch (skillType) {
        case FISHING -> maxFishingLevel_A;
        case RESPECT -> maxRespectLevel_A;
        default -> 0;
      };
      default -> 0;
    };
  }

  @Override
  public void initialize() {
    if (initialized) {
      throw new IllegalStateException("Service already initialized");
    }
    ArrayList<String[]> cSkillRequirements = DataFormatter.csv(Resource.C_SKILL_MAX_LEVELS.toString(), true);
    ArrayList<String[]> bSkillRequirements = DataFormatter.csv(Resource.B_SKILL_MAX_LEVELS.toString(), true);
    ArrayList<String[]> aSkillRequirements = DataFormatter.csv(Resource.A_SKILL_MAX_LEVELS.toString(), true);

    for (String[] requirement : cSkillRequirements) {
      switch (requirement[0]) {
        case "FISHING" -> maxFishingLevel_C = Byte.parseByte(requirement[1]);
        case "MINING" -> maxMiningLevel_C = Byte.parseByte(requirement[1]);
      }
    }

    for (String[] requirement : bSkillRequirements) {
      switch (requirement[0]) {
        case "FISHING" -> maxFishingLevel_B = Byte.parseByte(requirement[1]);
        case "MINING" -> maxMiningLevel_B = Byte.parseByte(requirement[1]);
        case "RESPECT" -> maxRespectLevel_B = Byte.parseByte(requirement[1]);
      }
    }

    for (String[] requirement : aSkillRequirements) {
      switch (requirement[0]) {
        case "FISHING" -> maxFishingLevel_A = Byte.parseByte(requirement[1]);
        case "RESPECT" -> maxRespectLevel_A = Byte.parseByte(requirement[1]);
      }
    }

    initialized = true;
  }

  @Override
  public void shutdown() {

  }

  private BlockingQueue<GainEntry> getRecentGainsQueue(SkillType skillType) {
    return recentGains.computeIfAbsent(skillType, k -> new LinkedBlockingQueue<>());
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
