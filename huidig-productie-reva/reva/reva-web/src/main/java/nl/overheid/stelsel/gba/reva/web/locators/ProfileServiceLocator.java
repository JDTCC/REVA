package nl.overheid.stelsel.gba.reva.web.locators;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import nl.overheid.stelsel.gba.reva.profile.ProfileService;
import nl.overheid.stelsel.gba.reva.web.RevaApplication;

/**
 * Service locator to return the Profile service to be used.
 * 
 */
public final class ProfileServiceLocator {

  // -------------------------------------------------------------------------
  // Constructor
  // -------------------------------------------------------------------------

  /**
   * Hide utility class constructor
   */
  private ProfileServiceLocator() {
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
  public static ProfileService getService() {
    BundleContext context = RevaApplication.get().getBundleContext();
    ServiceReference<ProfileService> serviceRef = context.getServiceReference( ProfileService.class );
    return context.getService( serviceRef );
  }
}