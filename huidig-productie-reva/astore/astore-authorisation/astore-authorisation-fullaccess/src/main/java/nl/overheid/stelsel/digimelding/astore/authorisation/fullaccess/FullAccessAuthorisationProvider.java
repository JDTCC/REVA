package nl.overheid.stelsel.digimelding.astore.authorisation.fullaccess;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationContext;
import nl.overheid.stelsel.digimelding.astore.authorisation.AuthorisationProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FullAccessAuthorisationProvider implements AuthorisationProvider {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(FullAccessAuthorisationProvider.class);

  // -------------------------------------------------------------------------
  // OSGi Blueprint lifecycle
  // -------------------------------------------------------------------------

  public void init() {
    logger.trace("FullAccessAuthorisationProvider:init");
  }

  public void destroy() {
    logger.trace("FullAccessAuthorisationProvider:destroy");
  }

  // -------------------------------------------------------------------------
  // Implementing AuthorisationProvider
  // -------------------------------------------------------------------------

  @Override
  public boolean isStoreAuthorised(AnnotationContext ctx, Annotation annotation) {
    return true;
  }

  @Override
  public boolean isRetrieveAuthorised(AnnotationContext ctx, String query) {
    return true;
  }

  @Override
  public boolean isClearAuthorised(AnnotationContext ctx) {
    return true;
  }
}
