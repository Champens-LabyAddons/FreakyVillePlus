package dk.fvtrademarket.fvplus.core.widgets;

import dk.fvtrademarket.fvplus.api.activatable.Activatable;
import dk.fvtrademarket.fvplus.api.activatable.guardvault.GuardVault;
import dk.fvtrademarket.fvplus.api.activatable.misc.GangArea;
import dk.fvtrademarket.fvplus.api.service.Service;
import dk.fvtrademarket.fvplus.api.service.activatable.ActivatableService;
import dk.fvtrademarket.fvplus.core.widgets.factory.TimerHudWidgetPatterns;
import net.labymod.api.Laby;
import java.util.HashMap;
import java.util.Map;

public class Widgets implements Service {

  private final ActivatableService activatableService;
  private final Map<Activatable, TimerHudWidget> widgets;
  private final TimerHudWidgetPatterns timerHudWidgetPatterns;

  private static Widgets instance = null;

  public Widgets(ActivatableService activatableService) {
    if (instance != null) {
      throw new IllegalStateException("Widgets already initialized");
    }
    this.activatableService = activatableService;
    this.widgets = new HashMap<>();
    this.timerHudWidgetPatterns = new TimerHudWidgetPatterns();

    instance = this;
  }

  public void setTimer(Activatable activatable, long endTime) {
    TimerHudWidget widget = this.widgets.get(activatable);
    if (widget == null) {
      widget = createWidget(activatable);
      this.widgets.put(activatable, widget);
    }
    widget.setEndTime(endTime);
  }

  @Override
  public void initialize() {
    for (Activatable activatable : this.activatableService.getAllActivatables()) {
      TimerHudWidget widget = createWidget(activatable);
      this.widgets.put(activatable, widget);
      Laby.labyAPI().hudWidgetRegistry().register(widget);
    }
  }

  @Override
  public void shutdown() {

  }

  private TimerHudWidget createWidget(Activatable activatable) {
    if (activatable instanceof GuardVault) {
      return this.timerHudWidgetPatterns.createTimerHudWidget((GuardVault) activatable);
    } else if (activatable instanceof GangArea) {
      return this.timerHudWidgetPatterns.createTimerHudWidget((GangArea) activatable);
    }
    return this.timerHudWidgetPatterns.createTimerHudWidget(activatable);
  }
}
