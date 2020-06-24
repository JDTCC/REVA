package nl.overheid.stelsel.gba.reva.bag.commands;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.clerezza.rdf.core.Resource;
import org.apache.clerezza.rdf.core.access.QueryableTcProvider;
import org.apache.clerezza.rdf.core.sparql.ResultSet;
import org.apache.clerezza.rdf.core.sparql.SolutionMapping;
import org.apache.clerezza.rdf.core.sparql.query.Variable;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.osgi.framework.ServiceException;

import nl.overheid.stelsel.digimelding.astore.utils.ResourceUtils;
import nl.overheid.stelsel.gba.reva.bag.BagService;

@Command( scope = "bag", name = "metainfo", description = "Retieve meta information on bag import." )
public class MetaInfoCommand extends OsgiCommandSupport {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  //@formatter:off
  private static final String QUERY_GEMEENTE_META = 
          "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
          "PREFIX gem:  <http://www.bprbzk.nl/BRP/informatiebank/tabel33#>\n" +
          "\n" +
          "SELECT ?timestampImport ?importedBestand ?laatsteMutatie \n" +
          "WHERE {\n" +
          "  GRAPH <http://www.bprbzk.nl/BRP/informatiebank/tabel33> {\n" + 
          "    ?metainfo rdf:type gem:Meta ;\n" +
          "      gem:heeftTimestamp ?timestampImport ;\n" + 
          "      gem:heeftBestandsnaam ?importedBestand .\n" + 
          "    OPTIONAL {\n" +
          "      ?metainfo gem:heeftLaatsteMutatieDatum ?laatsteMutatie .\n" + 
          "    }\n" +
          "  }\n" +
          "}";

  private static final String QUERY_GEMWPL_META = 
          "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
          "PREFIX rela: <http://www.digimelding.nl/2013/11/relaties#>\n" +
          "\n" +
          "SELECT ?timestampImport ?importedBestand ?extractDatum\n" +
          "WHERE {\n" +
          "  GRAPH <http://www.digimelding.nl/2013/11/relaties> {\n" +
          "    ?metainfo rdf:type rela:Meta ;\n" +
          "      rela:heeftTimestamp ?timestampImport ;\n" +
          "      rela:heeftBestandsnaam ?importedBestand .\n" +
          "    OPTIONAL {\n" +
          "      ?metainfo rela:heeftExtractDatum ?extractDatum .\n" +
          "    }\n" +
          "  }\n" +
          "}";
  //@formatter:on

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private QueryableTcProvider provider = null;

  // -------------------------------------------------------------------------
  // AbstractAction overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    Object obj = null;

    showGemeentenMetaInfo();
    showGemeenteWoonplaatsRelatiesMetaInfo();
    showBagMetaInfo();

    return obj;
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private QueryableTcProvider getQueryProvider() {
    if( provider == null ) {
      provider = getService( QueryableTcProvider.class );

      if( provider == null ) {
        throw new ServiceException( "QueryableTcProvider service not available" );
      }
    }

    return provider;
  }

  private BagService getBagService() {
    BagService service = getService( BagService.class );

    if( service == null ) {
      throw new ServiceException( "BAG service not available" );
    }

    return service;
  }

  private void showGemeentenMetaInfo() {
    Map<String, String> metaInfo = getMetaInfo( QUERY_GEMEENTE_META );

    session.getConsole().println( "Gemeente tabel:" );
    printMetaInfo( session.getConsole(), metaInfo );
    session.getConsole().println();
  }

  private void showGemeenteWoonplaatsRelatiesMetaInfo() {
    Map<String, String> metaInfo = getMetaInfo( QUERY_GEMWPL_META );

    session.getConsole().println( "Gemeente/Woonplaats relatie tabel:" );
    printMetaInfo( session.getConsole(), metaInfo );
    session.getConsole().println();
  }

  private Map<String, String> getMetaInfo( String query ) {
    Map<String, String> metaInfo = new HashMap<>();
    QueryableTcProvider queryProvider = getQueryProvider();

    Object response = queryProvider.executeSparqlQuery( query, null );
    if( response instanceof ResultSet ) {
      ResultSet resultSet = ResultSet.class.cast( response );
      if( resultSet.hasNext() ) {
        SolutionMapping row = resultSet.next();
        for( Entry<Variable, Resource> entry : row.entrySet() ) {
          metaInfo.put( entry.getKey().getName(), ResourceUtils.getResourceValue( entry.getValue() ) );
        }
      }
    } else {
      metaInfo.put( "Ontwikkelfout", "Geen gewone select-query!" );
    }

    return metaInfo;
  }

  private void showBagMetaInfo() {
    Map<String, String> metaInfo = new HashMap<>();
    Properties props = getBagService().getMetaInfo();
    for( Map.Entry<?, ?> entry : props.entrySet() ) {
      metaInfo.put( (String) entry.getKey(), (String) entry.getValue() );
    }

    session.getConsole().println( "BAG:" );
    printMetaInfo( session.getConsole(), metaInfo );
    session.getConsole().println();
  }

  private void printMetaInfo( PrintStream stream, Map<String, String> metaInfo ) {
    for( Entry<String, String> entry : metaInfo.entrySet() ) {
      stream.format( "%s : %s\n", entry.getKey(), entry.getValue() );
    }
    if( metaInfo.isEmpty() ) {
      stream.println( "Geen informatie beschikbaar!" );
    }
  }
}
