package dk.fvtrademarket.fvplus.api.service.activatable;

import dk.fvtrademarket.fvplus.api.activatable.Activatable;
import dk.fvtrademarket.fvplus.api.service.Service;
import java.util.Collection;

public interface ActivatableService extends Service {

  void registerActivatable(Activatable activatable);

  void unregisterActivatable(Activatable activatable);

  Collection<Activatable> getAllActivatables();

  @Override
  void initialize();

  @Override
  void shutdown();
}
