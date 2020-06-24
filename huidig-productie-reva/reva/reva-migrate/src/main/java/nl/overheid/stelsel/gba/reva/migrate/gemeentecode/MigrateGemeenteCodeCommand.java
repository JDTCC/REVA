package nl.overheid.stelsel.gba.reva.migrate.gemeentecode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import nl.overheid.stelsel.digimelding.astore.AnnotationStoreService;
import nl.overheid.stelsel.digimelding.astore.core.commands.NamedQueryAnnotationCommand;

import org.apache.clerezza.rdf.core.Resource;
import org.apache.clerezza.rdf.core.sparql.ResultSet;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;
import org.apache.karaf.shell.commands.basic.DefaultActionPreparator;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command( scope = "migrate", name = "gemeentecode", description = "Wijzigt voor registraties de gemeente van inschrijving (loketgemeente) naar een nieuwe gemeentecode. Gebruik dubbel-quotes(\") indien de gemeentecode met een 0 begint." )
public class MigrateGemeenteCodeCommand extends OsgiCommandSupport {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger( MigrateGemeenteCodeCommand.class );

  private static final String QUERY_ANALYSE = "migratieAnalyseOudeGemeenten";
  private static final String QUERY_MIGRATE = "migratieWijzigLoketGemeente";
  private static final String QUERY_GEMCODE = "migratieOphalenGemeenteCode";

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument( index = 0, name = "oudeGemeenteCode", description = "De oorspronkelijke gemeentecode", required = false, multiValued = false )
  private String oudeGemeenteCode;

  @Argument( index = 1, name = "nieuweGemeenteCode", description = "De nieuw te gebruiken gemeentecode", required = false, multiValued = false )
  private String nieuweGemeenteCode;

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option( name = "-a", aliases = { "--analyse" }, description = "Bestaande registratie analyseren, geen aanvullende argumenten nodig.", required = false, multiValued = false )
  private boolean isAnalyse;

  @Option( name = "-d", aliases = { "--dryrun" }, description = "Alleen controles uitvoeren, niet echt migreren.", required = false, multiValued = false )
  private boolean isDryRun;

  // -------------------------------------------------------------------------
  // NamedQueryCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    if( isAnalyse ) {
      return doAnalyse();
    } else {
      // Controleer de gemeentecodes.
      boolean beeindigd = true;
      boolean doorgaan = checkGemeenteCode( oudeGemeenteCode, beeindigd, "oudeGemeenteCode" );
      doorgaan &= checkGemeenteCode( nieuweGemeenteCode, !beeindigd, "nieuweGemeenteCode" );

      if( !isDryRun && doorgaan ) {
        // Voer de migratie uit.
        migrateGemeenteCode( oudeGemeenteCode, nieuweGemeenteCode );
      }
    }

    return "";
  }

  // -------------------------------------------------------------------------
  // Private Methods
  // -------------------------------------------------------------------------

  private Object doAnalyse() throws Exception {
    // Delegate query to the named query command
    NamedQueryAnnotationCommand queryCommand = new NamedQueryAnnotationCommand();
    queryCommand.setBundleContext( getBundleContext() );

    // Construct a session for command delegation
    DefaultActionPreparator preparator = new DefaultActionPreparator();
    preparator.prepare( queryCommand, session, Arrays.<Object> asList( QUERY_ANALYSE ) );

    // Execute delegate
    return queryCommand.execute( session );
  }

  private boolean checkGemeenteCode( String gemeenteCode, Boolean beeindigd, String veld ) {
    if( gemeenteCode == null || gemeenteCode.trim().length() == 0 ) {
      // Lege gemeentecode is niet toegestaan.
      output( "Lege gemeentecode voor '" + veld + "' is niet toegestaan!", true );
      return false;
    }

    // Query arguments
    Map<String, String> arguments = new HashMap<>();
    arguments.put( "gemeenteCode", gemeenteCode );

    // De gemeente code opzoeken in tabel33
    String nieuweGemeenteCode = null;
    try {
      Object response = getAnnotationStoreService().namedQuery( QUERY_GEMCODE, arguments );
      if( response instanceof ResultSet ) {
        ResultSet resultSet = ResultSet.class.cast( response );
        if( !resultSet.hasNext() ) {
          // Niet gevonden
          output( "Onbekende gemeentecode '" + gemeenteCode + "', migratie niet toegestaan!", true );
          return false;
        }

        Resource nieuweCode = resultSet.next().get( "nieuweCode" );
        if( nieuweCode != null ) {
          nieuweGemeenteCode = nieuweCode.toString();
        }
      }
    } catch( Exception ex ) {
      output( "Fout bij het controleren van gemeentecode (voor details zie log)", false );
      log.error( "Fout bij het controleren van gemeentecode!", ex );
      return false;
    }

    if( !beeindigd && nieuweGemeenteCode != null ) {
      // Gemeentecode moet nog steeds legitiem zijn.
      output( "Gemeentecode '" + gemeenteCode + "' voor '" + veld + "' is reeds vervallen, gebruik " + nieuweGemeenteCode, true );
      return false;
    }
    return true;
  }

  private void migrateGemeenteCode( String oudeGemeenteCode, String nieuweGemeenteCode ) {
    // Query arguments
    Map<String, String> arguments = new HashMap<>();
    arguments.put( "oudeGemeenteCode", oudeGemeenteCode );
    arguments.put( "nieuweGemeenteCode", nieuweGemeenteCode );

    // Execute migration query
    try {
      getAnnotationStoreService().namedQuery( QUERY_MIGRATE, arguments );
      output( "Gemeentecode migratie uitgevoerd: " + oudeGemeenteCode + " --> " + nieuweGemeenteCode, true );
    } catch( Exception ex ) {
      output( "Gemeentecode migratie niet gelukt voor: " + oudeGemeenteCode, false );
      log.warn( "Gemeentecode migratie niet gelukt voor: " + oudeGemeenteCode, ex );
    }
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
