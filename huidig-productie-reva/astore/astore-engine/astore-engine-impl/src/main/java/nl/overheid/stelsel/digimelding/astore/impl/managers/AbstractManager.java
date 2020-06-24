package nl.overheid.stelsel.digimelding.astore.impl.managers;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.overheid.stelsel.digimelding.astore.timing.TimingService;

import org.osgi.framework.Constants;
import org.osgi.framework.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractManager<T> {

  // -------------------------------------------------------------------------
  // Nested classes
  // -------------------------------------------------------------------------

  private final class ServiceRankingComparator implements Comparator<T> {
    @Override
    public int compare(T provider1, T provider2) {
      int compareResult = 0;
      if ( !provider1.equals(provider2) ) {
        Integer ranking1 = getRanking(provider1);
        Integer ranking2 = getRanking(provider2);

        if (ranking1.equals(ranking2)) {
          compareResult = (provider1.hashCode() > provider2.hashCode() ? 1 : -1);
        } else {
          if (ranking1 > ranking2) {
            compareResult = 1;
          } else {
            compareResult = -1;
          }
        }
      }
      return compareResult;
    }

    private Integer getRanking(T provider) {
      Integer ranking = providerRankingMap.get(provider);
      if (ranking == null) {
        return 0;
      }
      return ranking;
    }
  }

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final Logger logger = LoggerFactory.getLogger(AbstractManager.class);

  // -------------------------------------------------------------------------
  // Members
  // -------------------------------------------------------------------------

  private TimingService timingService = null;
  private final SortedSet<T> providers = new TreeSet<T>(new ServiceRankingComparator());
  private final Map<T, Integer> providerRankingMap = new HashMap<T, Integer>();

  // -------------------------------------------------------------------------
  // OSGi Blueprint lifecycle
  // -------------------------------------------------------------------------

  public void init() {
    logger.trace(this.getClass().getSimpleName() + ":init()");

    // Make sure to start with a clean slate
    providers.clear();
    providerRankingMap.clear();
  }

  public void destroy() {
    logger.trace(this.getClass().getSimpleName() + ":destroy()");
  }

  // -------------------------------------------------------------------------
  // OSGi Blueprint reference binding
  // -------------------------------------------------------------------------

  // Unfortunately the bind and unbind on this superclass result in
  // "...IllegalAccessException: Error calling listener method..."
  // To prevent this the bind and unbind need to be overloaded with a
  // super call to these methods.

  protected synchronized void bind(T provider, Map<?, ?> properties) {
    logger.trace(this.getClass().getSimpleName() + ":bind()");
    // Get the sevice ranking for the provider.
    Integer ranking = null;
    Object rankingValue = properties.get(Constants.SERVICE_RANKING);
    if (rankingValue instanceof Integer) {
      ranking = (Integer) properties.get(Constants.SERVICE_RANKING);
    }

    // Store this ranking, since it will be used when adding the provider
    // to the set of providers.
    providerRankingMap.put(provider, ranking);
    providers.add(provider);
  }

  protected synchronized void unbind(T provider) {
    logger.trace(this.getClass().getSimpleName() + ":unbind()");
    if (provider != null) {
      providers.remove(provider);

      // Since the provider is gone it's ranking in no longer needed.
      providerRankingMap.remove(provider);
    }
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public Set<T> getProviders() {
    return providers;
  }

  public TimingService getTimingService() {
    if (timingService == null) {
      throw new ServiceException("Timing service not available");
    }
    return timingService;
  }

  public void setTimingService(TimingService timingService) {
    this.timingService = timingService;
  }
}
