package dk.fvtrademarket.fvplus.api.enums;

import dk.fvtrademarket.fvplus.api.event.guardvault.GuardVaultFinishEvent;
import dk.fvtrademarket.fvplus.api.event.guardvault.GuardVaultTryEvent;
import dk.fvtrademarket.fvplus.api.event.guardvault.GuardVaultUpdateEvent;
import dk.fvtrademarket.fvplus.api.misc.EventMessage;
import net.labymod.api.event.Event;
import org.jetbrains.annotations.Nullable;

public enum FreakyVilleMessage implements EventMessage {
  // Vagt vault beskeder der handler om afslutningen af en VV bliver kun sendt når det blev gennemført uden fejl
  // Derfor er success altid true
  C_GUARD_VAULT_START(new GuardVaultTryEvent(PrisonSector.C, null)),
  C_GUARD_VAULT_UPDATE(new GuardVaultUpdateEvent(PrisonSector.C, null)),

  B_GUARD_VAULT_START(new GuardVaultTryEvent(PrisonSector.B, null)),
  B_GUARD_VAULT_UPDATE(new GuardVaultUpdateEvent(PrisonSector.B, null)),

  B_PLUS_GUARD_VAULT_START(new GuardVaultTryEvent(PrisonSector.B_PLUS, null)),
  B_PLUS_GUARD_VAULT_FINISH(new GuardVaultFinishEvent(PrisonSector.B_PLUS, null, true)),
  B_PLUS_GUARD_VAULT_UPDATE(new GuardVaultUpdateEvent(PrisonSector.B_PLUS, null)),

  A_GUARD_VAULT_START(new GuardVaultTryEvent(PrisonSector.A, null)),
  A_GUARD_VAULT_FINISH(new GuardVaultFinishEvent(PrisonSector.A, null, true)),
  A_GUARD_VAULT_UPDATE(new GuardVaultUpdateEvent(PrisonSector.A, null)),

  A_PLUS_GUARD_VAULT_START(new GuardVaultTryEvent(PrisonSector.A_PLUS, null)),
  A_PLUS_GUARD_VAULT_FINISH(new GuardVaultFinishEvent(PrisonSector.A_PLUS, null, true)),
  A_PLUS_GUARD_VAULT_UPDATE(new GuardVaultUpdateEvent(PrisonSector.A_PLUS, null)),
  ;

  private final @Nullable Event event;

  FreakyVilleMessage(@Nullable Event event) {
    this.event = event;
  }

  @Override
  public @Nullable Event getEvent() {
    return event;
  }

  public static FreakyVilleMessage fromString(String message) {
    for (FreakyVilleMessage freakyVilleMessage : values()) {
      if (freakyVilleMessage.name().equalsIgnoreCase(message)) {
        return freakyVilleMessage;
      }
    }
    return null;
  }
}
