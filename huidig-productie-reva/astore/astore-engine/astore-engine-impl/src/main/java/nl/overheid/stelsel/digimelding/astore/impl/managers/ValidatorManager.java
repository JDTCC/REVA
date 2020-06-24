package nl.overheid.stelsel.digimelding.astore.impl.managers;

import java.util.Map;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationContext;
import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;
import nl.overheid.stelsel.digimelding.astore.timing.TimingService;
import nl.overheid.stelsel.digimelding.astore.validation.ValidationProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidatorManager extends AbstractManager<ValidationProvider> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(ValidatorManager.class);

  // -------------------------------------------------------------------------
  // Members
  // -------------------------------------------------------------------------

  // -------------------------------------------------------------------------
  // OSGi Blueprint reference binding
  // -------------------------------------------------------------------------

  @Override
  public void bind(ValidationProvider provider, Map<?, ?> properties) {
    super.bind(provider, properties);
  }

  @Override
  public void unbind(ValidationProvider provider) {
    super.unbind(provider);
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  public void validate(AnnotationContext context, Annotation annotation) {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("ValidatorManager:validate");
    try {

      // TODO: what strategy applies here?
      // 1) Quit on first validation error
      // 2) Collect from all validators before returning failure.

      for (ValidationProvider validator : getProviders()) {
        try {
          TimingMeasurement childMeasurement =
              measurement.startChildMeasurement(validator.getClass().getSimpleName());
          validator.validate(context, annotation);
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
