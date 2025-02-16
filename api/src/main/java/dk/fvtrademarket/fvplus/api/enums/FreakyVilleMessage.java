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

  LIVING_AREA_LOOKUP(true),
  LIVING_AREA_HELP(true),

  PLAYER_MESSAGE_ALL(true),
  PLAYER_MESSAGE_YOU(true),

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
