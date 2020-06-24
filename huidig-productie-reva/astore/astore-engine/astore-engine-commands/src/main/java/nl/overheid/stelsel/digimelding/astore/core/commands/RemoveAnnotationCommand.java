package nl.overheid.stelsel.digimelding.astore.core.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import nl.overheid.stelsel.digimelding.astore.AnnotationStoreService;
import nl.overheid.stelsel.digimelding.astore.core.commands.decorators.DecoratorManager;
import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;
import nl.overheid.stelsel.digimelding.astore.utils.ResourceUtils;

import org.apache.clerezza.rdf.core.sparql.ResultSet;
import org.apache.clerezza.rdf.core.sparql.SolutionMapping;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.osgi.framework.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command(scope = "as", name = "remove", description = "Removes annotation trees from the store.")
public class RemoveAnnotationCommand extends AStoreCoreCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(RemoveAnnotationCommand.class);

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option(name = "-f", aliases = {"--from"},
      description = "Remove entries starting from the given date", required = false,
      multiValued = false)
  private String from = null;

  @Option(name = "-t", aliases = {"--till"},
      description = "Remove entries till the given data.", required = false,
      multiValued = false)
  private String till = null;

  @Option(name = "-l", aliases = {"--limit"},
      description = "Limit the maximum number of entries to remove (default = 1000).", required = false,
      multiValued = false)
  private int limit = 1000;

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument(index = 0, name = "UUID",
      description = "The argument identifying the annotation tree to remove. Use * to remove all.",
      required = true, multiValued = false)
  private String uuidStr;

  // -------------------------------------------------------------------------
  // AStoreCoreCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    TimingMeasurement measurement = getTimingService().getSession().startMeasurement("as:remove");
    try {
      Boolean response = Boolean.FALSE;
      boolean removeAll = "*".equals(uuidStr);

      try {
        if (removeAll) {
          if (from == null & till == null) {
            getAnnotationStoreService().remove();
          } else {
        	removeEntries( from, till );
          }
        } else {
          UUID uuid = UUID.fromString(uuidStr);
          getAnnotationStoreService().remove(uuid);
        }
        response = Boolean.TRUE;
      } catch (Exception ex) {
        handleException(ex);
      }
      return response;
    } finally {
      measurement.endMeasurement();
      getTimingService().getSession().endSession();
    }
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private void removeEntries( final String from, final String till ) throws Exception {
    logger.trace("RemoveAnnotationCommand:removeEntries");
    
    String startingDate = from;
    if ( from == null ) {
      startingDate = "2014-01-01";
    }
    String endingDate = till;
    if ( till == null ) {
    	throw new ServiceException("Till value should be properly set.");  
    }

    AnnotationStoreService service = getAnnotationStoreService();
    Map<String, String> map = new HashMap<String, String>();
    map.put("vanafDatum", startingDate);
    map.put("totenmetDatum", endingDate);
    map.put("limit", String.valueOf(limit));

    Object queryResult = service.namedQuery("showRegistratieStammen", map);
    if (queryResult == null) {
      session.getConsole().println("(No results, so nothing to remove)");
    }
    if (queryResult instanceof ResultSet) {
      session.getConsole().println("Exporting...");
      ResultSet results = ResultSet.class.cast(queryResult);

      // Bepaal de naam van de eerste kolom.
      String kolomNaam = results.getResultVars().get(0);

      // Exporteer elke rij
      while (results.hasNext()) {
        SolutionMapping row = results.next();
        String id = ResourceUtils.getResourceValue(row.get(kolomNaam));
        try {
            session.getConsole().print("\rRemoving " + id);
            UUID uuid = UUID.fromString(id);
            getAnnotationStoreService().remove(uuid);
        } catch (Exception ex) {
          logger.warn("Export failed!", ex);
          session.getConsole().println("\rExport failed " + id);
        }
      }
      session.getConsole().println("\rDone                                           ");
    } else {
      session.getConsole().println("Don't know how to process");
    }
  }
}
