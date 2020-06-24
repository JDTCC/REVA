package nl.overheid.stelsel.gba.reva.bag.importers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.impl.PlainLiteralImpl;
import org.apache.clerezza.rdf.core.impl.SimpleMGraph;
import org.apache.clerezza.rdf.core.impl.TripleImpl;
import org.apache.clerezza.rdf.core.impl.TypedLiteralImpl;
import org.apache.clerezza.rdf.ontologies.RDF;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.overheid.stelsel.digimelding.astore.utils.DateUtils;
import nl.overheid.stelsel.gba.reva.bag.importers.gemeenten.Gemeente;
import nl.overheid.stelsel.gba.reva.bag.importers.gemeenten.GemeenteLoader;
import nl.overheid.stelsel.gba.reva.bag.importers.gemeenten.GemeentenConstants;

@Command( scope = "bag-import", name = "import-gemeenten", description = "Importeer de gemeenten (tabel 33)." )
public class ImportGemeentenCommand extends OsgiCommandSupport {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger( ImportGemeentenCommand.class );

  private static final int KOLOM_GEMEENTECODE = 0;
  private static final int KOLOM_GEMEENTENAAM = 1;
  private static final int KOLOM_NIEUWECODE = 2;
  private static final int KOLOM_BEGINDATUM = 3;
  private static final int KOLOM_EINDDATUM = 4;

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private String laatsteMutatieDatum;

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option( name = "-d", aliases = { "--dryrun" }, description = "Reading the import file without actually importing.", required = false, multiValued = false )
  private boolean isDryRun;

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument( index = 0, name = "file", description = "The file to import.", required = true, multiValued = false )
  private String importFile;

  // -------------------------------------------------------------------------
  // NamedQueryCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    Object obj = null;
    laatsteMutatieDatum = "";

    logger.info( "Started Gemeenten import..." );
    try {
      List<Gemeente> gemeenten = getGemeentenFromFile( new File( importFile ) );
      processGemeenten( gemeenten );
    } catch( Exception ex ) {
      session.getConsole().println( ex.getMessage() );
      logger.warn( ex.getMessage(), ex );
      if( !isDryRun ) {
        session.getConsole().println( "Make sure to read in the old gemeenten again!" );
        logger.warn( "Make sure to read in the old gemeenten again!" );
      }
    }
    logger.info( "Finished Gemeenten import!" );
    return obj;
  }

  // -------------------------------------------------------------------------
  // Private Methods
  // -------------------------------------------------------------------------

  private List<Gemeente> getGemeentenFromFile( File gemeentenFile ) throws IOException {
    BufferedReader br = null;
    String line = "";
    String splitBy = "\",\"";

    List<Gemeente> gemeenten = new ArrayList<>();
    try {
      int linenumber = 0;
      br = new BufferedReader( new InputStreamReader( new FileInputStream( gemeentenFile ), "UTF-16" ) );
      while( (line = br.readLine()) != null ) {
        linenumber++;
        if( linenumber != 1 ) {
          // Trailing empty fields are truncated by split, so add
          // a none emtpy one to prevent truncation.
          line = line + ",\"end\"";
          String[] gemeenteData = unquote( line ).split( splitBy );

          // create car object to store values
          Gemeente gemeente = new Gemeente();
          gemeente.setCode( gemeenteData[KOLOM_GEMEENTECODE] );
          gemeente.setNaam( gemeenteData[KOLOM_GEMEENTENAAM] );
          if( gemeenteData[KOLOM_NIEUWECODE].trim().length() > 0 ) {
            gemeente.setNieuweCode( gemeenteData[KOLOM_NIEUWECODE] );
          }
          bewaarLaatsteMutatieDatum( gemeenteData );

          gemeenten.add( gemeente );
        }
      }
    } finally {
      if( br != null ) {
        try {
          br.close();
        } catch( IOException e ) {
          // ignore
        }
      }
    }
    return gemeenten;
  }

  private void bewaarLaatsteMutatieDatum( String[] gemeenteData ) {
    // Check of er een begin datum is
    String waarde = gemeenteData[KOLOM_BEGINDATUM].trim();
    if( waarde.length() > 0 && laatsteMutatieDatum.compareTo( waarde ) <= 0 ) {
      laatsteMutatieDatum = waarde;
    }

    // Check of er een eind datum is
    waarde = gemeenteData[KOLOM_EINDDATUM].trim();
    if( waarde.length() > 0 && laatsteMutatieDatum.compareTo( waarde ) <= 0 ) {
      laatsteMutatieDatum = waarde;
    }
  }

  private String unquote( String quotedString ) {
    return quotedString.substring( 1, quotedString.length() - 1 );
  }

  private void processGemeenten( List<Gemeente> gemeenten ) {
    MGraph tempGraph = new SimpleMGraph();

    // Store meta data in this graph first.
    // TODO: maybe use provenance instead
    UriRef metainfo = new UriRef( "metainfo" );
    tempGraph.add( new TripleImpl( metainfo, RDF.type, GemeentenConstants.METAINFO ) );
    tempGraph.add( new TripleImpl( metainfo, GemeentenConstants.METAINFO_HEEFT_BESTANDSNAAM, new PlainLiteralImpl( importFile ) ) );
    tempGraph.add( new TripleImpl( metainfo, GemeentenConstants.METAINFO_HEEFT_TIMESTAMP, new TypedLiteralImpl( DateUtils
            .getDateAsISO8601String( new Date() ), new UriRef( "xsd:dateTime" ) ) ) );
    tempGraph.add( new TripleImpl( metainfo, GemeentenConstants.METAINFO_HEEFT_LAATSTEMUTATIEDATUM, new PlainLiteralImpl(
            laatsteMutatieDatum ) ) );

    logger.info( "Processing gemeenten..." );
    session.getConsole().println( "Processing gemeenten..." );
    // First entry is column caption, skip it.
    for( Gemeente gemeente : gemeenten ) {
      session.getConsole().print( "\rProcessing: " + gemeente.getCode() );
      UriRef gemeenteRef = new UriRef( GemeentenConstants.ONTOLOGY + "/" + gemeente.getCode() );
      tempGraph.add( new TripleImpl( gemeenteRef, RDF.type, GemeentenConstants.GEMEENTE ) );
      tempGraph.add( new TripleImpl( gemeenteRef, GemeentenConstants.HEEFT_CODE, new PlainLiteralImpl( gemeente.getCode() ) ) );
      tempGraph.add( new TripleImpl( gemeenteRef, GemeentenConstants.HEEFT_NAAM, new PlainLiteralImpl( gemeente.getNaam() ) ) );
      if( gemeente.getNieuweCode() != null ) {
        tempGraph.add( new TripleImpl( gemeenteRef, GemeentenConstants.HEEFT_NIEUWECODE, new PlainLiteralImpl( gemeente
                .getNieuweCode() ) ) );
      }
    }

    logger.info( "Done processing gemeenten" );
    session.getConsole().println( "\nDone processing gemeenten..." );

    // Bepaal migratie gemeenten alvorens lijst met gemeenten bij te werken.
    List<Gemeente> migratieGemeenten = bepaalMigratieGemeenten( gemeenten );

    if( !isDryRun ) {
      logger.info( "Replacing storage contents..." );
      session.getConsole().println( "Replacing storage contents..." );
      MGraph graph = GraphStorageService.getGraph( getBundleContext(), GemeentenConstants.GEMEENTEN_GRAPH_NAME );
      graph.clear();
      graph.addAll( tempGraph );
      logger.info( "Done replacing storage contents" );
      session.getConsole().println( "Done replacing storage contents" );
    }

    if( !migratieGemeenten.isEmpty() ) {
      logger.info( "Migratie vereist voor de volgende gemeente(n): " );
      session.getConsole().println( "Migratie vereist voor de volgende gemeente(n):" );
      for( Gemeente gemeente : migratieGemeenten ) {
        String bericht = String.format( "%4s %-35s --> %4s", gemeente.getCode(), gemeente.getNaam(), gemeente.getNieuweCode() );
        logger.info( bericht );
        session.getConsole().println( bericht );
      }
    }
  }

  /**
   * Geeft de lijst van gemeenten die een nieuwe gemeentecode hebben gekregen.
   * 
   * @param gemeenten
   *          de te controleren lijst met gemeenten
   * @return Lijst met gemeente objecten.
   */
  private List<Gemeente> bepaalMigratieGemeenten( List<Gemeente> gemeenten ) {
    List<Gemeente> migratieGemeenten = new ArrayList<>();

    // Laad gemeente tabel zoals deze nu wordt gebruikt (oud)
    Map<String, Gemeente> gemeentenOud = GemeenteLoader.getGemeenten( getBundleContext() );

    for( Gemeente gemeente : gemeenten ) {
      // Heeft deze gemeente wel een nieuwe gemeente code?
      if( gemeente.getNieuweCode() == null || "".equals( gemeente.getNieuweCode() ) ) {
        // Geen nieuwe gemeente code dan zeker geen migratie noodzalijk.
        continue;
      }

      Gemeente oudeGemeente = gemeentenOud.get( gemeente.getCode() );
      if( oudeGemeente != null && !gemeente.getNieuweCode().equals( oudeGemeente.getNieuweCode() ) ) {
        migratieGemeenten.add( gemeente );
      }
    }

    return migratieGemeenten;
  }
}
