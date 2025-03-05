package dk.fvtrademarket.fvplus.core.skill;

import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.enums.SkillType;

public class SkillProfile {
  private PrisonSector sector;

  private int miningLevel;
  private int fishingLevel;
  private int respectLevel;

  private double miningExperience;
  private double fishingExperience;
  private double respectExperience;

  private SkillProfile(PrisonSector sector, int miningLevel, int fishingLevel, int respectLevel, double miningExperience, double fishingExperience, double respectExperience) {
    this.sector = sector;
    this.miningLevel = miningLevel;
    this.fishingLevel = fishingLevel;
    this.respectLevel = respectLevel;
    this.miningExperience = miningExperience;
    this.fishingExperience = fishingExperience;
    this.respectExperience = respectExperience;
  }

  public static SkillProfile createDefault() {
    return new SkillProfile(PrisonSector.C, 0, 0, 0,0, 0, 0);
  }

  public static SkillProfile createDefault(PrisonSector sector) {
    return new SkillProfile(sector, 0, 0, 0,0, 0, 0);
  }

  public PrisonSector getSector() {
    return this.sector;
  }

  public int getMiningLevel() {
    return this.miningLevel;
  }

  public int getFishingLevel() {
    return this.fishingLevel;
  }

  public int getRespectLevel() {
    return this.respectLevel;
  }

  public double getMiningExperience() {
    return this.miningExperience;
  }

  public double getFishingExperience() {
    return this.fishingExperience;
  }

  public double getRespectExperience() {
    return this.respectExperience;
  }

  public void setSector(PrisonSector sector) {
    this.sector = sector;
  }

  public void setMiningLevel(int miningLevel) {
    this.miningLevel = miningLevel;
  }

  public void setFishingLevel(int fishingLevel) {
    this.fishingLevel = fishingLevel;
  }

  public void setRespectLevel(int respectLevel) {
    this.respectLevel = respectLevel;
  }

  public void setMiningExperience(double miningExperience) {
    this.miningExperience = miningExperience;
  }

  public void setFishingExperience(double fishingExperience) {
    this.fishingExperience = fishingExperience;
  }

  public void setRespectExperience(double respectExperience) {
    this.respectExperience = respectExperience;
  }

  public void addExperience(SkillType skillType, double experience) {
    switch (skillType) {
      case MINING:
        this.miningExperience += experience;
        break;
      case FISHING:
        this.fishingExperience += experience;
        break;
      case RESPECT:
        this.respectExperience += experience;
        break;
    }
  }

  public void removeExperience(SkillType skillType, double experience) {
    switch (skillType) {
      case MINING:
        this.miningExperience -= experience;
        break;
      case FISHING:
        this.fishingExperience -= experience;
        break;
      case RESPECT:
        this.respectExperience -= experience;
        break;
    }
  }

  public void incrementLevel(SkillType skillType) {
    switch (skillType) {
      case MINING:
        this.miningLevel++;
        break;
      case FISHING:
        this.fishingLevel++;
        break;
      case RESPECT:
        this.respectLevel++;
        break;
    }
  }
}
