package dk.fvtrademarket.fvplus.api.enums;

public enum SkillType {
  MINING,
  FISHING,
  RESPECT("RESPEKT"),
  ;

  private final String otherName;

  SkillType() {
    this.otherName = name();
  }

  SkillType(String otherName) {
    this.otherName = otherName;
  }

  public String getOtherName() {
    return otherName;
  }

  public static SkillType fromString(String string) {
    for (SkillType skillType : values()) {
      if (skillType.name().equalsIgnoreCase(string)) {
        return skillType;
      }
      if (skillType.otherName != null && skillType.otherName.equalsIgnoreCase(string)) {
        return skillType;
      }
    }
    return null;
  }
}
