package dk.fvtrademarket.fvplus.api.service.activatable;

import dk.fvtrademarket.fvplus.api.activatable.Activatable;
import dk.fvtrademarket.fvplus.api.service.Service;
import net.labymod.api.reference.annotation.Referenceable;
import java.util.Collection;

/**
 * Et service til at håndtere aktiverbare objekter
 *
 * @author Champen_V1ldtand
 * @since 2.0.0
 */
@Referenceable
public interface ActivatableService extends Service {

  void registerActivatable(Activatable activatable);

  void unregisterActivatable(Activatable activatable);

  Collection<Activatable> getAllActivatables();

  /**
   * Hent alle aktiverbare objekter af en bestemt type
   *
   * @param activatableClass klassen for de aktiverbare objekter
   * @return alle aktiverbare objekter af den givne type
   * @param <A> typen af aktiverbare objekter
   */
  <A extends Activatable> Collection<A> getActivatables(Class<A> activatableClass);

  @Override
  void initialize();

  @Override
  void shutdown();
}
