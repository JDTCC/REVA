package nl.overheid.stelsel.gba.reva.bag.commands;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceException;
import org.osgi.framework.ServiceReference;

import nl.overheid.stelsel.gba.reva.bag.BagService;

/**
 * Service locator voor het dynamisch ophalen van de actieve bag service.
 * 
 */
public final class BagServiceLocator {

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  private BagServiceLocator() {
    // prevent instantiation.
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  public static BagService getBagService( BundleContext context ) {
    ServiceReference<BagService> serviceRef = context.getServiceReference( BagService.class );
    if( serviceRef == null ) {
      throw new ServiceException( "BagService service not available" );
    }
    return context.getService( serviceRef );
  }
}
