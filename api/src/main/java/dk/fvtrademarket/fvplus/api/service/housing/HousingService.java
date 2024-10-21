package dk.fvtrademarket.fvplus.api.service.housing;

import dk.fvtrademarket.fvplus.api.housing.LivingArea;
import dk.fvtrademarket.fvplus.api.service.Service;
import java.util.Collection;

public interface HousingService<T extends LivingArea> extends Service {

  void registerLivingQuarters(T housing);

  void unregisterLivingQuarters(T housing);

  Collection<T> getAllLivingQuarters();

  @Override
  void initialize();

  @Override
  void shutdown();

}
