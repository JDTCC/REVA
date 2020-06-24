package nl.overheid.stelsel.digimelding.astore.namedquery;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.ServiceException;

public abstract class AbstractNamedQueryService extends HashMap<String, String> implements
    NamedQueryService {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = -1416169247700144627L;

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private QueryBuilderService queryBuilder;

  // -------------------------------------------------------------------------
  // Implementing NamedQueryService
  // -------------------------------------------------------------------------

  /** {@inheritDoc} */
  @Override
  public String getQuery(String name, Map<String, String> parameters) {
    String namedQuery = getNamedQuery(name);

    String query = null;
    if (namedQuery != null) {
      query = getQueryBuilder().build(namedQuery, parameters);
    }
    return query;
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  /**
   * Gets the query builder to be used.
   * 
   * @return
   */
  public QueryBuilderService getQueryBuilder() {
    if (queryBuilder == null) {
      throw new ServiceException("QueryBuilderService service not available");
    }
    return queryBuilder;
  }

  /**
   * Sets the query builder to be used.
   * 
   * @param queryBuilder
   */
  public void setQueryBuilder(QueryBuilderService queryBuilder) {
    this.queryBuilder = queryBuilder;
  }
}
