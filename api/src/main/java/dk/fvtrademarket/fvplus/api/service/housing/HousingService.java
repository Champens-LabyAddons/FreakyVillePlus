package dk.fvtrademarket.fvplus.api.service.housing;

import dk.fvtrademarket.fvplus.api.enums.FreakyVilleServer;
import dk.fvtrademarket.fvplus.api.housing.LivingArea;
import dk.fvtrademarket.fvplus.api.service.Service;
import net.labymod.api.reference.annotation.Referenceable;
import java.util.Collection;
import java.util.Optional;

@Referenceable
public interface HousingService extends Service {

  void registerLivingArea(LivingArea housing);

  void unregisterLivingArea(LivingArea housing);

  Optional<LivingArea> getLivingArea(FreakyVilleServer freakyVilleServer, String areaIdentifier);

  Collection<LivingArea> getAllLivingAreas();

  @Override
  void initialize();

  @Override
  void shutdown();

}
