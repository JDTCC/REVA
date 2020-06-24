package nl.overheid.stelsel.digimelding.astore.timing.commands;

import java.util.Collection;

import nl.overheid.stelsel.digimelding.astore.timing.TimingSession;
import nl.overheid.stelsel.digimelding.astore.timing.commands.decorators.PendingTimingSessionDecorator;

import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Karaf shell commando voor the laten zien van pending sessions.
 * 
 */
@Command(scope = "timing", name = "pending",
    description = "Show information on pending (not yet terminated) timing sessions.")
public class PendingTimingCommand extends TimingCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(PendingTimingCommand.class);

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option(name = "-v", aliases = {"verbose"}, description = "Show verbose information.",
      required = false, multiValued = false)
  private boolean isVerbose;

  // -------------------------------------------------------------------------
  // TimingCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() {
    Object obj = null;

    try {
      Collection<TimingSession> sessions = getTimingService().getPendingSessions();
      if (isVerbose) {
        obj = new PendingTimingSessionDecorator(sessions).toString();
      } else {
        obj = String.valueOf(sessions.size());
      }
    } catch (Exception ex) {
      session.getConsole().println(ex.getMessage());
      logger.warn(ex.getMessage(), ex);
    }
    return obj;
  }
}
