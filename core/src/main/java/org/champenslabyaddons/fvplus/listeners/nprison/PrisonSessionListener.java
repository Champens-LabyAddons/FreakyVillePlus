package org.champenslabyaddons.fvplus.listeners.nprison;

import net.labymod.api.event.Subscribe;
import org.champenslabyaddons.fvplus.configuration.SessionSubConfiguration;
import org.champenslabyaddons.fvplus.connection.ClientInfo;
import org.champenslabyaddons.fvplus.event.StatChangeEvent;
import org.champenslabyaddons.fvplus.session.AddonSession;
import org.champenslabyaddons.fvplus.util.FreakyVilleServer;

public class PrisonSessionListener {
  private final AddonSession addonSession;
  private final ClientInfo clientInfo;
  private final SessionSubConfiguration configuration;

  public PrisonSessionListener(AddonSession addonSession, ClientInfo clientInfo, SessionSubConfiguration configuration) {
    this.addonSession = addonSession;
    this.clientInfo = clientInfo;
    this.configuration = configuration;
  }

  @Subscribe
  public void onStatChange(StatChangeEvent event) {
    if (!this.clientInfo.isOnFreakyVille()) {
      return;
    }
    if (this.clientInfo.getCurrentServer() != FreakyVilleServer.PRISON) {
      return;
    }
    if (!this.configuration.includeStatsFromPrison().get()) {
      return;
    }
    if (this.addonSession.getPrisonSession().isEmpty()) {
      return;
    }
    if (event.getValue() == null) {
      return;
    }
    switch (event.getStat()) {
      case BALANCE_DIFFERENCE:
        this.addonSession.getPrisonSession().get().addBalanceDifference((Double) event.getValue());
        break;
      case BO_REWARDS_CLAIMED:
        this.addonSession.getPrisonSession().get().addBoRewardsClaimed((Integer) event.getValue());
        break;
      case HEADS_GOTTEN:
        this.addonSession.getPrisonSession().get().addHeadsGotten((Integer) event.getValue());
        break;
    }
  }

}
