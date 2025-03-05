package dk.fvtrademarket.fvplus.api.enums;

public enum FreakyVilleMessage {
  C_GUARD_VAULT_START(),
  C_GUARD_VAULT_UPDATE(),

  B_GUARD_VAULT_START(),
  B_GUARD_VAULT_UPDATE(),

  B_PLUS_GUARD_VAULT_START(),
  B_PLUS_GUARD_VAULT_FINISH(),
  B_PLUS_GUARD_VAULT_UPDATE(),

  A_GUARD_VAULT_START(),
  A_GUARD_VAULT_FINISH(),
  A_GUARD_VAULT_UPDATE(),

  A_PLUS_GUARD_VAULT_START(),
  A_PLUS_GUARD_VAULT_FINISH(),
  A_PLUS_GUARD_VAULT_UPDATE(),

  GANG_AREA_TRY_UNSPECIFIED(),
  GANG_AREA_TRY_B(),
  GANG_AREA_TRY_B_PLUS(),
  GANG_AREA_TRY_A_PLUS(),

  GANG_AREA_UPDATE(),

  GANG_AREA_FINISH_UNSPECIFIED(),
  GANG_AREA_FINISH_B_PLUS(),
  GANG_AREA_FINISH_A_PLUS(),

  SKILL_EXPERIENCE_GAIN_GENERIC(true),
  SKILL_EXPERIENCE_GAIN_FISHING_SCROLL(true),
  SKILL_EXPERIENCE_GAIN_RESPECT_SCROLL(true),

  SKILL_LEVEL_UP(),

  LIVING_AREA_LOOKUP(true),
  LIVING_AREA_HELP(true),

  PLAYER_MESSAGE_ALL(true),
  PLAYER_MESSAGE_YOU(true),

  PRISON_CHECK(),

  UNRECOGNIZED();

  private final boolean cancelMessage;

  FreakyVilleMessage(boolean cancelMessage) {
    this.cancelMessage = cancelMessage;
  }

  FreakyVilleMessage() {
    this(false);
  }

  public boolean isMessageCancelled() {
    return cancelMessage;
  }

  public static FreakyVilleMessage fromString(String message) {
    for (FreakyVilleMessage freakyVilleMessage : values()) {
      if (freakyVilleMessage.name().equalsIgnoreCase(message)) {
        return freakyVilleMessage;
      }
    }
    return UNRECOGNIZED;
  }
}
