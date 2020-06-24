package nl.overheid.stelsel.gba.reva.web.locators;

import org.apache.clerezza.rdf.core.serializedform.Parser;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceException;
import org.osgi.framework.ServiceReference;

import nl.overheid.stelsel.gba.reva.web.RevaApplication;

/**
 * Service locator to return the rdf parser service to be used.
 * 
 */
public final class RdfParserServiceLocator {

  // -------------------------------------------------------------------------
  // Constructor
  // -------------------------------------------------------------------------

  /**
   * Hide utility class constructor
   */
  private RdfParserServiceLocator() {
    // prevent instantiation
  }

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  /**
   * @return rdf parser service
   */
  public static Parser getService() {
    BundleContext context = RevaApplication.get().getBundleContext();
    ServiceReference<Parser> serviceRef = context.getServiceReference( Parser.class );
    if( serviceRef == null ) {
      throw new ServiceException( "Parser service not available" );
    }
    return context.getService( serviceRef );
  }
}