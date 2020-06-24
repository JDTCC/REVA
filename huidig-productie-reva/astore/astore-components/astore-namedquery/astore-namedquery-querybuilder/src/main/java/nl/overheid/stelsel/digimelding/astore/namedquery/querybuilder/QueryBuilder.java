package nl.overheid.stelsel.digimelding.astore.namedquery.querybuilder;

import java.io.StringReader;
import java.util.Map;

import nl.overheid.stelsel.digimelding.astore.namedquery.QueryBuilderException;
import nl.overheid.stelsel.digimelding.astore.namedquery.QueryBuilderService;
import nl.xup.template.Bindings;
import nl.xup.template.SimpleBindings;
import nl.xup.template.Template;
import nl.xup.template.TemplateEngineService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class that helps to build a query from a template and a table of parameters. This builder
 * will replace parameters in the template with their corresponding values in the parameters map.
 * 
 * XupTemplate is used for doing the hard templating work.
 * 
 */
public final class QueryBuilder implements QueryBuilderService {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(QueryBuilder.class);

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private TemplateEngineService templateEngine;

  // -------------------------------------------------------------------------
  // Constructor
  // -------------------------------------------------------------------------

  public QueryBuilder() {
    // Prevent instantiation.
  }

  // -------------------------------------------------------------------------
  // Implementing QueryBuilderService
  // -------------------------------------------------------------------------

  /**
   * Builds a query from the template and the given name/value map.
   * 
   * @param queryTemplate the template query
   * @param parameters the name/value map for substitution
   * @return String with substituted parameters.
   */
  public String build(final String queryTemplate, final Map<String, String> parameters) {
    String response = queryTemplate;
    if (queryTemplate != null && parameters != null) {
      logger.debug("Building query: \n{}", queryTemplate);
      // Prepare bindings
      Bindings bindings = new SimpleBindings();
      for (Map.Entry<String, String> entry : parameters.entrySet()) {
        Object value = entry.getValue();
        if (value instanceof String) {
          value = escapeValue(String.class.cast(value));
        }
        logger.debug("Query parameter: {} = '{}' escaped: '{}'", entry.getKey(), entry.getValue(),
            value);
        bindings.put(entry.getKey(), value);
      }
      response = renderTemplate(queryTemplate, bindings);
      logger.debug("Resulting query: \n{}", response);
    }
    return response;
  }

  // -------------------------------------------------------------------------
  // Getter / Setter methods
  // -------------------------------------------------------------------------

  public TemplateEngineService getTemplateEngine() {
    return templateEngine;
  }

  public void setTemplateEngine(TemplateEngineService templateEngine) {
    this.templateEngine = templateEngine;
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  /**
   * Renders the given template and returns the rendered string.
   * 
   * @param template the template to render
   * @param bindings The specified Bindings
   * @return String with the rendering results.
   */
  private String renderTemplate(String templateString, Bindings bindings) {
    // construct response from template.
    try {
      Template template = getTemplateEngine().getTemplate(new StringReader(templateString));
      return template.execute(bindings);
    } catch (Exception e) {
      throw new QueryBuilderException(e.getMessage(), e);
    }
  }

  /**
   * Escapes the given string according the sparql escape rules in
   * http://www.w3.org/TR/2013/REC-sparql11-query-20130321/#grammarEscapes
   * 
   * @param newValue to be escaped
   * @return the escaped string
   */
  private String escapeValue(String value) {
    String newValue = value;
    newValue = newValue.replace("\\", "\\\\");
    newValue = newValue.replace("\t", "\\\t");
    newValue = newValue.replace("\n", "\\\n");
    newValue = newValue.replace("\r", "\\\r");
    newValue = newValue.replace("\b", "\\\b");
    newValue = newValue.replace("\f", "\\\f");
    newValue = newValue.replace("\"", "\\\"");
    newValue = newValue.replace("'", "\\'");
    return newValue;
  }
}
