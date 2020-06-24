package nl.overheid.stelsel.digimelding.astore.namedquery.commands;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(scope = "namedQuery", name = "remove",
    description = "Removes the named query with the specified name.")
public class RemoveNamedQueryCommand extends NamedQueryCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(RemoveNamedQueryCommand.class);

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
      if (getNamedQueryService().containsKey(name)) {
        obj = getNamedQueryService().remove(name);
      } else {
        session.getConsole().format("No query stored under '%s'", name);
      }
    } catch (Exception ex) {
      session.getConsole().println(ex.getMessage());
      logger.warn(ex.getMessage(), ex);
    }
    return obj;
  }

}
