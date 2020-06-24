package nl.overheid.stelsel.digimelding.astore;

import java.util.HashMap;
import java.util.Map;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.UriRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class AnnotationContext {

  // ------------------------------------------------------------------------
  // Class attributes
  // ------------------------------------------------------------------------

  private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationContext.class);

  // ------------------------------------------------------------------------
  // Object attributes
  // ------------------------------------------------------------------------

  private UriRef ref = new UriRef("default");
  private Map<String, Object> bag = new HashMap<String, Object>();
  private MGraph tree = null;

  // ------------------------------------------------------------------------
  // Getters / Setters
  // ------------------------------------------------------------------------

  public UriRef getRef() {
    return ref;
  }

  public void setRef(UriRef ref) {
    this.ref = ref;
  }

  /**
   * Gives the context value for the given key or null if the key is not known.
   * 
   * @param key the key for which to return the value.
   * @return the value or null if the key is not known.
   */
  public Object getValue(String key) {
    return bag.get(key);
  }

  /**
   * Gives the context value for the given key or null if the key is not known.
   * 
   * @param <T> the generic type
   * @param key the key for which to return the value.
   * @param expectedType the expected type of the return value.
   * @return the value or null if the key is not known.
   */
  public <T> T getValue(String key, Class<T> expectedType) {
    T result = null;

    Object object = bag.get(key);
    if (object == null || expectedType.isInstance(object)) {
      // Perform a cast only if the object is of the expected type.
      result = expectedType.cast(object);
    } else {
      LOGGER.error(String.format("Key '%s' not of expected type '%s', type was '%s'!", key,
          expectedType.getSimpleName(), object.getClass().getSimpleName()));
    }
    return result;
  }

  /**
   * Store the given value in the context under the given name.
   * 
   * @param key the key under which to store the value.
   * @param value the value to store
   */
  public void setValue(String key, Object value) {
    bag.put(key, value);
  }

  /**
   * Gives the full annotation tree as a graph.
   * 
   * @return Graph object containing the annotation tree of null if no tree exists yet.
   */
  public MGraph getTree() {
    return tree;
  }

  /**
   * Store the given annotation tree graph in the context.
   * 
   * @param tree the annotation tree as a graph
   */
  public void setTree(MGraph tree) {
    this.tree = tree;
  }
}
