package dk.fvtrademarket.fvplus.api.service.housing;

import dk.fvtrademarket.fvplus.api.housing.LivingArea;
import dk.fvtrademarket.fvplus.api.service.Service;
import net.labymod.api.reference.annotation.Referenceable;
import java.util.Collection;

@Referenceable
public interface HousingService extends Service {

  void registerLivingQuarters(LivingArea housing);

  void unregisterLivingQuarters(LivingArea housing);

  Collection<LivingArea> getAllLivingQuarters();

  @Override
  void initialize();

  @Override
  void shutdown();

}
