package dk.fvtrademarket.fvplus.core.widgets;

import dk.fvtrademarket.fvplus.api.activatable.Activatable;
import dk.fvtrademarket.fvplus.api.activatable.prison.GuardVault;
import dk.fvtrademarket.fvplus.api.activatable.prison.GangArea;
import dk.fvtrademarket.fvplus.api.service.Service;
import dk.fvtrademarket.fvplus.api.service.activatable.ActivatableService;
import dk.fvtrademarket.fvplus.core.widgets.factory.TimerHudWidgetPatterns;
import net.labymod.api.Laby;
import net.labymod.api.util.logging.Logging;
import java.util.HashMap;
import java.util.Map;

public class Widgets implements Service {

  private final ActivatableService activatableService;
  private final Map<Activatable, TimerHudWidget> widgets;
  private final TimerHudWidgetPatterns timerHudWidgetPatterns;

  private final Logging logger = Logging.create(this.getClass());

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

  public void register(Activatable activatable) {
    try {
      internalRegistration(activatable);
    } catch (Exception e) {
      this.logger.error("Failed to register activatable", e);
    }
  }

  private void internalRegistration(Activatable activatable) {
    if (this.widgets.containsKey(activatable)) {
      throw new IllegalStateException("Activatable already registered");
    }
    TimerHudWidget widget = createWidget(activatable);
    this.widgets.put(activatable, widget);
    Laby.labyAPI().hudWidgetRegistry().register(widget);
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
      internalRegistration(activatable);
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
