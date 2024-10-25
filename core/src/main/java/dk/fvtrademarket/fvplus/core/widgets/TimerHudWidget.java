package dk.fvtrademarket.fvplus.core.widgets;

import net.labymod.api.Laby;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.util.I18n;
import java.time.Duration;

public class TimerHudWidget extends TextHudWidget<TextHudWidgetConfig> {

  private static final String FORMAT_KEY = "fvplus.widgets.timer.timeLeft.";

  private TextLine textLine;
  private final String timerName;
  private final Icon associatedIcon;

  private long endTime;

  public TimerHudWidget(String id, String timerName, Icon associatedIcon, long endTime) {
    super(id);
    this.timerName = timerName;
    this.associatedIcon = associatedIcon;
    this.endTime = endTime;

    this.bindCategory(Laby.labyAPI().hudWidgetRegistry().categoryRegistry().getById("fvplus_timer_category"));
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    String currentTimeLeft = this.getTimeLeft();
    boolean shouldShow = !currentTimeLeft.isEmpty();
    if (currentTimeLeft.isEmpty()) {
      currentTimeLeft = I18n.translate(FORMAT_KEY + "unavailable");
    }
    this.textLine = super.createLine(this.timerName, currentTimeLeft);
    if (shouldShow) {
      this.textLine.setState(State.VISIBLE);
    } else {
      this.textLine.setState(State.HIDDEN);
    }
    this.setIcon(this.associatedIcon);
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    if (isFinished()) {
      this.textLine.update(I18n.translate(FORMAT_KEY + "finished"));
      this.textLine.setState(State.VISIBLE);
    } else if (!this.getTimeLeft().isEmpty()) {
      this.textLine.update(this.getTimeLeft());
      this.textLine.setState(State.VISIBLE);
    } else if (this.textLine == null) {
      this.textLine = this.createLine(this.timerName, " ");
      this.textLine.setState(State.HIDDEN);
    } else if (this.getTimeLeft().isEmpty()) {
      this.textLine.update(I18n.translate(FORMAT_KEY + "unavailable"));
      this.textLine.setState(State.HIDDEN);
    }
  }

  private String getTimeLeft() {
    StringBuilder timeLeft = new StringBuilder();
    long timeLeftMillis = endTime - System.currentTimeMillis();
    if (timeLeftMillis >= 0) {
      Duration timeLeftDuration = Duration.ofMillis(timeLeftMillis);
      if (timeLeftDuration.toHours() > 0) {
        timeLeft.append(timeLeftDuration.toHours())
            .append(I18n.translate(FORMAT_KEY + "format.hours")).append(",");
      }
      if (timeLeftDuration.toMinutes() > 0) {
        timeLeft.append(timeLeftDuration.toMinutes() % 60)
            .append(I18n.translate(FORMAT_KEY + "format.minutes")).append(",");
      }
      timeLeft.append(timeLeftDuration.getSeconds() % 60).append(I18n.translate(FORMAT_KEY + "format.seconds"));
    } else {
      return "";
    }
    return timeLeft.toString();
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }

  private boolean isFinished() {
    return System.currentTimeMillis() >= endTime;
  }
}