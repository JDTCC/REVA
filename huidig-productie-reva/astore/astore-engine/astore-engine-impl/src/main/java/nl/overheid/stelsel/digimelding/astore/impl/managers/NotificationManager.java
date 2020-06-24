package nl.overheid.stelsel.digimelding.astore.impl.managers;

import java.util.Map;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationContext;
import nl.overheid.stelsel.digimelding.astore.notification.NotificationProvider;
import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;
import nl.overheid.stelsel.digimelding.astore.timing.TimingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationManager extends AbstractManager<NotificationProvider> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(NotificationManager.class);

  // -------------------------------------------------------------------------
  // Members
  // -------------------------------------------------------------------------

  // -------------------------------------------------------------------------
  // OSGi Blueprint reference binding
  // -------------------------------------------------------------------------

  @Override
  public void bind(NotificationProvider provider, Map<?, ?> properties) {
    super.bind(provider, properties);
  }

  @Override
  public void unbind(NotificationProvider provider) {
    super.unbind(provider);
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  public void notify(AnnotationContext context, Annotation annotation) {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("NotificationManager:notify");
    try {

      for (NotificationProvider notifier : getProviders()) {
        try {
          TimingMeasurement childMeasurement =
              measurement.startChildMeasurement(notifier.getClass().getSimpleName());
          notifier.notify(context, annotation);
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
