package dk.fvtrademarket.fvplus.api.service.activatable;

import dk.fvtrademarket.fvplus.api.activatable.Activatable;
import dk.fvtrademarket.fvplus.api.service.Service;
import java.util.Collection;

public interface ActivatableService<T extends Activatable> extends Service {

  void registerActivatable(T activatable);

  void unregisterActivatable(T activatable);

  Collection<T> getAllActivatables();

  @Override
  void initialize();

  @Override
  void shutdown();
}
