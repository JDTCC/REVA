package nl.overheid.stelsel.digimelding.astore.timing.commands;

import java.util.Iterator;

import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;
import nl.overheid.stelsel.digimelding.astore.timing.TimingSession;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Karaf shell commando voor het laten zien van session details.
 * 
 */
@Command(scope = "timing", name = "get",
    description = "Retrieves details for the associated timing session.")
public class GetTimingCommand extends TimingCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(GetTimingCommand.class);

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument(index = 0, name = "id",
      description = "The id of the timing session (defaults to the last terminate session).",
      required = false, multiValued = false)
  private String id;

  // -------------------------------------------------------------------------
  // TimingCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() {
    Object obj = null;
    try {
      TimingSession timingSession = null;
      if (id == null) {
        timingSession = getTimingService().getLastSession();
      } else {
        timingSession = getTimingService().findSession(id);
      }

      if (timingSession == null) {
        session.getConsole().format("Can't find timing session.\n");
      } else {
        obj = printTimingSession(timingSession);
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

  private String printTimingSession(TimingSession session) {
    StringBuffer printBuffer = new StringBuffer();

    // Add session details
    printBuffer.append("Session details:\n");
    printBuffer.append("id:       " + session.getId() + "\n");
    printBuffer.append("duration: " + session.getDuration() + "\n");

    // Add thread details
    printBuffer.append("Thread:\n");
    printBuffer.append("  id:     " + session.getThreadInfo().getId() + "\n");
    printBuffer.append("  name:   " + session.getThreadInfo().getName() + "\n");

    // Add measurements
    printBuffer.append("Measurements:\n");
    String prefix = "";
    for (Iterator<TimingMeasurement> iterator = session.getMeasurements().iterator(); iterator
        .hasNext();) {
      TimingMeasurement measurement = iterator.next();
      printMeasurement(printBuffer, measurement, prefix, !iterator.hasNext());
    }

    return printBuffer.toString();
  }

  private void printMeasurement(StringBuffer printBuffer, TimingMeasurement measurement,
      String prefix, boolean isTail) {
    printBuffer.append(prefix).append((isTail ? "\\-- " : "+-- ")).append(measurement.getId())
        .append("\n");
    printBuffer.append(prefix).append((isTail ? "    " : "|   ")).append("|  duration: ")
        .append(measurement.getDuration()).append("\n");
    printBuffer.append(prefix).append((isTail ? "    " : "|   ")).append("|  pending:  ")
        .append(measurement.isPending()).append("\n");

    for (Iterator<TimingMeasurement> iterator = measurement.getChildMeasurements().iterator(); iterator
        .hasNext();) {
      TimingMeasurement childMeasurement = iterator.next();
      printMeasurement(printBuffer, childMeasurement, prefix + (isTail ? "    " : "|   "),
          !iterator.hasNext());
    }
  }
}
