package nl.overheid.stelsel.digimelding.astore.core.commands;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import nl.overheid.stelsel.digimelding.astore.AnnotationStoreService;
import nl.overheid.stelsel.digimelding.astore.core.commands.decorators.DecoratorManager;
import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;

import org.apache.commons.io.IOUtils;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(scope = "as", name = "query", description = "Retrieves information from the store.")
public class QueryAnnotationCommand extends AStoreCoreCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(QueryAnnotationCommand.class);

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option(name = "-u", aliases = {"--url"},
      description = "Retrieves the query from the specified url.", required = false,
      multiValued = false)
  private boolean isUrl;

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument(index = 0, name = "query", description = "The query to be executed.", required = true,
      multiValued = false)
  private String queryString;

  // -------------------------------------------------------------------------
  // AStoreCoreCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    TimingMeasurement measurement = getTimingService().getSession().startMeasurement("as:query");
    try {
      Object obj = null;

      try {
        String query = queryString;
        if (isUrl) {
          query = retrieveQuery(queryString);
        }

        obj = executeQuery(query).toString();
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

  protected Object executeQuery(String queryString) throws Exception {
    logger.trace(String.format("QueryAnnotationCommand:executeQuery( %s )", queryString));
    AnnotationStoreService service = getAnnotationStoreService();

    Object queryResult = service.query(queryString);
    if (queryResult == null) {
      session.getConsole().println("(No results)");
    } else {
      queryResult = DecoratorManager.decorate(queryResult);
    }

    return queryResult;
  }

  private static String retrieveQuery(String urlStr) throws IOException {
    URL url = new URL(urlStr);
    InputStream in = url.openStream();

    try {
      return IOUtils.toString(in, "UTF-8");
    } finally {
      in.close();
    }
  }
}
