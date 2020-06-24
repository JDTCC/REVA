package nl.overheid.stelsel.digimelding.astore.core.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.impl.SimpleMGraph;
import org.apache.clerezza.rdf.core.serializedform.Parser;
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(
    scope = "as",
    name = "import",
    description = "Imports information from the store merging possibly existing data with the imported data.")
public class ImportAnnotationCommand extends AStoreCoreCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(ImportAnnotationCommand.class);

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option(name = "-f", aliases = {"--format", "--fmt"},
      description = "RDF format to be used (default rdf+xml).", required = false,
      multiValued = false)
  private String format;

  @Option(name = "-r", aliases = {"--replace"},
      description = "Replace (instead of merge with) existing data.", required = false,
      multiValued = false)
  private boolean replace;

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument(index = 0, name = "location",
      description = "The filesystem directory or file to import from.", required = true,
      multiValued = false)
  private String location;

  // -------------------------------------------------------------------------
  // AStoreCoreCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    TimingMeasurement measurement = getTimingService().getSession().startMeasurement("as:import");
    try {
      // Use default format if format not specified.
      if (format == null) {
        format = SupportedFormat.RDF_XML;
      }
      if (getSupportedFormats().containsKey(format.toUpperCase())) {
        format = getSupportedFormats().get(format.toUpperCase());
      }

      File importLocation = new File(location);
      if (importLocation.isDirectory()) {
        importAll();
      } else {
        // import single entry
        location = importLocation.getParent();
        importGraph(importLocation.getName());
      }
    } finally {
      measurement.endMeasurement();
      getTimingService().getSession().endSession();
    }
    return "";
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public Parser getParserService() {
    BundleContext context = getBundleContext();
    ServiceReference<Parser> serviceRef = context.getServiceReference(Parser.class);
    if (serviceRef == null) {
      throw new ServiceException("RDF Parser service not available");
    }
    return context.getService(serviceRef);
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private void importGraph(String registratieId) {
    try {
      session.getConsole().print("\rImporting " + registratieId);
      // Read the annotation tree from the given url.
      Annotation annotation = readAnnotation(registratieId);
      annotation.setStam(UUID.fromString(registratieId));

      if (replace) {
        getAnnotationStoreService().remove(annotation.getStam());
      }
      getAnnotationStoreService().put(annotation);
    } catch (Exception ex) {
      logger.warn("Import failed! (" + registratieId + ")", ex);
      session.getConsole().println("\rImport failed " + registratieId);
      return;
    }
  }

  private void importAll() throws Exception {
    File importsDir = new File(location);
    session.getConsole().println("Importing...");
    File[] importFiles = importsDir.listFiles();
    if ( importFiles != null ) {
      for (File singleImport : importFiles) {
        importGraph(singleImport.getName());
      }
    }
    session.getConsole().println("\rDone                                           ");
  }

  protected Annotation readAnnotation(String registratieId) throws Exception {
    Annotation annotation = new Annotation();

    try (InputStream fis = new FileInputStream(location + File.separator + registratieId)) {
      MGraph graph = new SimpleMGraph();
      getParserService().parse(graph, fis, format);
      annotation.setGraph(graph);
    }

    return annotation;
  }
}
