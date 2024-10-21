package dk.fvtrademarket.fvplus.core.internal;

import dk.fvtrademarket.fvplus.api.activatable.guardvault.DefaultGuardVault;
import dk.fvtrademarket.fvplus.api.activatable.guardvault.GuardVault;
import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.service.activatable.ActivatableService;
import dk.fvtrademarket.fvplus.core.util.DataFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GuardVaultActivatableService implements ActivatableService<GuardVault> {

  private final Set<GuardVault> guardVaults;

  public GuardVaultActivatableService() {
    this.guardVaults = new HashSet<>();
  }

  @Override
  public void registerActivatable(GuardVault activatable) {
    this.guardVaults.add(activatable);
  }

  @Override
  public void unregisterActivatable(GuardVault activatable) {
    this.guardVaults.remove(activatable);
  }

  @Override
  public Collection<GuardVault> getAllActivatables() {
    return Set.copyOf(this.guardVaults);
  }

  @Override
  public void initialize() {
    ArrayList<String[]> guardVaultData = DataFormatter.csv();
    for (String[] line : guardVaultData) {
      GuardVault guardVault = new DefaultGuardVault(
          // Den Sector som VagtVaulten er placeret i
          PrisonSector.fromString(line[0]),
          // De sektorer som VagtVaulten kan ses fra
          readVisibleSectors(line[1]),
          Integer.parseInt(line[2]),
          Integer.parseInt(line[3]),
          Integer.parseInt(line[4]),
          Integer.parseInt(line[5])
      );
      this.registerActivatable(guardVault);
    }
  }

  @Override
  public void shutdown() {

  }

  private PrisonSector[] readVisibleSectors(String data) {
    String[] sectorStrings = data.split(";");
    PrisonSector[] sectors = new PrisonSector[sectorStrings.length];
    for (int i = 0; i < sectorStrings.length; i++) {
      sectors[i] = PrisonSector.fromString(sectorStrings[i]);
    }
    return sectors;
  }
}
