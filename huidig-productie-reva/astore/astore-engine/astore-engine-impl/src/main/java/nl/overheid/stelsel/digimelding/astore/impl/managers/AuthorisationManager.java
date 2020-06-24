package nl.overheid.stelsel.digimelding.astore.impl.managers;

import java.util.Map;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationContext;
import nl.overheid.stelsel.digimelding.astore.authorisation.AuthorisationProvider;
import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;
import nl.overheid.stelsel.digimelding.astore.timing.TimingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorisationManager extends AbstractManager<AuthorisationProvider> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(AuthorisationManager.class);

  // -------------------------------------------------------------------------
  // Members
  // -------------------------------------------------------------------------

  // -------------------------------------------------------------------------
  // OSGi Blueprint reference binding
  // -------------------------------------------------------------------------

  @Override
  public void bind(AuthorisationProvider provider, Map<?, ?> properties) {
    super.bind(provider, properties);
  }

  @Override
  public void unbind(AuthorisationProvider provider) {
    super.unbind(provider);
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  /**
   * Checks whether the issuer is authorized to store the given annotation.
   * 
   * @param ctx the context.
   * @param annotation the annotation to be stored.
   * @return {@code True} if authorized, {@code False} otherwise
   */
  public boolean isStoreAuthorised(AnnotationContext ctx, Annotation annotation) {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("AuthorisationManager:isStoreAuthorised");
    try {

      // Pretend unauthorised until authorised at least once.
      boolean unAuthorised = true;
      for (AuthorisationProvider authoriser : getProviders()) {
        try {
          TimingMeasurement childMeasurement =
              measurement.startChildMeasurement(authoriser.getClass().getSimpleName());
          unAuthorised &= !authoriser.isStoreAuthorised(ctx, annotation);
          childMeasurement.endMeasurement();
        } catch (Exception ex) {
          logger.error(ex.getMessage(), ex);
        }
      }
      return !unAuthorised;
    } finally {
      measurement.endMeasurement();
    }
  }

  /**
   * Checks whether the issuer is authorized to invoke this given (named) query.
   * 
   * @param ctx the context.
   * @param query the query or named query to use
   * @return {@code True} if authorized, {@code False} otherwise
   */
  public boolean isRetrieveAuthorised(AnnotationContext ctx, String query) {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement(
            "AuthorisationManager:isRetrieveAuthorised");
    try {

      // Pretend unauthorised until authorised at least once.
      boolean unAuthorised = true;
      for (AuthorisationProvider authoriser : getProviders()) {
        try {
          TimingMeasurement childMeasurement =
              measurement.startChildMeasurement(authoriser.getClass().getSimpleName());
          unAuthorised &= !authoriser.isRetrieveAuthorised(ctx, query);
          childMeasurement.endMeasurement();
        } catch (Exception ex) {
          logger.error(ex.getMessage(), ex);
        }
      }
      return !unAuthorised;
    } finally {
      measurement.endMeasurement();
    }
  }

  /**
   * Checks whether the issuer is authorized to wipe the annotation repository.
   * 
   * @param ctx the context.
   * @return {@code True} if authorized, {@code False} otherwise
   */
  public boolean isClearAuthorised(AnnotationContext ctx) {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("AuthorisationManager:isClearAuthorised");
    try {

      // Pretend unauthorised until authorised at least once.
      boolean unAuthorised = true;
      for (AuthorisationProvider authoriser : getProviders()) {
        try {
          TimingMeasurement childMeasurement =
              measurement.startChildMeasurement(authoriser.getClass().getSimpleName());
          unAuthorised &= !authoriser.isClearAuthorised(ctx);
          childMeasurement.endMeasurement();
        } catch (Exception ex) {
          logger.error(ex.getMessage(), ex);
        }
      }
      return !unAuthorised;
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
