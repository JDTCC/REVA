package nl.overheid.stelsel.digimelding.astore.preprocess.saferemove;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.serializedform.Serializer;
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationContext;
import nl.overheid.stelsel.digimelding.astore.processor.AnnotationProcessor;

/**
 * This notification provider uses twitter to send out notification.
 * 
 */
public class SafeRemoveProcessorProvider implements AnnotationProcessor {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(SafeRemoveProcessorProvider.class);

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
  // Object attributes
  // -------------------------------------------------------------------------

  private BundleContext bundleContext;
  private String backupDir;
  private String backupFormat;

  // -------------------------------------------------------------------------
  // OSGi Blueprint lifecycle
  // -------------------------------------------------------------------------

  public void init() {
    logger.trace("SafeRemoveProcessorProvider:init");
  }

  public void destroy() {
    logger.trace("SafeRemoveProcessorProvider:destroy");
  }

  // -------------------------------------------------------------------------
  // Implementing ProcessorProvider
  // -------------------------------------------------------------------------

  @Override
  public void process(AnnotationContext context, Annotation annotation) {
    // No operation, we only process removals.
  };

  @Override
  public void processRemoval(AnnotationContext context, UUID uuid) {
    String graphId = uuid.toString();

    String location = getExportDir();
    logger.info(String.format("Exporting '%s' to '%s'", graphId, location));
    try ( FileOutputStream fos = new FileOutputStream(location + File.separator + graphId) ) {
      MGraph graph = context.getTree();
      if (graph == null) {
        logger.warn("No graph found with id = '{}'", graphId);
        return;
      }

      getSerializerService().serialize(fos, graph, backupFormat);
    } catch (final Exception e) {
        logger.warn("Writing remove backup for '{}' failed due to: {}", graphId, e.getMessage());
        throw new SafeRemoveException(e);
    }
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public BundleContext getBundleContext() {
    return bundleContext;
  }

  public void setBundleContext(BundleContext bundleContext) {
    this.bundleContext = bundleContext;
  }

  public String getBackupDir() {
    return backupDir;
  }

  public void setBackupDir(String backupDir) {
    this.backupDir = backupDir;
  }

  public String getBackupFormat() {
    return backupFormat;
  }

  public void setBackupFormat(final String backupFormat) {
    this.backupFormat = backupFormat;
    if (this.backupFormat == null) {
      this.backupFormat = SupportedFormat.RDF_XML;
    }
    if (supportedFormatShortCuts.containsKey(this.backupFormat.toUpperCase())) {
      this.backupFormat = supportedFormatShortCuts.get(this.backupFormat.toUpperCase());
    }
  }

  public Serializer getSerializerService() {
    BundleContext context = getBundleContext();
    ServiceReference<Serializer> serviceRef = context.getServiceReference(Serializer.class);
    if (serviceRef == null) {
      throw new ServiceException("RDF Serializer service not available");
    }
    return context.getService(serviceRef);
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private String getExportDir() {
    String location = String.format("%s%3$s%2$tY%3$s%2$tm%3$s%2$td", getBackupDir(), Calendar.getInstance(), File.separator);
    File exportdir = new File(location);
    if ( exportdir.mkdirs() ) {
      logger.warn(String.format("Failed to create SafeRemove location '%s'", location));
    } 
    return location;
  }
}
