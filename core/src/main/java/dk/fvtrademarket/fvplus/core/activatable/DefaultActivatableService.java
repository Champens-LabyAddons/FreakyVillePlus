package dk.fvtrademarket.fvplus.core.activatable;

import dk.fvtrademarket.fvplus.api.activatable.Activatable;
import dk.fvtrademarket.fvplus.api.activatable.guardvault.GuardVault;
import dk.fvtrademarket.fvplus.api.activatable.misc.GangArea;
import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.service.activatable.ActivatableService;
import dk.fvtrademarket.fvplus.core.util.DataFormatter;
import dk.fvtrademarket.fvplus.core.util.Resources;
import net.labymod.api.models.Implements;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Singleton
@Implements(ActivatableService.class)
public class DefaultActivatableService implements ActivatableService {
  private final Set<Activatable> activatables;
  private boolean initialized;

  public DefaultActivatableService() {
    this.activatables = new HashSet<>();
    this.initialized = false;
  }

  @Override
  public void registerActivatable(Activatable activatable) {
    this.activatables.add(activatable);
  }

  @Override
  public void unregisterActivatable(Activatable activatable) {
    this.activatables.remove(activatable);
  }

  @Override
  public Collection<Activatable> getAllActivatables() {
    return new HashSet<>(this.activatables);
  }

  @Override
  public <A extends Activatable> Collection<A> getActivatables(Class<A> activatableClass) {
    Set<A> activatables = new HashSet<>();
    for (Activatable activatable : this.activatables) {
      if (activatableClass.isInstance(activatable)) {
        activatables.add(activatableClass.cast(activatable));
      }
    }
    return activatables;
  }

  @Override
  public void initialize() {
    if (initialized) {
      throw new IllegalStateException("Service already initialized");
    }
    ArrayList<String[]> guardVaultData = DataFormatter.csv(Resources.GUARD_VAULTS);
    ArrayList<String[]> gangAreaData = DataFormatter.csv(Resources.GANG_AREAS);

    guardVaultData.removeFirst();
    gangAreaData.removeFirst();

    for (String[] line : guardVaultData) {
      readGuardVault(line);
    }
    for (String[] line : gangAreaData) {
      readGangArea(line);
    }
    initialized = true;
  }

  @Override
  public void shutdown() {
    initialized = false;
  }

  private void readGangArea(String[] line) {
    GangArea gangArea = new DefaultGangArea(
        // Den Sector som Bandeområdet er placeret i
        PrisonSector.fromString(line[0]),
        Integer.parseInt(line[1]),
        Integer.parseInt(line[2]),
        Integer.parseInt(line[3]),
        Integer.parseInt(line[4])
    );
    this.registerActivatable(gangArea);
  }

  private void readGuardVault(String[] line) {
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

  private PrisonSector[] readVisibleSectors(String data) {
    String[] sectorStrings = data.split(";");
    PrisonSector[] sectors = new PrisonSector[sectorStrings.length];
    for (int i = 0; i < sectorStrings.length; i++) {
      sectors[i] = PrisonSector.fromString(sectorStrings[i]);
    }
    return sectors;
  }
}
