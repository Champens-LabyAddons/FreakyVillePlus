package dk.fvtrademarket.fvplus.core.activatable;

import dk.fvtrademarket.fvplus.api.activatable.Activatable;
import dk.fvtrademarket.fvplus.api.activatable.guardvault.GuardVault;
import dk.fvtrademarket.fvplus.api.activatable.misc.GangArea;
import dk.fvtrademarket.fvplus.api.enums.PrisonSector;
import dk.fvtrademarket.fvplus.api.service.activatable.ActivatableService;
import dk.fvtrademarket.fvplus.core.util.DataFormatter;
import dk.fvtrademarket.fvplus.core.util.Resource;
import dk.fvtrademarket.fvplus.core.widgets.Widgets;
import net.labymod.api.models.Implements;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Singleton
@Implements(ActivatableService.class)
public class DefaultActivatableService implements ActivatableService {
  private final Set<Activatable> activatables;
  private final Map<Activatable, Long> cooldowns;
  private final Map<Activatable, Long> personalCooldowns;
  private final Set<Activatable> limboingActivatables;

  private final Widgets widgets;

  private boolean initialized;

  public DefaultActivatableService() {
    this.activatables = new HashSet<>();
    this.cooldowns = new HashMap<>();
    this.personalCooldowns = new HashMap<>();
    this.limboingActivatables = new HashSet<>();

    this.widgets = new Widgets(this);

    this.initialized = false;
  }

  @Override
  public void registerActivatable(Activatable activatable) {
    if (!this.activatables.contains(activatable)) {
      this.widgets.register(activatable);
    }
    this.activatables.add(activatable);
  }

  @Override
  public void unregisterActivatable(Activatable activatable) {
    this.activatables.remove(activatable);
  }

  public void putActivatableOnCooldown(Activatable activatable, long endTime, boolean personal) {
    if (personal) {
      this.personalCooldowns.put(activatable, endTime);
    } else {
      this.cooldowns.put(activatable, endTime);
    }
    this.widgets.setTimer(activatable, endTime);
  }

  public void putActivatableOnCooldown(Activatable activatable, boolean personal) {
    long currentTime = System.currentTimeMillis();
    long endTime = currentTime + (activatable.getSharedCooldown() * 1000L);
    if (personal) {
      endTime = currentTime + (activatable.getPersonalCooldown() * 1000L);
    }
    putActivatableOnCooldown(activatable, endTime, personal);
    this.widgets.setTimer(activatable, endTime);
  }

  public void putActivatableOnFailureCooldown(Activatable activatable) {
    long currentTime = System.currentTimeMillis();
    long endTime = currentTime + (activatable.getCooldownUponFailure() * 1000L);
    putActivatableOnCooldown(activatable, endTime, false);
    this.widgets.setTimer(activatable, endTime);
  }

  public void removeActivatableFromCooldown(Activatable activatable) {
    this.cooldowns.remove(activatable);
    this.personalCooldowns.remove(activatable);
  }

  public void putActivatableInLimbo(Activatable activatable) {
    this.limboingActivatables.add(activatable);
  }

  public void removeActivatableFromLimbo(Activatable activatable) {
    this.limboingActivatables.remove(activatable);
  }

  @Override
  public Collection<Activatable> getAllActivatables() {
    return Set.copyOf(this.activatables);
  }

  @Override
  public Map<Activatable, Long> getCooldowns() {
    return Map.copyOf(this.cooldowns);
  }

  @Override
  public Collection<Activatable> getLimboingActivatables() {
    return Set.copyOf(this.limboingActivatables);
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
  public long getCooldownTime(Activatable activatable) {
    long endTime = this.cooldowns.getOrDefault(activatable, 0L);
    if (this.personalCooldowns.containsKey(activatable)) {
      endTime = this.personalCooldowns.get(activatable);
    }
    return Math.max(0, endTime - System.currentTimeMillis());
  }

  @Override
  public boolean hasActivatable(Activatable activatable) {
    return this.activatables.contains(activatable);
  }

  @Override
  public boolean isOnCooldown(Activatable activatable) {
    return getCooldownTime(activatable) > 0;
  }

  @Override
  public boolean isOnPersonalCooldown(Activatable activatable) {
    return this.personalCooldowns.containsKey(activatable) && getCooldownTime(activatable) > 0;
  }

  @Override
  public void initialize() {
    if (initialized) {
      throw new IllegalStateException("Service already initialized");
    }
    ArrayList<String[]> guardVaultData = DataFormatter.csv(Resource.GUARD_VAULTS.toString(), true);
    ArrayList<String[]> gangAreaData = DataFormatter.csv(Resource.GANG_AREAS.toString(), true);

    if (!guardVaultData.isEmpty()) {
      guardVaultData.removeFirst();
      for (String[] line : guardVaultData) {
        this.activatables.add(readGuardVault(line));
      }
    }
    if (!gangAreaData.isEmpty()) {
      gangAreaData.removeFirst();
      for (String[] line : gangAreaData) {
        this.activatables.add(readGangArea(line));
      }
    }
    widgets.initialize();
    initialized = true;
  }

  @Override
  public void shutdown() {
    initialized = false;
  }

  private GangArea readGangArea(String[] line) {
    return new DefaultGangArea(
        // Den Sector som Bandeområdet er placeret i
        PrisonSector.fromString(line[0]),
        Integer.parseInt(line[1]),
        Integer.parseInt(line[2]),
        Integer.parseInt(line[3]),
        Integer.parseInt(line[4])
    );
  }

  private GuardVault readGuardVault(String[] line) {
    return new DefaultGuardVault(
        // Den Sector som VagtVaulten er placeret i
        PrisonSector.fromString(line[0]),
        // De sektorer som VagtVaulten kan ses fra
        readVisibleSectors(line[1]),
        Integer.parseInt(line[2]),
        Integer.parseInt(line[3]),
        Integer.parseInt(line[4]),
        Integer.parseInt(line[5])
    );
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
