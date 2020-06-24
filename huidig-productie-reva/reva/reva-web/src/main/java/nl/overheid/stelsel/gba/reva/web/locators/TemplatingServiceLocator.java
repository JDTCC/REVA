package nl.overheid.stelsel.gba.reva.web.locators;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import nl.overheid.stelsel.gba.reva.web.RevaApplication;
import nl.xup.template.TemplateEngineService;

/**
 * Service locator to return the templating service to be used.
 * 
 */
public final class TemplatingServiceLocator {

  // -------------------------------------------------------------------------
  // Constructor
  // -------------------------------------------------------------------------

  /**
   * Hide utility class constructor
   */
  private TemplatingServiceLocator() {
    // prevent instantiation
  }

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  /**
   * @return templating service
   */
  public static TemplateEngineService getService() {
    BundleContext context = RevaApplication.get().getBundleContext();
    ServiceReference<TemplateEngineService> serviceRef = context.getServiceReference( TemplateEngineService.class );
    return context.getService( serviceRef );
  }
}