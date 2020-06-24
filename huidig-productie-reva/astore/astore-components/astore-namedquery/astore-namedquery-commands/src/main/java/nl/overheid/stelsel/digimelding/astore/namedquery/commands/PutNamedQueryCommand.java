package nl.overheid.stelsel.digimelding.astore.namedquery.commands;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(scope = "namedQuery", name = "put",
    description = "Associates the specified query with the given name.")
public class PutNamedQueryCommand extends NamedQueryCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(PutNamedQueryCommand.class);

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option(name = "-u", aliases = {"url"},
      description = "Retrieves the query from the specified url.", required = false,
      multiValued = false)
  private boolean isUrl;

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument(index = 0, name = "name", description = "The name for the query.", required = true,
      multiValued = false)
  private String name;

  @Argument(index = 1, name = "query", description = "The query to be stored.", required = true,
      multiValued = false)
  private String queryString;

  // -------------------------------------------------------------------------
  // NamedQueryCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    Object obj = null;

    try {
      String query = queryString;
      if (isUrl) {
        query = retrieveQuery(queryString);
      }

      obj = getNamedQueryService().putNamedQuery(name, query);

      if (obj != null) {
        session.getConsole().println("Replaced existing query!");
      } else {
        session.getConsole().format("Query stored under '%s'", name);
      }
    } catch (Exception ex) {
      session.getConsole().println(ex.getMessage());
      logger.warn(ex.getMessage(), ex);
    }

    return obj;
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

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
