package nl.overheid.stelsel.digimelding.astore.core.commands;

import java.io.InputStream;
import java.net.URL;
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

@Command(scope = "as", name = "put", description = "Puts annotation(s) into the store.")
public class PutAnnotationCommand extends AStoreCoreCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(PutAnnotationCommand.class);

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option(name = "-f", aliases = {"--format", "--fmt"},
      description = "RDF format of the annotation to be stored.", required = false,
      multiValued = false)
  private String format;

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument(index = 0, name = "uuid",
      description = "Uuid that identifies the root or trunk of the annotation tree",
      required = true, multiValued = false)
  private String uuidString;

  @Argument(index = 1, name = "url", description = "Url to the annotation to be stored",
      required = true, multiValued = false)
  private String urlString;

  // -------------------------------------------------------------------------
  // AStoreCoreCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    TimingMeasurement measurement = getTimingService().getSession().startMeasurement("as:put");
    try {
      UUID uuid = UUID.fromString(uuidString);
      boolean result = false;

      // Use default format if format not specified.
      if (format == null) {
        format = SupportedFormat.RDF_XML;
      }

      try {
        executeCommand(uuid, urlString, format);
        result = true;
      } catch (Exception ex) {
        handleException(ex);
      }
      return result;
    } finally {
      measurement.endMeasurement();
      getTimingService().getSession().endSession();
    }
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public Parser getParserService() {
    BundleContext context = getBundleContext();
    ServiceReference<Parser> serviceRef = context.getServiceReference(Parser.class);
    if (serviceRef == null) {
      throw new ServiceException("Parser service not available");
    }
    return context.getService(serviceRef);
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  protected void executeCommand(UUID uuid, String urlStr, String format) throws Exception {
    URL url = new URL(urlStr);
    String readFormat = format;
    if (getSupportedFormats().containsKey(format.toUpperCase())) {
      readFormat = getSupportedFormats().get(format.toUpperCase());
    }

    // Reed the annotation tree from the given url.
    Annotation annotation = readAnnotation(url, readFormat);
    annotation.setStam(uuid);

    getAnnotationStoreService().put(annotation);
  }

  protected Annotation readAnnotation(URL url, String format) throws Exception {
    logger.trace(String.format("PutAnnotationCommand:readAnnotation( %s, %s )", url, format));

    InputStream in = url.openStream();
    MGraph graph = new SimpleMGraph();
    getParserService().parse(graph, in, format);

    Annotation annotation = new Annotation();
    annotation.setGraph(graph);

    return annotation;
  }
}
