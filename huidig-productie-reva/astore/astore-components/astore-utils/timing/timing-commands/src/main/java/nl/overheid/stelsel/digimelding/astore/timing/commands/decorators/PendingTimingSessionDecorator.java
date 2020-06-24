package nl.overheid.stelsel.digimelding.astore.timing.commands.decorators;

import java.util.Collection;

import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;
import nl.overheid.stelsel.digimelding.astore.timing.TimingSession;
import nl.overheid.stelsel.digimelding.astore.utils.CollectionUtils;
import nl.xup.tableprinter.ColumnHeader.Alignment;
import nl.xup.tableprinter.Row;
import nl.xup.tableprinter.TablePrinter;

/**
 * Decorator for Collection<TimingSession>. This decorator add a proper toString() to the original
 * type.
 * 
 */
public class PendingTimingSessionDecorator {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private final Collection<TimingSession> decorated;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Creates a new Timing session decorator to decorate pending sessions.
   * 
   * @param decorated the session to be decorated.
   */
  public PendingTimingSessionDecorator(Collection<TimingSession> decorated) {
    this.decorated = decorated;
  }

  // -------------------------------------------------------------------------
  // Object overrides
  // -------------------------------------------------------------------------

  @Override
  public String toString() {
    TablePrinter table = new TablePrinter();
    table.createColumnHeader("id");
    table.createColumnHeader("duration", Alignment.RIGHT);
    table.createColumnHeader("pending measurement");

    for (TimingSession session : decorated) {
      Row tableRow = table.createRow();
      tableRow.addCell(session.getId());
      tableRow.addCell(String.valueOf(session.getDuration()));

      TimingMeasurement pendingMeasurement = findDeepestPending(session);
      if (pendingMeasurement != null) {
        tableRow.addCell(pendingMeasurement.getId());
      } else {
        tableRow.addCell("unknown");
      }
    }

    String response = table.toString();
    if (response.isEmpty()) {
      response = "(empty)";
    }
    return response;
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private TimingMeasurement findDeepestPending(TimingSession session) {
    for (TimingMeasurement measurement : session.getMeasurements()) {
      TimingMeasurement deeperPending = findDeepestPending(measurement);
      if (deeperPending != null) {
        return deeperPending;
      }
    }

    return null;
  }

  private TimingMeasurement findDeepestPending(TimingMeasurement measurement) {
    TimingMeasurement currentPending = null;
    Collection<TimingMeasurement> measurements = null;

    if (measurement != null) {
      if (measurement.isPending()) {
        currentPending = measurement;
        measurements = measurement.getChildMeasurements();
      } else {
        // This one is not pending so no pending childs either.
        return null;
      }
    }

    // Check for even deeper ones.
    for (TimingMeasurement measurement2 : CollectionUtils.nullIsEmpty(measurements)) {
      TimingMeasurement deeperPending = findDeepestPending(measurement2);
      if (deeperPending != null) {
        return deeperPending;
      }
    }

    return currentPending;
  }
}
