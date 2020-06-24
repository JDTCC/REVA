package nl.overheid.stelsel.digimelding.astore.core.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import nl.overheid.stelsel.digimelding.astore.AnnotationTree;
import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;
import nl.overheid.stelsel.digimelding.astore.utils.ResourceUtils;

import org.apache.clerezza.rdf.core.serializedform.Serializer;
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat;
import org.apache.clerezza.rdf.core.sparql.ResultSet;
import org.apache.clerezza.rdf.core.sparql.SolutionMapping;
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
    name = "export",
    description = "Exports information from the store. Existing files will be overwritten without warning.")
public class ExportAnnotationCommand extends AStoreCoreCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(ExportAnnotationCommand.class);

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option(name = "-f", aliases = {"--format", "--fmt"},
      description = "RDF format to be used (default rdf+xml).", required = false,
      multiValued = false)
  private String format;

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument(index = 0, name = "id", description = "The id to be exported (use * for all).",
      required = true, multiValued = false)
  private String registratieId;

  @Argument(index = 1, name = "destination",
      description = "The filesystem destination where to place exported graphs.", required = true,
      multiValued = false)
  private String destination;

  // -------------------------------------------------------------------------
  // AStoreCoreCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    TimingMeasurement measurement = getTimingService().getSession().startMeasurement("as:export");
    try {
      // Use default format if format not specified.
      if (format == null) {
        format = SupportedFormat.RDF_XML;
      }
      if (getSupportedFormats().containsKey(format.toUpperCase())) {
        format = getSupportedFormats().get(format.toUpperCase());
      }

      prepareOutputLocation(destination);
      if ("*".equals(registratieId)) {
        exportAll();
      } else {
        // export single entry
        export(registratieId);
        session.getConsole().println();
      }
      return "";
    } finally {
      measurement.endMeasurement();
      getTimingService().getSession().endSession();
    }
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

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

  private void export(String registratieId) {
    UUID id = UUID.fromString(registratieId);

    AnnotationTree annotationTree = getAnnotationTree(id);
    if (annotationTree != null) {
      try (FileOutputStream fos = new FileOutputStream(destination + File.separator + registratieId)) {
        session.getConsole().print("\rExporting " + registratieId);
        getSerializerService().serialize(fos, annotationTree.getGraph(), format);
      } catch (Exception ex) {
        logger.warn("Export failed!", ex);
        session.getConsole().println("\rExport failed " + registratieId);
      }
    }
  }

  private AnnotationTree getAnnotationTree(UUID id) {
    try {
      AnnotationTree tree = getAnnotationStoreService().get(id);
      session.getConsole().println("No export for unknown " + registratieId);
      return tree;
    } catch (Exception ex) {
      logger.warn("Export failed!", ex);
      session.getConsole().println("\rExport failed " + registratieId);
      return null;
    }
  }
  
  private void exportAll() throws Exception {
    Object queryResult = getAnnotationStoreService().namedQuery("export", null);
    if (queryResult instanceof ResultSet) {
      session.getConsole().println("Exporting...");
      ResultSet results = ResultSet.class.cast(queryResult);

      // Bepaal de naam van de eerste kolom.
      String kolomNaam = results.getResultVars().get(0);

      // Exporteer elke rij
      while (results.hasNext()) {
        SolutionMapping row = results.next();
        String id = ResourceUtils.getResourceValue(row.get(kolomNaam));
        export(id);
      }
      session.getConsole().println("\rDone                                           ");
    }
  }

  private void prepareOutputLocation(String outputLocation) {
    File outputDir = new File(outputLocation);
    outputDir.mkdirs();
    if (!outputDir.isDirectory()) {
      session.getConsole().println("Destination needs to be a directory...");
      throw new IllegalArgumentException("Destination is not a directory");
    }
  }
}
