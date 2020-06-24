package nl.overheid.stelsel.digimelding.astore.core.commands;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.overheid.stelsel.digimelding.astore.AnnotationStoreService;
import nl.overheid.stelsel.digimelding.astore.core.commands.decorators.DecoratorManager;
import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(scope = "as", name = "namedQuery", description = "Retrieve annotation trees")
public class NamedQueryAnnotationCommand extends AStoreCoreCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(NamedQueryAnnotationCommand.class);

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument(index = 0, name = "namedQuery", description = "The named query to be used",
      required = true, multiValued = false)
  private String namedQuery;

  @Argument(index = 1, name = "parameters", description = "The parameters to be used in the query",
      required = false, multiValued = true)
  private List<String> parameters;

  // -------------------------------------------------------------------------
  // AStoreCoreCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("as:namedQuery");
    try {
      Map<String, String> parameterMap = parseParameters(parameters);

      Object obj = null;
      try {
        obj = executeCommand(namedQuery, parameterMap);
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
  // Private methods
  // -------------------------------------------------------------------------

  private static Map<String, String> parseParameters(List<String> parameters) {
    List<String> params = Collections.<String>emptyList();
    if (parameters != null) {
      params = parameters;
    }

    Map<String, String> map = new HashMap<String, String>();
    for (String param : params) {
      int separator = param.indexOf('=');
      String name = param.substring(0, separator).trim();
      String value = param.substring(separator + 1).trim();
      map.put(name, value);
    }
    return map;
  }

  protected Object executeCommand(String namedQuery, Map<String, String> parameters)
      throws Exception {
    logger.trace("NamedQueryAnnotationCommand:executeCommand ");
    AnnotationStoreService service = getAnnotationStoreService();

    Object queryResult = service.namedQuery(namedQuery, parameters);
    if (queryResult == null) {
      session.getConsole().println("(No results)");
    } else {
      queryResult = DecoratorManager.decorate(queryResult);
    }

    return queryResult;
  }
}
