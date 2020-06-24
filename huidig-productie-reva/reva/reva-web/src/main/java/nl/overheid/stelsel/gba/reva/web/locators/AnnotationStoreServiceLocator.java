package nl.overheid.stelsel.gba.reva.web.locators;

import nl.overheid.stelsel.digimelding.astore.AnnotationStoreService;
import nl.overheid.stelsel.gba.reva.web.RevaApplication;

/**
 * Service locator to return the annotation store service to be used.
 * 
 */
public final class AnnotationStoreServiceLocator {

  // -------------------------------------------------------------------------
  // Constructor
  // -------------------------------------------------------------------------

  /**
   * Hide utility class constructor
   */
  private AnnotationStoreServiceLocator() {
    // prevent instantiation
  }

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  /**
   * @return annotation store service
   */
  public static AnnotationStoreService getService() {
    RevaApplication app = RevaApplication.get();
    return app.getAnnotationStoreService();
  }
}