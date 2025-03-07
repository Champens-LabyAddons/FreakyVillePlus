package dk.fvtrademarket.fvplus.core.util;

import net.labymod.api.util.I18n;
import java.time.Duration;

public final class NumberUtil {

  private static final String FORMAT_KEY = "fvplus.widgets.timer.timeLeft.";

  private NumberUtil() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static String convertLongToTimeString(long time) {
    StringBuilder timeLeft = new StringBuilder();
    if (time >= 0) {
      Duration timeLeftDuration = Duration.ofMillis(time);
      if (timeLeftDuration.toHours() > 0) {
        timeLeft.append(timeLeftDuration.toHours())
            .append(I18n.translate(FORMAT_KEY + "format.hours")).append(", ");
      }
      if (timeLeftDuration.toMinutes() > 0) {
        timeLeft.append(timeLeftDuration.toMinutes() % 60)
            .append(I18n.translate(FORMAT_KEY + "format.minutes")).append(", ");
      }
      timeLeft.append(timeLeftDuration.getSeconds() % 60).append(I18n.translate(FORMAT_KEY + "format.seconds"));
    } else {
      return "";
    }
      return timeLeft.toString();
  }

  public static String convertNumberToSimpleString(double number) {
    if (number < 1000) {
      return String.valueOf(number);
    }
    if (number < 1000000) {
      return String.format("%.1fK", number / 1000);
    }
    return String.format("%.1fM", number / 1000000);
  }

}
