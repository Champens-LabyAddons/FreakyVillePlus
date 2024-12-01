package dk.fvtrademarket.fvplus.core.housing;

import dk.fvtrademarket.fvplus.api.housing.LivingArea;
import dk.fvtrademarket.fvplus.api.service.housing.HousingService;
import net.labymod.api.models.Implements;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Singleton
@Implements(HousingService.class)
public class DefaultHousingService implements HousingService {
  private final Set<LivingArea> livingQuarters;

  public DefaultHousingService() {
    this.livingQuarters = new HashSet<>();
  }

  @Override
  public void registerLivingQuarters(LivingArea housing) {
    this.livingQuarters.add(housing);
  }

  @Override
  public void unregisterLivingQuarters(LivingArea housing) {
    this.livingQuarters.remove(housing);
  }

  @Override
  public Collection<LivingArea> getAllLivingQuarters() {
    return this.livingQuarters;
  }

  @Override
  public void initialize() {

  }

  @Override
  public void shutdown() {

  }
}
