package dk.fvtrademarket.fvplus.core.poi;

import net.labymod.api.util.I18n;
import net.labymod.api.util.Pair;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.util.Client;
import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Repræsenterer et Point of Interest (POI) på FreakyVille. Et POI er et sted med en handling som
 * spilleren kan udføre. I senere udgivelser vil der nok ske ændringer i hvordan vi håndterer
 * POI'er, da et POI potientielt kunne beskrives som en hvilken som helst lokation på serveren.
 */
public class POI {

  private final String identifier;
  private final String displayName;
  /**
   * Den fulde besked klienten modtager når en POI aktiveres, uden spillernavnet.
   */
  private final Pair<String, String> activationPair;
  /**
   * Den fulde besked klienten modtager når en POI gennemføres, uden spillernavnet.
   */
  private final Pair<String, String> confirmationPair;
  /**
   * Den besked klienten modtager når en POI er under cooldown.
   */
  private final String updateTimerMessage;
  private final int timedCooldown;
  private LocalDateTime activatableAtTimeAll;
  private LocalDateTime activatableAtTimePersonal;
  private final int waitTimeUponFailure;
  private final int expectedTimeToFinish;
  private final int personalTimedCooldown;
  private final FreakyVilleServer assosciatedServer;

  public POI(String identifier, String displayName,
      Pair<String, String> activationPair, Pair<String, String> confirmationPair,
      String updateTimerMessage,
      int timedCooldown, int waitTimeUponFailure, int expectedTimeToFinish,
      int personalTimedCooldown,
      FreakyVilleServer assosciatedServer) {
    this.identifier = identifier;
    this.displayName = displayName;
    this.activationPair = activationPair;
    this.confirmationPair = confirmationPair;
    this.updateTimerMessage = updateTimerMessage;
    this.timedCooldown = timedCooldown;
    this.waitTimeUponFailure = waitTimeUponFailure;
    this.expectedTimeToFinish = expectedTimeToFinish;
    this.personalTimedCooldown = personalTimedCooldown;
    this.assosciatedServer = assosciatedServer;
  }

  public String getIdentifier() {
    return identifier;
  }

  public String getDisplayName() {
    return displayName;
  }

  public Pair<String, String> getActivationPair() {
    return activationPair;
  }

  public Pair<String, String> getConfirmationPair() {
    return confirmationPair;
  }

  public String getUpdateTimerMessage() {
    return updateTimerMessage;
  }

  public int getTimedCooldown() {
    return timedCooldown;
  }

  public LocalDateTime getActivatableAtTimeAll() {
    return activatableAtTimeAll;
  }

  public void setActivatableAtTimeAll(LocalDateTime activatableAtTimeAll) {
    this.activatableAtTimeAll = activatableAtTimeAll;
  }

  public LocalDateTime getActivatableAtTimePersonal() {
    return activatableAtTimePersonal;
  }

  public void setActivatableAtTimePersonal(LocalDateTime activatableAtTimePersonal) {
    this.activatableAtTimePersonal = activatableAtTimePersonal;
  }

  public int getWaitTimeUponFailure() {
    return waitTimeUponFailure;
  }

  public int getExpectedTimeToFinish() {
    return expectedTimeToFinish;
  }

  public int getPersonalTimedCooldown() {
    return personalTimedCooldown;
  }

  public FreakyVilleServer getAssosciatedServer() {
    return assosciatedServer;
  }

  public String getTimeLeft(ClientInfo clientInfo) {
    if (this.activatableAtTimeAll == null) {
      return null;
    } else {
      Duration duration = Duration.between(LocalDateTime.now(), this.activatableAtTimeAll);
      long minutes = duration.toMinutes();
      long seconds = duration.getSeconds();
      long subtractedMinutes = minutes * 60L;
      seconds -= subtractedMinutes;
      String isTakeable = I18n.translate("fvplus.widgets.timer.timeLeft.isTakeable");
      String timeFormatAll = I18n.translate("fvplus.widgets.timer.timeLeft.format.all",
          I18n.translate("fvplus.widgets.timer.timeLeft.format.minutes", minutes),
          I18n.translate("fvplus.widgets.timer.timeLeft.format.seconds", seconds));
      String cantCheckForSure = "";
      if (!Client.poiCanBeUpdated(clientInfo, this)) {
        cantCheckForSure =
            " (" + I18n.translate("fvplus.widgets.timer.timeLeft.cantKnowForSure") + ")";
      }
      if (duration.isNegative() || duration.isZero()) {
        return isTakeable + cantCheckForSure;
      } else {
        return timeFormatAll;
      }
    }
  }

  public String getPersonalTimeLeft() {
    if (this.activatableAtTimePersonal == null) {
      return null;
    } else {
      LocalDateTime now = LocalDateTime.now();
      Duration duration = Duration.between(now, this.activatableAtTimePersonal);
      if (duration.isNegative()) {
        return I18n.translate("fvplus.widgets.timer.timeLeft.isTakeable");
      } else {
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);
        long seconds = duration.getSeconds();
        String message = "";
        if (hours > 0L) {
          message =
              message + I18n.translate("fvplus.widgets.timer.timeLeft.format.hours", hours) + ", ";
        }

        if (hours > 0L || minutes > 0L) {
          message =
              message + I18n.translate("fvplus.widgets.timer.timeLeft.format.minutes", minutes)
                  + ", ";
        }

        message = message + I18n.translate("fvplus.widgets.timer.timeLeft.format.seconds", seconds);
        return message;
      }
    }
  }
}
