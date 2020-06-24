package nl.overheid.stelsel.gba.reva.web.locators;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import nl.overheid.stelsel.digimelding.astore.timing.TimingService;
import nl.overheid.stelsel.gba.reva.web.RevaApplication;

/**
 * Service locator to return the timing service to be used.
 * 
 */
public final class TimingServiceLocator {

  // -------------------------------------------------------------------------
  // Constructor
  // -------------------------------------------------------------------------

  /**
   * Hide utility class constructor
   */
  private TimingServiceLocator() {
    // prevent instantiation
  }

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  /**
   * Retrieve the Profile Service.
   * 
   * @return profile service
   */
  public static TimingService getService() {
    BundleContext context = RevaApplication.get().getBundleContext();
    ServiceReference<TimingService> serviceRef = context.getServiceReference( TimingService.class );
    return context.getService( serviceRef );
  }
}