package nl.overheid.stelsel.gba.reva.bag.importers.gemeenten;

import java.util.HashMap;
import java.util.Map;

import org.apache.clerezza.rdf.core.PlainLiteral;
import org.apache.clerezza.rdf.core.Resource;
import org.apache.clerezza.rdf.core.sparql.ResultSet;
import org.apache.clerezza.rdf.core.sparql.SolutionMapping;
import org.osgi.framework.BundleContext;

import nl.overheid.stelsel.gba.reva.bag.importers.GraphStorageService;

/**
 * Deze class leest alle gemeenten uit de triple store zoals deze eerder zijn
 * zijn ingeladen.
 * 
 */
public final class GemeenteLoader {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  //@formatter:off
  private static final String GET_GEMEENTEN_QUERY = 
          "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
          "PREFIX gem:  <http://www.bprbzk.nl/BRP/informatiebank/tabel33#>\n" + 
          "SELECT ?code ?naam ?nieuweCode\n" + 
          "WHERE {\n" + 
          "  GRAPH <http://www.bprbzk.nl/BRP/informatiebank/tabel33> {\n" + 
          "    ?gemeente rdf:type gem:Gemeente ;\n" +
          "      gem:code ?code ;\n" + 
          "      gem:naam ?naam .\n" + 
          "      OPTIONAL { ?gemeente gem:nieuweCode ?nieuweCode . }\n" + 
          "  }\n" + 
          "}\n" + 
          "ORDER BY ASC(?code)";
  //@formatter:on

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  private GemeenteLoader() {
    // prevent instantiation
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  public static Map<String, Gemeente> getGemeenten( BundleContext context ) {
    Map<String, Gemeente> gemeenten = new HashMap<String, Gemeente>();
    Object queryResult = GraphStorageService.runQuery( context, GET_GEMEENTEN_QUERY );
    if( queryResult instanceof ResultSet ) {
      ResultSet gemeenteLijst = ResultSet.class.cast( queryResult );

      // Bepaal alle gemeeente objecten
      for( ; gemeenteLijst.hasNext(); ) {
        SolutionMapping row = gemeenteLijst.next();
        Gemeente gemeente = new Gemeente();
        // lees gemeente alle velden
        gemeente.setNaam( getValue( row, "naam" ) );
        gemeente.setCode( getValue( row, "code" ) );
        gemeente.setNieuweCode( getValue( row, "nieuweCode" ) );
        gemeenten.put( gemeente.getCode(), gemeente );
      }
    }

    return gemeenten;
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private static String getValue( SolutionMapping row, String key ) {
    String value = null;

    Resource resource = row.get( key );
    if( resource instanceof PlainLiteral ) {
      value = PlainLiteral.class.cast( resource ).getLexicalForm();
    }

    return value;
  }
}
