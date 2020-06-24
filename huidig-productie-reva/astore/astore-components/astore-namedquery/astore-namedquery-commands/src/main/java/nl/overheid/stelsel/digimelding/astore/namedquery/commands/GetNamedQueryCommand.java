package nl.overheid.stelsel.digimelding.astore.namedquery.commands;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(scope = "namedQuery", name = "get",
    description = "Retrieves the query associated with the given name.")
public class GetNamedQueryCommand extends NamedQueryCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(GetNamedQueryCommand.class);

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument(index = 0, name = "name", description = "The name for the query.", required = true,
      multiValued = false)
  private String name;

  // -------------------------------------------------------------------------
  // NamedQueryCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    Object obj = null;

    try {
      obj = getNamedQueryService().getNamedQuery(name);
      if (obj == null) {
        session.getConsole().format("No query stored under '%s'\n", name);
      }
    } catch (Exception ex) {
      session.getConsole().println(ex.getMessage());
      logger.warn(ex.getMessage(), ex);
    }
    return obj;
  }

}
