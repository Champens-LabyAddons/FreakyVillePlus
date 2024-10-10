package dk.fvtrademarket.fvplus.core.listeners.nprison;

import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.util.Pair;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;
import dk.fvtrademarket.fvplus.core.internal.PoiList;
import dk.fvtrademarket.fvplus.core.poi.POI;
import dk.fvtrademarket.fvplus.core.poi.PrisonPOI;
import dk.fvtrademarket.fvplus.core.util.Client;
import dk.fvtrademarket.fvplus.core.util.Messaging;
import dk.fvtrademarket.fvplus.core.util.Prison;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PoiListener {

  private final ClientInfo clientInfo;
  private final PoiList poiList;
  private final Pattern hoursPattern;
  private final Pattern minutesPattern;
  private final Pattern secondsPattern;

  public PoiListener(ClientInfo clientInfo, PoiList poiList) {
    this.clientInfo = clientInfo;
    this.poiList = poiList;
    this.hoursPattern = Pattern.compile("(\\d+) time");
    this.minutesPattern = Pattern.compile("(\\d+) minut");
    this.secondsPattern = Pattern.compile("(\\d+) sekund");
  }

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    if (!this.clientInfo.isOnFreakyVille()) {
      return;
    }
    String plainMessage = event.chatMessage().getPlainText().trim();
    for (POI poi : this.poiList.getPois()) {
      if (!isPoiMessage(poi, plainMessage)) {
        continue;
      }
      if (!poiAndPlayerLocationMatches(poi)) {
        continue;
      }
      handle(poi, plainMessage);
      return;
    }
  }

  private void handle(POI poi, String message) {
    if (!Client.poiCanBeUpdated(this.clientInfo, poi)) {
      Messaging.displayTranslatable("fvplus.server.prison.poi.willNotUpdate", NamedTextColor.RED);
      return;
    }
    String demystifiedMessage = message.replace(getPlayerFromString(poi, message), "").trim();
    if (demystifiedMessage.equals(pairToString(poi.getActivationPair()))) {
      activation(poi, message);
      return;
    } else if (demystifiedMessage.equals(pairToString(poi.getConfirmationPair()))) {
      confirmation(poi, message);
      return;
    }
    if (poi.getUpdateTimerMessage().isEmpty() ||
        poi.getUpdateTimerMessage().isBlank() ||
        poi.getUpdateTimerMessage() == null) {
      return;
    }
    if (message.startsWith(poi.getUpdateTimerMessage())) {
      update(poi, message);
    }
  }

  private void activation(POI poi, String message) {
    if (Objects.equals(poi.getConfirmationPair().getFirst(), "") && Objects.equals(
        poi.getConfirmationPair().getSecond(), "")) {
      poi.setActivatableAtTimeAll(LocalDateTime.now().plusMinutes(poi.getTimedCooldown() / 60));
      setPersonalCooldown(poi, message);
      return;
    }
    this.poiList.getLimboingPois().add(poi);
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    Runnable task = () -> {
      if (this.poiList.getLimboingPois().contains(poi)) {
        this.poiList.getLimboingPois().remove(poi);
        poi.setActivatableAtTimeAll(
            LocalDateTime.now().plusMinutes(poi.getWaitTimeUponFailure() / 60));
      }
    };
    executor.schedule(task, poi.getExpectedTimeToFinish() + 1, TimeUnit.SECONDS);
    executor.shutdown();
  }

  private void confirmation(POI poi, String message) {
    if (this.poiList.getLimboingPois().isEmpty()) {
      return;
    }
    this.poiList.getLimboingPois().forEach(
        limboingPoi -> {
          if (limboingPoi.equals(poi)) {
            limboingPoi.setActivatableAtTimeAll(
                LocalDateTime.now().plusMinutes(limboingPoi.getTimedCooldown() / 60));
            setPersonalCooldown(limboingPoi, message);
            this.poiList.getLimboingPois().remove(limboingPoi);
          }
        }
    );
  }

  private void update(POI poi, String message) {
    if (Objects.equals(poi.getUpdateTimerMessage(), "")) {
      return;
    }
    if (poi.getIdentifier().toUpperCase().contains("BO")) {
      return;
    }
    boolean isGlobal = !(poi.getActivatableAtTimePersonal() != null
        && poi.getActivatableAtTimePersonal().isAfter(LocalDateTime.now()));
    setTimer(poi, message, isGlobal);
  }

  private void setPersonalCooldown(POI poi, String message) {
    if (this.clientInfo.getClientPlayer().isEmpty()) {
      return;
    }
    ClientPlayer clientPlayer = this.clientInfo.getClientPlayer().get();
    if (message.contains(clientPlayer.getName())) {
      poi.setActivatableAtTimePersonal(
          LocalDateTime.now().plusMinutes(poi.getPersonalTimedCooldown() / 60));
    }
  }

  private int readTimeFromString(String timer, Pattern pattern) {
    int amount = 0;
    Matcher matcher = pattern.matcher(timer);
    if (matcher.find()) {
      amount = Integer.parseInt(matcher.group(1));
    }
    return amount;
  }

  private void setTimer(POI poi, String message, boolean isGlobal) {
    int hours = readTimeFromString(message, this.hoursPattern);
    int minutes = readTimeFromString(message, this.minutesPattern);
    int seconds = readTimeFromString(message, this.secondsPattern);
    if (isGlobal) {
      poi.setActivatableAtTimeAll(
          LocalDateTime.now().plusHours(hours).plusMinutes(minutes).plusSeconds(seconds));
    } else {
      poi.setActivatableAtTimePersonal(
          LocalDateTime.now().plusHours(hours).plusMinutes(minutes).plusSeconds(seconds));
    }
  }

  private String getPlayerFromString(POI poi, String message) {
    String player = "";
    if (message.startsWith(Objects.requireNonNull(poi.getActivationPair().getFirst())) &&
        message.endsWith(Objects.requireNonNull(poi.getActivationPair().getSecond()))) {
      player = message.substring(poi.getActivationPair().getFirst().length() + 1,
          message.length() - poi.getActivationPair().getSecond().length());
    } else if (message.startsWith(Objects.requireNonNull(poi.getConfirmationPair().getFirst())) &&
        message.endsWith(Objects.requireNonNull(poi.getConfirmationPair().getSecond()))) {
      player = message.substring(poi.getConfirmationPair().getFirst().length() + 1,
          message.length() - poi.getConfirmationPair().getSecond().length());
    }
    return player;
  }

  private String pairToString(Pair<String, String> pair) {
    return pair.getFirst() + " " + pair.getSecond();
  }

  private boolean isPoiMessage(POI poi, String message) {
    boolean isActivation =
        message.startsWith(Objects.requireNonNull(poi.getActivationPair().getFirst()))
            && message.endsWith(Objects.requireNonNull(poi.getActivationPair().getSecond()));
    boolean isUpdate;
    if (poi.getUpdateTimerMessage().isEmpty() ||
        poi.getUpdateTimerMessage().isBlank() ||
        poi.getUpdateTimerMessage() == null) {
      isUpdate = false;
    } else {
      isUpdate = message.startsWith(poi.getUpdateTimerMessage());
    }
    if (Objects.equals(poi.getConfirmationPair().getFirst(), "") && Objects.equals(
        poi.getConfirmationPair().getSecond(), "")) {
      return isActivation || isUpdate;
    }
    boolean isConfirmation =
        message.startsWith(Objects.requireNonNull(poi.getConfirmationPair().getFirst()))
            && message.endsWith(Objects.requireNonNull(poi.getConfirmationPair().getSecond()));
    return isActivation || isUpdate || isConfirmation;
  }

  private boolean poiAndPlayerLocationMatches(POI poi) {
    if (poi.getAssosciatedServer() != this.clientInfo.getCurrentServer()) {
      return false;
    }
    if (poi instanceof PrisonPOI) {
      if (clientInfo.getPrison().isEmpty()) {
        return false;
      }
      for (Prison prison : ((PrisonPOI) poi).getWhereCanItBeUpdated()) {
        if (prison == clientInfo.getPrison().get()) {
          return true;
        }
      }
      return false;
    }
    return true;
  }
}
