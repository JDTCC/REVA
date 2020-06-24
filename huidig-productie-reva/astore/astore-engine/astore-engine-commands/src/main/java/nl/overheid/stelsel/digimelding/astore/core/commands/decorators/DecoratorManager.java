package nl.overheid.stelsel.digimelding.astore.core.commands.decorators;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nl.overheid.stelsel.digimelding.astore.AnnotationTree;

import org.apache.clerezza.rdf.core.TripleCollection;
import org.apache.clerezza.rdf.core.sparql.ResultSet;

public final class DecoratorManager {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Map<Class<?>, Decorator<?>> decorators = new HashMap<Class<?>, Decorator<?>>();

  static {
    decorators.put(AnnotationTree.class, new AnnotationTreeDecorator(null));
    decorators.put(TripleCollection.class, new TripleCollectionDecorator(null));
    decorators.put(ResultSet.class, new ResultSetDecorator(null));
  }

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  private DecoratorManager() {
    // prevent instantiation.
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  public static <T> T decorate(T decorated) {
    T response = decorated;

    Decorator<T> decorator = findDecorator(decorated);
    if (decorator != null) {
      response = decorator.decorate(decorated);
    }

    return response;
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  @SuppressWarnings("unchecked")
  public static <T> Decorator<T> findDecorator(T decorated) {
    for (Entry<Class<?>, Decorator<?>> decoratorEntry : decorators.entrySet()) {
      if (decoratorEntry.getKey().isInstance(decorated)) {
        return (Decorator<T>) decoratorEntry.getValue();
      }
    }
    return null;
  }
}
