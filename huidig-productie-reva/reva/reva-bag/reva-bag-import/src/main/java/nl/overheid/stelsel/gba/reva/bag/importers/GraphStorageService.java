package nl.overheid.stelsel.gba.reva.bag.importers;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.access.NoSuchEntityException;
import org.apache.clerezza.rdf.core.access.QueryableTcProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GraphStorageService {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger( GraphStorageService.class );

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  private GraphStorageService() {
    // prevent instantiation.
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  public static MGraph getGraph( BundleContext context, UriRef graphId ) {
    MGraph graph = null;
    QueryableTcProvider manager = getStorageProvider( context );
    try {
      graph = manager.getMGraph( graphId );
      logger.info( "Opened existing graph: ", graphId.getUnicodeString() );
    } catch( NoSuchEntityException e ) {
      logger.info( "Create new graph: ", graphId.getUnicodeString() );
      graph = manager.createMGraph( graphId );
    }

    return graph;
  }

  public static Object runQuery( BundleContext context, String query ) {
    QueryableTcProvider manager = getStorageProvider( context );
    return manager.executeSparqlQuery( query, null );
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private static QueryableTcProvider getStorageProvider( BundleContext context ) {
    ServiceReference<QueryableTcProvider> serviceRef = context.getServiceReference( QueryableTcProvider.class );
    QueryableTcProvider provider = context.getService( serviceRef );
    if( provider == null ) {
      throw new ServiceException( "No QueryableTcProvider found!" );
    }
    return provider;
  }

}
