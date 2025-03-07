package dk.fvtrademarket.fvplus.core.widgets;

import dk.fvtrademarket.fvplus.core.util.NumberUtil;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.util.I18n;
import org.jetbrains.annotations.NotNull;

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

    this.bindCategory(Laby.labyAPI().hudWidgetRegistry().categoryRegistry().getById("fvplus_misc_timer_category"));
  }

  public TimerHudWidget(String id, String timerName, Icon associatedIcon, long endTime, String categoryId) {
    this(id, timerName, associatedIcon, endTime);

    this.bindCategory(Laby.labyAPI().hudWidgetRegistry().categoryRegistry().getById(categoryId));

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

  @Override
  public @NotNull Component displayName() {
    return Component.text(this.timerName + " " + I18n.translate("fvplus.widgets.timer." + "displayName"));
  }

  private String getTimeLeft() {
    long timeLeftMillis = endTime - System.currentTimeMillis();
    return NumberUtil.convertLongToTimeString(timeLeftMillis);
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }

  private boolean isFinished() {
    return System.currentTimeMillis() >= endTime && endTime != -1;
  }
}