package nl.overheid.stelsel.digimelding.astore.namedquery;

import java.util.Map;

/**
 * A service that maps names to queries.
 * 
 */
public interface NamedQueryService extends Map<String, String> {

  /**
   * Returns the query to which the specified name is mapped, or {@code null} if no mapping exists
   * for the specified name.
   * 
   * @param name the name whose associated query is to be returned
   * @return the named query to which the specified name is mapped, or {@code null} if no mapping
   *         for the name.
   * @throws NullPointerException if the specified name is null
   */
  String getNamedQuery(String name);

  /**
   * Associates the specified query with the specified name. If a previous mapping exists for the
   * name, the old query is replaced by the specified new query.
   *
   * @param name name with which the specified query is to be associated
   * @param query named query to be associated with the specified name
   * @return the previous query associated with <tt>name</tt>, or <tt>null</tt> if there was no
   *         mapping for <tt>name</tt>.
   * @throws NullPointerException if the specified name or query is null
   */
  String putNamedQuery(String name, String query);

  /**
   * Returns a query constructed from the named query mapped to the specified name or {@code null}
   * if no mapping exists for the specified name. In the constructed query each placeholder in the
   * named query will be replaced with matching values from the specified parameters.
   * 
   * @param name the name whose associated query is to be used
   * @param parameters the parameters to used for constructing the query
   * @return the named query to which the specified name is mapped, or {@code null} if no mapping
   *         for the name.
   * @throws NullPointerException if the specified name is null
   * @throws QueryBuilderException if constructing the quere failed.
   */
  String getQuery(String name, Map<String, String> parameters);
}
