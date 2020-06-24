package nl.overheid.stelsel.digimelding.astore.impl.managers;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.overheid.stelsel.digimelding.astore.AnnotationTree;
import nl.overheid.stelsel.digimelding.astore.filter.FilterProvider;
import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;
import nl.overheid.stelsel.digimelding.astore.timing.TimingService;

public class FilterManager extends AbstractManager<FilterProvider> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(FilterManager.class);

  // -------------------------------------------------------------------------
  // Members
  // -------------------------------------------------------------------------

  // -------------------------------------------------------------------------
  // OSGi Blueprint reference binding
  // -------------------------------------------------------------------------

  @Override
  public void bind(FilterProvider provider, Map<?, ?> properties) {
    super.bind(provider, properties);
  }

  @Override
  public void unbind(FilterProvider provider) {
    super.unbind(provider);
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  public void filter(Set<AnnotationTree> trees) {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("FilterManager:filter");
    try {

      for (FilterProvider provider : getProviders()) {
        try {
          TimingMeasurement childMeasurement =
              measurement.startChildMeasurement(provider.getClass().getSimpleName());
          provider.filter();
          childMeasurement.endMeasurement();
        } catch (Exception ex) {
          logger.error(ex.getMessage(), ex);
        }
      }
    } finally {
      measurement.endMeasurement();
    }
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  /**
   * Noodzakelijk omdat blueprint de setter uit de super class niet pakt.
   */
  @Override
  public void setTimingService(TimingService timingService) {
    super.setTimingService(timingService);
  }
}
