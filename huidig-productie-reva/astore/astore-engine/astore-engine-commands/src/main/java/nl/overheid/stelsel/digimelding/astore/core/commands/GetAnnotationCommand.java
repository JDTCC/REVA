package nl.overheid.stelsel.digimelding.astore.core.commands;

import java.util.UUID;

import nl.overheid.stelsel.digimelding.astore.AnnotationTree;
import nl.overheid.stelsel.digimelding.astore.core.commands.decorators.AnnotationTreeDecorator;
import nl.overheid.stelsel.digimelding.astore.core.commands.decorators.DecoratorManager;
import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;

import org.apache.clerezza.rdf.core.serializedform.Serializer;
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
    name = "get",
    description = "Retrieve annotation(s) from the store by either id, target or reference. (defaults to id=GUID)")
public class GetAnnotationCommand extends AStoreCoreCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(GetAnnotationCommand.class);

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option(name = "-f", aliases = {"--format", "--fmt"}, description = "RDF format to be returned.",
      required = false, multiValued = false)
  private String format;

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument(index = 0, name = "UUID or URI",
      description = "The argument identifying annotation(s).", required = true, multiValued = false)
  private String arg;

  // -------------------------------------------------------------------------
  // AStoreCoreCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    TimingMeasurement measurement = getTimingService().getSession().startMeasurement("as:get");
    try {
      // Use default format if format not specified.
      if (format == null) {
        format = SupportedFormat.RDF_XML;
      }
      if (getSupportedFormats().containsKey(format.toUpperCase())) {
        format = getSupportedFormats().get(format.toUpperCase());
      }

      Object obj = null;
      try {
        obj = getByID(arg, format);
        if (obj != null) {
          obj = obj.toString();
        }
      } catch (Exception ex) {
        handleException(ex);
      }

      return obj;
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
      throw new ServiceException("Serializer service not available");
    }
    return context.getService(serviceRef);
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  protected AnnotationTree getByID(String arg, String format) throws Exception {
    logger.trace("GetAnnotationCommand:getByID " + arg);
    UUID uuid = UUID.fromString(arg);

    AnnotationTree annotationTree = getAnnotationStoreService().get(uuid);
    annotationTree = DecoratorManager.decorate(annotationTree);
    if (annotationTree instanceof AnnotationTreeDecorator) {
      AnnotationTreeDecorator decorator = AnnotationTreeDecorator.class.cast(annotationTree);
      decorator.format(format);
      decorator.setSerializerService(getSerializerService());
    }
    return annotationTree;
  }
}
