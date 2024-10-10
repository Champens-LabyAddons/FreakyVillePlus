package dk.fvtrademarket.fvplus.core.session.server;

import dk.fvtrademarket.fvplus.core.session.ServerSession;

public class PrisonSession extends ServerSession {
  private double balanceDifference;
  private int boRewardsClaimed;
  private int headsGotten;

  public PrisonSession() {
    super();
    this.balanceDifference = 0;
    this.boRewardsClaimed = 0;
    this.headsGotten = 0;
  }

  public double getBalanceDifference() {
    return this.balanceDifference;
  }

  public int getBoRewardsClaimed() {
    return this.boRewardsClaimed;
  }

  public int getHeadsGotten() {
    return this.headsGotten;
  }

  public void addBalanceDifference(double balanceDifference) {
    this.balanceDifference += balanceDifference;
  }

  public void addBoRewardsClaimed(int boRewardsClaimed) {
    this.boRewardsClaimed += boRewardsClaimed;
  }

  public void addHeadsGotten(int headsGotten) {
    this.headsGotten += headsGotten;
  }
}
