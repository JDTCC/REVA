package nl.overheid.stelsel.gba.reva.web.configuration;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import nl.overheid.stelsel.gba.reva.web.RevaApplication;

/**
 * Application version related configuration.
 * 
 */
public final class VersionConfiguration implements ManagedService {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final String CONFIG_PID = "version";
  private static final String CONFIG_VERSION = "reva.version";
  private static final String CONFIG_CONTEXT = "reva.context";

  public static final String CONTEXT_VALUE_PROD = "productie";
  public static final String CONTEXT_VALUE_DEV = "ontwikkel";

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static VersionConfiguration instance = null;

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private BundleContext bundleContext = null;
  private ServiceRegistration<?> serviceReg = null;
  private Dictionary<String, ?> config = new Hashtable<>();

  // -------------------------------------------------------------------------
  // Constructor
  // -------------------------------------------------------------------------

  /**
   * Hide utility class constructor
   */
  private VersionConfiguration( BundleContext context ) {
    clear();
    bundleContext = context;

    Hashtable<String, Object> properties = new Hashtable<String, Object>();
    properties.put( Constants.SERVICE_PID, CONFIG_PID );
    serviceReg = context.registerService( ManagedService.class.getName(), this, properties );
  }

  // -------------------------------------------------------------------------
  // Implementing ManagedService
  // -------------------------------------------------------------------------

  public void updated( Dictionary<String, ?> properties ) throws ConfigurationException {
    if( properties == null ) {
      return;
    }
    config = properties;
  };

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  /**
   * Tells in which context the application is currently running.
   * 
   * @return String with the application context.
   */
  public static String applicationContext() {
    Object value = getInstance().config.get( CONFIG_CONTEXT );
    if( value != null ) {
      return (String) value;
    }

    return "Onbekend";
  }

  /**
   * Tells in what application version is running.
   * 
   * @return String with the application version.
   */
  public static String applicationVersion() {
    Object value = getInstance().config.get( CONFIG_VERSION );
    if( value != null ) {
      return (String) value;
    }

    return "Onbekend";
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private static VersionConfiguration getInstance() {
    BundleContext context = RevaApplication.get().getBundleContext();
    if( instance == null || !context.equals( instance.bundleContext ) ) {
      instance = new VersionConfiguration( context );
    }

    return instance;
  }

  private void clear() {
    if( serviceReg != null ) {
      serviceReg.unregister();
      serviceReg = null;
    }
  }
}