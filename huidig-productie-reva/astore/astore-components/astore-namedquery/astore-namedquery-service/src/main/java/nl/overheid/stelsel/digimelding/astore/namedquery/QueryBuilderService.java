package nl.overheid.stelsel.digimelding.astore.namedquery;

import java.util.Map;

/**
 * Utility interface that helps to build a query from a template and a table of parameters. This
 * builder will replace parameters in the template with their corresponding values in the parameters
 * map.
 * 
 */
public interface QueryBuilderService {

  /**
   * Builds a query from the template and the given name/value map.
   * 
   * @param queryTemplate the template query
   * @param parameters the name/value map for substitution
   * @return String with substituted parameters.
   */
  String build(final String queryTemplate, final Map<String, String> parameters);
}
