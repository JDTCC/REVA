package nl.overheid.stelsel.gba.reva.bag.importers.gemeenten;

import org.apache.clerezza.rdf.core.UriRef;

public final class GemeentenConstants {

  public static final String ONTOLOGY = "http://www.bprbzk.nl/BRP/informatiebank/tabel33";
  public static final UriRef GEMEENTEN_GRAPH_NAME = new UriRef( ONTOLOGY );

  public static final UriRef METAINFO = new UriRef( ONTOLOGY + "#Meta" );
  public static final UriRef METAINFO_HEEFT_TIMESTAMP = new UriRef( ONTOLOGY + "#heeftTimestamp" );
  public static final UriRef METAINFO_HEEFT_BESTANDSNAAM = new UriRef( ONTOLOGY + "#heeftBestandsnaam" );
  public static final UriRef METAINFO_HEEFT_LAATSTEMUTATIEDATUM = new UriRef( ONTOLOGY + "#heeftLaatsteMutatieDatum" );

  public static final UriRef GEMEENTE = new UriRef( ONTOLOGY + "#Gemeente" );
  public static final UriRef HEEFT_CODE = new UriRef( ONTOLOGY + "#code" );
  public static final UriRef HEEFT_NAAM = new UriRef( ONTOLOGY + "#naam" );
  public static final UriRef HEEFT_NIEUWECODE = new UriRef( ONTOLOGY + "#nieuweCode" );

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  private GemeentenConstants() {
    // Hide, to prevent instantiation.
  }
}
