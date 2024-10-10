package dk.fvtrademarket.fvplus.core.widgets;

import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine;
import net.labymod.api.client.gui.hud.hudwidget.text.TextLine.State;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.util.I18n;
import dk.fvtrademarket.fvplus.core.connection.ClientInfo;

public abstract class TimerWidget extends TextHudWidget<TextHudWidgetConfig> {

  private TextLine textLine;
  protected final ClientInfo clientInfo;
  private final Icon associatedIcon;

  public TimerWidget(String identifier, ClientInfo clientInfo, Icon associatedIcon) {
    super(identifier);
    this.clientInfo = clientInfo;
    this.associatedIcon = associatedIcon;
  }

  @Override
  public void load(TextHudWidgetConfig config) {
    super.load(config);
    String currentTimeLeft = this.getTimeLeft();
    boolean shouldShow = currentTimeLeft != null;
    if (currentTimeLeft == null) {
      currentTimeLeft = I18n.translate("fvplus.widgets.timer.timeLeft.unavailable");
    }
    this.textLine = super.createLine(this.getPOI(), currentTimeLeft);
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
    if (this.getTimeLeft() != null) {
      // @TODO: Tilføj en metode der automatisk checker om en spiller er en vagt, og hvis de er, så skal den ikke vise tiden.
      this.textLine.update(this.getTimeLeft());
      this.textLine.setState(State.VISIBLE);
    } else if (this.textLine == null) {
      this.textLine = this.createLine(this.getPOI(), " ");
      this.textLine.setState(State.HIDDEN);
    }
  }

  protected abstract String getTimeLeft();

  protected abstract String getPOI();
}
