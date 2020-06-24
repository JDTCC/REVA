package nl.overheid.stelsel.digimelding.astore.namedquery.commands;

import org.apache.felix.gogo.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(scope = "namedQuery", name = "list", description = "Lists all stored named queries.")
public class ListNamedQueriesCommand extends NamedQueryCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(ListNamedQueriesCommand.class);

  // -------------------------------------------------------------------------
  // NamedQueryCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    Object obj = null;

    try {
      obj = getNamedQueryService().keySet();
    } catch (Exception ex) {
      session.getConsole().println(ex.getMessage());
      logger.warn(ex.getMessage(), ex);
    }
    return obj;
  }

}
