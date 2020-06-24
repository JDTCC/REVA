package nl.overheid.stelsel.digimelding.astore.impl.managers;

import java.util.Map;
import java.util.UUID;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationContext;
import nl.overheid.stelsel.digimelding.astore.processor.AnnotationProcessor;
import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;
import nl.overheid.stelsel.digimelding.astore.timing.TimingService;

import org.osgi.framework.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessorManager extends AbstractManager<AnnotationProcessor> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(ProcessorManager.class);

  // -------------------------------------------------------------------------
  // Members
  // -------------------------------------------------------------------------

  // -------------------------------------------------------------------------
  // OSGi Blueprint reference binding
  // -------------------------------------------------------------------------

  @Override
  public void bind(AnnotationProcessor provider, Map<?, ?> properties) {
    super.bind(provider, properties);
  }

  @Override
  public void unbind(AnnotationProcessor provider) {
    super.unbind(provider);
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  public void process(AnnotationContext context, Annotation annotation) {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("ProcessorManager:process");
    try {

      for (AnnotationProcessor processor : getProviders()) {
        try {
          TimingMeasurement childMeasurement =
              measurement.startChildMeasurement(processor.getClass().getSimpleName());
          processor.process(context, annotation);
          childMeasurement.endMeasurement();
        } catch (ServiceException sex) {
          logger.warn("Preprocessor error, annotation not stored due to: " + sex.getMessage());
          throw sex;
        } catch (Exception ex) {
          logger.error(ex.getMessage(), ex);
          throw new ServiceException("Preprocessor error, annotation not stored!", ex);
        }
      }
    } finally {
      measurement.endMeasurement();
    }
  }

  public void processRemoval(AnnotationContext context, UUID uuid) {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("ProcessorManager:process");
    try {

      for (AnnotationProcessor processor : getProviders()) {
        try {
          TimingMeasurement childMeasurement =
              measurement.startChildMeasurement(processor.getClass().getSimpleName());
          processor.processRemoval(context, uuid);
          childMeasurement.endMeasurement();
        } catch (ServiceException sex) {
          logger.warn("Preprocessor error, remove aborted due to: " + sex.getMessage());
          throw sex;
        } catch (Exception ex) {
          logger.error(ex.getMessage(), ex);
          throw new ServiceException("Preprocessor error, remove aborted!", ex);
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
