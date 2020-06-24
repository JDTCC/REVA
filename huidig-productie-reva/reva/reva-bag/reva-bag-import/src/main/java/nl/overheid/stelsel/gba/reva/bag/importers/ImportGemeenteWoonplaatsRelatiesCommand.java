package nl.overheid.stelsel.gba.reva.bag.importers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

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
import org.w3c.dom.Document;

import nl.overheid.stelsel.digimelding.astore.utils.DateUtils;
import nl.overheid.stelsel.gba.reva.bag.importers.gemeenten.Gemeente;
import nl.overheid.stelsel.gba.reva.bag.importers.gemeenten.GemeenteLoader;
import nl.overheid.stelsel.gba.reva.bag.importers.gemeentewoonplaatsrelatie.BAGExtract;
import nl.overheid.stelsel.gba.reva.bag.importers.gemeentewoonplaatsrelatie.RelatieConstants;
import nl.overheid.stelsel.gba.reva.bag.importers.gemeentewoonplaatsrelatie.Relaties.Relatie;
import nl.overheid.stelsel.gba.reva.bag.importers.gemeentewoonplaatsrelatie.Util;

@Command( scope = "bag-import", name = "import-relatie", description = "Importeer de gemeente-woonplaats relatie tabel." )
public class ImportGemeenteWoonplaatsRelatiesCommand extends OsgiCommandSupport {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger( ImportGemeenteWoonplaatsRelatiesCommand.class );

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

    logger.info( "Started Gemeente/Woonplaats relatie import..." );
    try {
      File dataFile = new File( importFile );
      List<Relatie> relaties = getRelatiesFromFile( dataFile );
      BAGExtract extractInfo = getExtractDatumFromFile( dataFile );
      processRelaties( extractInfo, relaties );
    } catch( Exception ex ) {
      session.getConsole().println( ex.getMessage() );
      logger.warn( ex.getMessage(), ex );
      if( !isDryRun ) {
        session.getConsole().println( "Make sure to read in the old relaties again!" );
        logger.warn( "Make sure to read in the old relaties again!" );
      }
    }
    logger.info( "Finished Gemeente/Woonplaats relatie import!" );
    return obj;
  }

  // -------------------------------------------------------------------------
  // Private Methods
  // -------------------------------------------------------------------------

  private synchronized List<Relatie> getRelatiesFromFile( File relatiesFile ) throws TransformerException, JAXBException {
    List<Relatie> relaties = null;

    // Set up the thread contect classloader (TCCL) for XSLT to work properly.
    ClassLoader orig = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader( this.getClass().getClassLoader() );

    try {
      Document document = getDomDocument( relatiesFile );
      relaties = new Util().getGemeenteWoonplaatsRelaties( document );
    } finally {
      // Restore TCCL now that XSLT processing is done.
      Thread.currentThread().setContextClassLoader( orig );
    }

    return relaties;
  }

  private synchronized BAGExtract getExtractDatumFromFile( File relatiesFile ) throws TransformerException, JAXBException {
    BAGExtract extractInfo = null;

    // Set up the thread contect classloader (TCCL) for XSLT to work properly.
    ClassLoader orig = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader( this.getClass().getClassLoader() );

    try {
      Document document = getDomDocument( relatiesFile );
      extractInfo = new Util().getExtractDatum( document );
    } finally {
      // Restore TCCL now that XSLT processing is done.
      Thread.currentThread().setContextClassLoader( orig );
    }

    return extractInfo;
  }

  private Document getDomDocument( File file ) throws TransformerException {
    Source source = new StreamSource( file );
    DOMResult result = new DOMResult();

    TransformerFactory factory = TransformerFactory.newInstance();
    Transformer transformer = factory.newTransformer();
    transformer.transform( source, result );

    return (Document) result.getNode();
  }

  private void processRelaties( BAGExtract extractInfo, List<Relatie> relaties ) {
    MGraph tempGraph = new SimpleMGraph();
    Set<String> gebruikteGemeenteCodes = new HashSet<>();

    // Store meta data in this graph first.
    // TODO: maybe use provenance instead
    UriRef metainfo = new UriRef( "metainfo" );
    tempGraph.add( new TripleImpl( metainfo, RDF.type, RelatieConstants.METAINFO ) );
    tempGraph.add( new TripleImpl( metainfo, RelatieConstants.METAINFO_HEEFT_BESTANDSNAAM, new PlainLiteralImpl( importFile ) ) );
    tempGraph.add( new TripleImpl( metainfo, RelatieConstants.METAINFO_HEEFT_TIMESTAMP, new TypedLiteralImpl( DateUtils
            .getDateAsISO8601String( new Date() ), new UriRef( "xsd:dateTime" ) ) ) );
    tempGraph.add( new TripleImpl( metainfo, RelatieConstants.METAINFO_HEEFT_EXTRACTDATUM, new PlainLiteralImpl( extractInfo
            .getDatum() ) ) );

    logger.info( "Processing relaties..." );
    session.getConsole().println( "Processing relaties..." );
    for( Relatie relatie : relaties ) {
      session.getConsole().print( "\rProcessing: " + relatie.getWoonplaats() );
      gebruikteGemeenteCodes.add( relatie.getGemeente() );
      UriRef gemeente = new UriRef( "http://www.kadaster.nl/gemeente/" + relatie.getGemeente() );
      tempGraph.add( new TripleImpl( gemeente, RDF.type, RelatieConstants.GEMEENTE ) );
      tempGraph.add( new TripleImpl( gemeente, RelatieConstants.HEEFT_PLAATS, new PlainLiteralImpl( relatie.getWoonplaats() ) ) );
    }
    logger.info( "Done processing relaties" );
    session.getConsole().println( "\nDone processing relaties..." );

    // Bepaal migratie gemeenten alvorens lijst met gemeenten bij te werken.
    List<Gemeente> migratieGemeenten = controleerMigratieGemeenten( gebruikteGemeenteCodes );

    if( !isDryRun ) {
      logger.info( "Replacing storage contents..." );
      session.getConsole().println( "Replacing storage contents..." );
      MGraph graph = GraphStorageService.getGraph( getBundleContext(), RelatieConstants.RELATIE_GRAPH_NAME );
      graph.clear();
      graph.addAll( tempGraph );
      logger.info( "Done replacing storage contents" );
      session.getConsole().println( "Done replacing storage contents" );
    }

    if( !migratieGemeenten.isEmpty() ) {
      logger.info( "Import bevat niet bestaande gemeenten: " );
      session.getConsole().println( "Import bevat niet bestaande gemeenten:" );
      for( Gemeente gemeente : migratieGemeenten ) {
        String bericht = String.format( "%4s %-35s --> %4s", gemeente.getCode(), gemeente.getNaam(), gemeente.getNieuweCode() );
        logger.info( bericht );
        session.getConsole().println( bericht );
      }
    }
  }

  /**
   * Geeft de lijst van gemeenten die een nieuwe gemeentecode hebben gekregen en
   * toch voorkomen in de lijst met gemeenten waarvoor een woonplaats relatie
   * bestaat..
   * 
   * @param gemeenteCodes
   *          de te controleren lijst met gemeente codes
   * @return Lijst met gemeente objecten.
   */
  private List<Gemeente> controleerMigratieGemeenten( Collection<String> gemeenteCodes ) {
    List<Gemeente> migratieGemeenten = new ArrayList<>();

    // Laad gemeente tabel zoals deze nu wordt gebruikt (oud)
    Map<String, Gemeente> gemeenten = GemeenteLoader.getGemeenten( getBundleContext() );

    for( String gemeenteCode : gemeenteCodes ) {
      // Kennen we deze gemeente code wel?
      if( gemeenten.containsKey( gemeenteCode ) ) {
        Gemeente gemeente = gemeenten.get( gemeenteCode );
        if( !(gemeente.getNieuweCode() == null || "".equals( gemeente.getNieuweCode() )) ) {
          migratieGemeenten.add( gemeente );
        }
      } else {
        // Onbekende gemeente
        Gemeente gemeente = new Gemeente();
        gemeente.setCode( gemeenteCode );
        gemeente.setNaam( "?" );
        gemeente.setNieuweCode( "?" );
        migratieGemeenten.add( gemeente );
      }
    }

    return migratieGemeenten;
  }
}
