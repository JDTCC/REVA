package nl.overheid.stelsel.gba.reva.web.locators;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import nl.overheid.stelsel.gba.reva.bag.BagService;
import nl.overheid.stelsel.gba.reva.web.RevaApplication;

/**
 * Service locator to return the BAG service to be used.
 * 
 */
public final class BagServiceLocator {

  // -------------------------------------------------------------------------
  // Constructor
  // -------------------------------------------------------------------------

  /**
   * Hide utility class constructor
   */
  private BagServiceLocator() {
    // prevent instantiation
  }

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  /**
   * @return bag service
   */
  public static BagService getService() {
    BundleContext context = RevaApplication.get().getBundleContext();
    ServiceReference<BagService> serviceRef = context.getServiceReference( BagService.class );
    return context.getService( serviceRef );
  }
}