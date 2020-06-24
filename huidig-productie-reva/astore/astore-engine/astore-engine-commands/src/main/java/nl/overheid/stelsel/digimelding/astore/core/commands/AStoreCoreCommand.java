package nl.overheid.stelsel.digimelding.astore.core.commands;

import java.util.HashMap;
import java.util.Map;

import nl.overheid.stelsel.digimelding.astore.AnnotationStoreService;
import nl.overheid.stelsel.digimelding.astore.timing.TimingService;

import org.apache.clerezza.rdf.core.serializedform.SupportedFormat;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceException;
import org.osgi.framework.ServiceReference;

public abstract class AStoreCoreCommand extends OsgiCommandSupport {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Map<String, String> supportedFormatShortCuts = new HashMap<String, String>();

  static {
    supportedFormatShortCuts.put("RDF", SupportedFormat.RDF_XML);
    supportedFormatShortCuts.put("TURTLE", SupportedFormat.TURTLE);
    supportedFormatShortCuts.put("XTURTLE", SupportedFormat.X_TURTLE);
    supportedFormatShortCuts.put("N3", SupportedFormat.N3);
    supportedFormatShortCuts.put("N-TRIPLE", SupportedFormat.N_TRIPLE);
    supportedFormatShortCuts.put("JSON", SupportedFormat.RDF_JSON);
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public AnnotationStoreService getAnnotationStoreService() {
    BundleContext context = getBundleContext();
    ServiceReference<AnnotationStoreService> serviceRef =
        context.getServiceReference(AnnotationStoreService.class);
    if (serviceRef == null) {
      throw new ServiceException("AnnotationStoreService service not available");
    }
    return context.getService(serviceRef);
  }

  public TimingService getTimingService() {
    BundleContext context = getBundleContext();
    ServiceReference<TimingService> serviceRef = context.getServiceReference(TimingService.class);
    if (serviceRef == null) {
      throw new ServiceException("Timing service not available");
    }
    return context.getService(serviceRef);
  }

  public static Map<String, String> getSupportedFormats() {
    return supportedFormatShortCuts;
  }

  // -------------------------------------------------------------------------
  // Protected methods
  // -------------------------------------------------------------------------

  protected void handleException(Exception exception) {
    if (exception.getMessage() == null || exception.getMessage().isEmpty()) {
      session.getConsole().println(exception.getClass().getSimpleName());
    } else {
      session.getConsole().println(exception.getMessage());
    }
  }
}
