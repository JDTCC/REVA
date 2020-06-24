package nl.overheid.stelsel.gba.reva.migrate.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.clerezza.rdf.core.sparql.ResultSet;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.overheid.stelsel.digimelding.astore.AnnotationStoreService;
import nl.overheid.stelsel.digimelding.astore.utils.ResourceUtils;

@Command( scope = "migrate", name = "model", description = "Migreert data van het oude model naar het nieuwe model." )
public class MigrateDataModelCommand extends OsgiCommandSupport {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger( MigrateDataModelCommand.class );

  private static class Pair {
    String queryName;
    String message;
    
    Pair( final String queryName, final String message ) {
      this.queryName = queryName;
      this.message = message;
    }
  }
  
  private static final List<Pair> queries = new ArrayList<>();

  static {
    queries.add( new Pair( "migratieIncorrectDateType", "Migreren oa:annotatedAt van xsd:xsd:dateTime --> xsd:dateTime") );  
    queries.add( new Pair( "migratieDoubleAnnotatedAt", "Verwijderen van oa:annotateAt^^xsd:string indien dubbel") );  
    queries.add( new Pair( "migratieIncorrectDateIntType", "Migreren: aanmaakDatumInt van xsd:string --> xsd:integer") );  
  }

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  // None

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option( name = "-s", aliases = { "--batchSize" }, description = "Limit batch size (defaults to 5000).", required = false, multiValued = false )
  private long batchSize = 5000L;

  // -------------------------------------------------------------------------
  // NamedQueryCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {

	// Determine the number of records to be migrated
//	long countRegistrations = getRegistrationCount();
//	long countRuns = (countRegistrations / batchSize) + 1;
//    session.getConsole().print( String.format("Migreren van %d registraties in %d batches van %d elk.\n", countRegistrations, countRuns, batchSize) );
//	  
//    try {
//      for ( Pair pair : queries ) {
//        output( pair.message, true );
//        for (int i = 0; i < countRuns; i++) {
//          session.getConsole().print( String.format("\r%s %d/%d", pair.message, i+1, countRuns ));
//          runQuery(pair.queryName);		 
//		}
//        session.getConsole().print( "\n" );
//      }
//    } catch( Exception ex ) {
//      output( "\nMigratie fout! zie log.", false );
//      log.warn( "Migratie niet gelukt!", ex );
//    }

    return "No automated migration, please revert to using migration scripts manually!";
  }

  // -------------------------------------------------------------------------
  // Private Methods
  // -------------------------------------------------------------------------

  private long getRegistrationCount() throws Exception {
	long count = 0L;
	
    // Query arguments
    Map<String, String> arguments = new HashMap<>();

    // Execute migration query
    Object result = getAnnotationStoreService().namedQuery( "countStammen", arguments );

    if( result instanceof ResultSet ) {
      ResultSet results = ResultSet.class.cast( result );
      String firstField = results.getResultVars().get( 0 );
      if( results.hasNext() ) {
        count = Long.parseLong( ResourceUtils.getResourceValue( results.next().get( firstField ) ) );
      }
    } else {
      throw new RuntimeException( "Invalid size retrieval query 'countStammen' check you query." );
    }

    return count;
  }

  private void runQuery( String queryName ) throws Exception {
    // Query arguments
    Map<String, String> arguments = new HashMap<>();
    arguments.put("limit", String.valueOf(batchSize));

    // Execute migration query
    getAnnotationStoreService().namedQuery( queryName, arguments );
  }

  private void output( String message, boolean log ) {
    session.getConsole().println( message );
    if( log ) {
      logger.info( message );
    }
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public AnnotationStoreService getAnnotationStoreService() {
    BundleContext context = getBundleContext();
    ServiceReference<AnnotationStoreService> serviceRef = context.getServiceReference( AnnotationStoreService.class );
    if( serviceRef == null ) {
      throw new ServiceException( "AnnotationStoreService service not available" );
    }
    return context.getService( serviceRef );
  }
}
