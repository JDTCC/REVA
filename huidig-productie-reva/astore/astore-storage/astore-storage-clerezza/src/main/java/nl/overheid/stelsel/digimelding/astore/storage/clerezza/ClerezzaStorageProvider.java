package nl.overheid.stelsel.digimelding.astore.storage.clerezza;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.Triple;
import org.apache.clerezza.rdf.core.TripleCollection;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.access.NoSuchEntityException;
import org.apache.clerezza.rdf.core.access.QueryableTcProvider;
import org.apache.clerezza.rdf.core.impl.SimpleMGraph;
import org.apache.clerezza.rdf.core.impl.TripleImpl;
import org.apache.clerezza.rdf.ontologies.RDF;
import org.osgi.framework.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationContext;
import nl.overheid.stelsel.digimelding.astore.AnnotationTree;
import nl.overheid.stelsel.digimelding.astore.storage.StorageProvider;

/**
 * This storage provider uses a Clerezza based storage backend. The actual backend used depends on
 * the packaging/assembly. According to clerezza, of all backend present in the packaging the one
 * with the highest weight will be used.
 * 
 * The following parameters are injected in the list of parameters for named queries:
 * 
 * - stamIndexName: the configured stam index name,
 * 
 */
public class ClerezzaStorageProvider implements StorageProvider {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final String DEFAULT_STAM_INDEX_NAME = "http://digimelding.nl/AStore/stamIndex";
  private static final String DEFAULT_STAM_COLUMN_NAME = "stamId";
  static final String QUERY_PARAM_STAM_INDEX = "stamIndexName";
  static final String QUERY_PARAM_STAM_COLUMN = "stamColumnName";
  static final String QUERY_PARAM_LIMIT = "limit";
  static final String QUERY_PARAM_OFFSET = "offset";

  private static final UriRef URI_STAM = new UriRef("http://digimelding.nl/AStore/Stam");

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(ClerezzaStorageProvider.class);

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private QueryableTcProvider clerezzaProvider;
  private String stamIndexName = DEFAULT_STAM_INDEX_NAME;
  private String stamColumnName = DEFAULT_STAM_COLUMN_NAME;
  private String defaultLimit = DEFAULT_STAM_COLUMN_NAME;
  private String defaultOffset = DEFAULT_STAM_COLUMN_NAME;

  // -------------------------------------------------------------------------
  // OSGi Blueprint lifecycle
  // -------------------------------------------------------------------------

  /**
   * Called on initialization by blueprint when properly configured in the blueprint configuration
   * (context.xml).
   */
  public void init() {
    logger.trace("ClerezzaStorageProvider:init");
  }

  /**
   * Called on destory by blueprint when properly configured in the blueprint configuration
   * (context.xml).
   */
  public void destroy() {
    logger.trace("ClerezzaStorageProvider:destroy");
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  /**
   * Returns the clerezza provider service being used.
   * 
   * @return The clerezza provider service
   * @throws ServiceException when no service provider present.
   */
  public QueryableTcProvider getClerezzaProvider() {
    if (clerezzaProvider == null) {
      throw new ServiceException("Clerezza provider service not available");
    }
    return clerezzaProvider;
  }

  /**
   * Sets the clerezza provider service to be used.
   * 
   * @param clerezzaProvider the service provider to use.
   */
  public void setClerezzaProvider(QueryableTcProvider clerezzaProvider) {
    this.clerezzaProvider = clerezzaProvider;
  }

  /**
   * Gives the index name for annotation trees.
   * 
   * @return String containing the index name.
   */
  public String getStamIndexName() {
    return stamIndexName;
  }

  /**
   * Set the index name to be used for all annotation tree stammen.
   * 
   * @param stamIndexName the index name to be used.
   */
  public void setStamIndexName(String stamIndexName) {
    this.stamIndexName = stamIndexName;
  }

  /**
   * Gives the column name to be used for annotation tree ids in resultsets.
   * 
   * @return String containing the resultset column name.
   */
  public String getStamColumnName() {
    return stamColumnName;
  }

  /**
   * Set the column name to be used for all annotation tree ids in resultsets.
   * 
   * @param stamColumnName the column name to be used in resultsets.
   */
  public void setStamColumnName(String stamColumnName) {
    this.stamColumnName = stamColumnName;
  }

  /**
   * Gives the default limit value to be used in queries. The executor of queries can override this
   * default value.
   * 
   * @return String containing the default limit value to be used in queries.
   */
  public String getDefaultLimit() {
    return defaultLimit;
  }

  /**
   * Set the default limit value to be used in queries. The query executor can always override this
   * value if needed.
   * 
   * @param defaultLimit the default limit to be used in queries.
   */
  public void setDefaultLimit(String defaultLimit) {
    this.defaultLimit = defaultLimit;
  }

  /**
   * Gives the default offset value to be used in queries. The executor of queries can override this
   * default value.
   * 
   * @return String containing the default offset value to be used in queries.
   */
  public String getDefaultOffset() {
    return defaultOffset;
  }

  /**
   * Set the default offset value to be used in queries. The query executor can always override this
   * value if needed.
   * 
   * @param defaultOffset the default offset to be used in queries.
   */
  public void setDefaultOffset(String defaultOffset) {
    this.defaultOffset = defaultOffset;
  }

  // -------------------------------------------------------------------------
  // Implementing StorageProvider
  // -------------------------------------------------------------------------

  @Override
  public void store(AnnotationContext context, Annotation annotation) {
    logger.trace("ClerezzaStorageProvider:store");

    MGraph graph = getMGraph(context.getRef());
    if (graph == null) {
      graph = clerezzaProvider.createMGraph(context.getRef());
      addToIndex(context.getRef());
    }

    graph.addAll(annotation.getGraph());
  }

  @Override
  public AnnotationTree retrieve(AnnotationContext context, UUID stam) {
    MGraph graph = getMGraph(context.getRef());

    AnnotationTree tree = null;
    if (graph != null) {
      // Create and fill tree.
      tree = new AnnotationTree();
      tree.setStam(stam);
      tree.setGraph(graph);
    }
    
    return tree;
  }

  @Override
  public Object query(AnnotationContext context, String query) {
    logger.trace("ClerezzaStorageProvider:query");

    return clerezzaProvider.executeSparqlQuery(query, null);
  }

  @Override
  public void remove(AnnotationContext context, UriRef uriRef) {
    logger.trace("ClerezzaStorageProvider:remove *");

    UriRef stam = context.getRef();
    clerezzaProvider.deleteTripleCollection(stam);
    removeFromIndex(context.getRef());
  }

  @Override
  public void remove(AnnotationContext context) {
    logger.trace("ClerezzaStorageProvider:remove *");

    TripleCollection stamIndex = new SimpleMGraph(getStamIndex());
    for (Triple triple : stamIndex) {
      if (URI_STAM.equals(triple.getObject())) {
        UriRef stam = UriRef.class.cast(triple.getSubject());
        clerezzaProvider.deleteTripleCollection(stam);
      }
    }

    getStamIndex().clear();
  }

  @Override
  public Map<String, String> getQueryParameters() {
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put(QUERY_PARAM_STAM_INDEX, getStamIndexName());
    parameters.put(QUERY_PARAM_STAM_COLUMN, getStamColumnName());
    parameters.put(QUERY_PARAM_LIMIT, getDefaultLimit());
    parameters.put(QUERY_PARAM_OFFSET, getDefaultOffset());
    return parameters;
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private MGraph getMGraph(UriRef uriRef) {
    try {
      return getClerezzaProvider().getMGraph(uriRef);
    } catch (NoSuchEntityException ex) {
      // Graph does not yet exists.
      return null;
    }
  }
  
  /**
   * Gives access to the actual stam index.
   * 
   * @return TripleCollection containing the stam index.
   */
  private TripleCollection getStamIndex() {
    TripleCollection index;

    index = getMGraph(new UriRef(getStamIndexName()));
    if ( index == null ) {
      index = getClerezzaProvider().createMGraph(new UriRef(getStamIndexName()));
    }

    return index;
  }

  /**
   * Add the given stam to the index.
   * 
   * @param stam to be added to the index.
   */
  private void addToIndex(UriRef stam) {
    TripleCollection index = getStamIndex();
    index.add(new TripleImpl(stam, RDF.type, URI_STAM));
  }

  /**
   * Removes the given stam from the index.
   * 
   * @param stam to be removed from the index.
   */
  private void removeFromIndex(UriRef stam) {
    TripleCollection index = getStamIndex();
    index.remove(new TripleImpl(stam, RDF.type, URI_STAM));
  }
}
