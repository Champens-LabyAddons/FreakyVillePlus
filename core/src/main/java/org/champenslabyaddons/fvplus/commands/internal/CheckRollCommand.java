package org.champenslabyaddons.fvplus.commands.internal;

import com.google.gson.Gson;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.util.I18n;
import net.labymod.api.util.logging.Logging;
import org.champenslabyaddons.fvplus.commands.FreakyVillePlusCommand;
import org.champenslabyaddons.fvplus.connection.ClientInfo;
import org.champenslabyaddons.fvplus.objects.WheelData;
import org.champenslabyaddons.fvplus.objects.WheelData.Wheel;
import org.champenslabyaddons.fvplus.util.Messaging;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class CheckRollCommand extends FreakyVillePlusCommand {
  private final ClientInfo clientInfo;

  public CheckRollCommand(ClientInfo clientInfo) {
    super("checkRoll", "",  "cr", "roll");
    this.clientInfo = clientInfo;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.clientInfo.isOnFreakyVille()) return false;
    if (arguments.length < 1) {
      displayTranslatable("usage", NamedTextColor.RED);
      return true;
    }
    if (arguments[0].length() != 36) {
      displayTranslatable("invalidUuid", NamedTextColor.RED);
      return true;
    }
    String uniqueId = arguments[0];

    CompletableFuture.supplyAsync(() -> {
      try {
        URL url = new URL("https://freakyville.dk/api/wheel/wheel/" + uniqueId);
        InputStreamReader reader = new InputStreamReader(url.openStream());
        return new Gson().fromJson(reader, WheelData.class);
      } catch (Exception e) {
        throw new CompletionException(e);
      }
    }).exceptionally(ex -> {
      Logging.getLogger().error(I18n.translate("fvplus.logging.error.fetchingWheelData"), ex);
      return null;
    }).thenAcceptAsync(data -> {
      if (data != null) {
        displayInChat(data.getWheel());
      }
    });
    return true;
  }

  private void displayInChat(Wheel wheel) {
    String header = " -= [ " + I18n.getTranslation(getTranslationKey("header")) + " ] =-";
    ZonedDateTime timestamp = ZonedDateTime.parse(wheel.getCreatedAt(), DateTimeFormatter.ISO_ZONED_DATE_TIME);
    String formattedTimestamp = timestamp.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    Component time = Component.text(formattedTimestamp, NamedTextColor.GRAY);
    Component id = Component.text(wheel.getId(), NamedTextColor.AQUA);
    Component wheelID = Component.translatable(getTranslationKey("wheel.header"), NamedTextColor.GOLD, id);
    displayMessage(Component.text(header, NamedTextColor.GOLD));
    displayMessage(time);
    displayMessage(wheelID);
    for (int i = 0; i < wheel.getOptions().length; i++) {
      if (wheel.getOptions().length > 10) {
        displayTranslatable("wheel.tooManyOptions", NamedTextColor.RED);
        break;
      }
      Component option = Component.text(" - " + wheel.getOptions()[i].getOption(), NamedTextColor.GRAY);
      displayMessage(option);
    }
    int winnerIndex = Integer.parseInt(wheel.getWinner());
    Component winner = Component.text(
        " - " + I18n.getTranslation(getTranslationKey("wheel.winner"), wheel.getOptions()[winnerIndex].getOption()),
        NamedTextColor.GREEN);
    displayMessage(winner);
  }
}
