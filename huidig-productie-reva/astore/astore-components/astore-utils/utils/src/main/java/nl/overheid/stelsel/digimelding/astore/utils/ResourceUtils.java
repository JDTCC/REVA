package nl.overheid.stelsel.digimelding.astore.utils;

import org.apache.clerezza.rdf.core.BNode;
import org.apache.clerezza.rdf.core.PlainLiteral;
import org.apache.clerezza.rdf.core.Resource;
import org.apache.clerezza.rdf.core.TypedLiteral;
import org.apache.clerezza.rdf.core.UriRef;

/**
 * Utility class for Clereza Resource handling.
 * 
 */
public final class ResourceUtils {

  // -------------------------------------------------------------------------
  // Constructor
  // -------------------------------------------------------------------------

  /**
   * Hide utility class constructor
   */
  private ResourceUtils() {
    // prevent instantiation
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  /**
   * TODO: Verplaatsen naar utils component of onderdeel maken van Clerezza.
   * Helper to get the proper string representation for the given Resource.
   */
  public static String getResourceValue(Resource resource) {
    StringBuilder value = new StringBuilder();
    if (resource instanceof UriRef) {
      value.append(((UriRef) resource).getUnicodeString());
    } else if (resource instanceof TypedLiteral) {
      value.append(((TypedLiteral) resource).getLexicalForm());
    } else if (resource instanceof PlainLiteral) {
      value.append(((PlainLiteral) resource).getLexicalForm());
    } else if (resource instanceof BNode) {
      value.append("/");
    } else if (resource != null) {
      value.append(resource.toString());
    }
    return value.toString();
  }
}
