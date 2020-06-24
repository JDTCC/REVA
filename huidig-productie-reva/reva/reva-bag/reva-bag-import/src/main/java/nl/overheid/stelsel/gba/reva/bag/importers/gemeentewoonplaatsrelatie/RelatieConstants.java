package nl.overheid.stelsel.gba.reva.bag.importers.gemeentewoonplaatsrelatie;

import org.apache.clerezza.rdf.core.UriRef;

public final class RelatieConstants {

  public static final String ONTOLOGY = "http://www.digimelding.nl/2013/11/relaties";
  public static final UriRef RELATIE_GRAPH_NAME = new UriRef( ONTOLOGY );

  public static final UriRef METAINFO = new UriRef( ONTOLOGY + "#Meta" );
  public static final UriRef METAINFO_HEEFT_TIMESTAMP = new UriRef( ONTOLOGY + "#heeftTimestamp" );
  public static final UriRef METAINFO_HEEFT_BESTANDSNAAM = new UriRef( ONTOLOGY + "#heeftBestandsnaam" );
  public static final UriRef METAINFO_HEEFT_EXTRACTDATUM = new UriRef( ONTOLOGY + "#heeftExtractDatum" );

  public static final UriRef GEMEENTE = new UriRef( ONTOLOGY + "#Gemeente" );
  public static final UriRef HEEFT_PLAATS = new UriRef( ONTOLOGY + "#heeftPlaats" );

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  private RelatieConstants() {
    // Hide, to prevent instantiation.
  }
}
