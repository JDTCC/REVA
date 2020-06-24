package nl.overheid.stelsel.digimelding.astore.timing.commands;

import java.util.Collection;

import nl.overheid.stelsel.digimelding.astore.timing.TimingSession;
import nl.overheid.stelsel.digimelding.astore.timing.commands.decorators.TimingSessionDecorator;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Karaf shell commando voor het laten zien van afgeronde sessions.
 * 
 */
@Command(scope = "timing", name = "list",
    description = "Lists terminated timing session in reverse chronoligical order.")
public class ListTimingCommand extends TimingCommand {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final int DEFAULT_COUNT = 15;

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(ListTimingCommand.class);

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument(index = 0, name = "count",
      description = "The number of entries to show (defaults to 15).", required = false,
      multiValued = false)
  private Integer count = DEFAULT_COUNT;

  // -------------------------------------------------------------------------
  // TimingCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() {
    Object obj = null;

    try {
      Collection<TimingSession> sessions = getTimingService().getLastSessions(count);
      obj = new TimingSessionDecorator(sessions).toString();
    } catch (Exception ex) {
      session.getConsole().println(ex.getMessage());
      logger.warn(ex.getMessage(), ex);
    }
    return obj;
  }

}
