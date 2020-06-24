package nl.overheid.stelsel.digimelding.astore.timing.commands.decorators;

import java.util.Collection;

import nl.overheid.stelsel.digimelding.astore.timing.TimingSession;
import nl.xup.tableprinter.ColumnHeader.Alignment;
import nl.xup.tableprinter.Row;
import nl.xup.tableprinter.TablePrinter;

/**
 * Decorator for Collection<TimingSession>. This decorator add a proper toString() to the original
 * type.
 * 
 */
public class TimingSessionDecorator {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private final Collection<TimingSession> decorated;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Creates a new timing session decorator.
   * 
   * @param decorated the session to be decorated.
   */
  public TimingSessionDecorator(Collection<TimingSession> decorated) {
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

    for (TimingSession session : decorated) {
      Row tableRow = table.createRow();
      tableRow.addCell(session.getId());
      tableRow.addCell(String.valueOf(session.getDuration()));
    }

    String response = table.toString();
    if (response.isEmpty()) {
      response = "(empty)";
    }
    return response;
  }
}
