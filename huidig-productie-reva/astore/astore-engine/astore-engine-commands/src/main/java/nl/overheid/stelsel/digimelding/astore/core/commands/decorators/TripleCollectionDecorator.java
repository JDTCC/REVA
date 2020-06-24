package nl.overheid.stelsel.digimelding.astore.core.commands.decorators;

import java.util.Collection;
import java.util.Iterator;

import org.apache.clerezza.rdf.core.NonLiteral;
import org.apache.clerezza.rdf.core.Resource;
import org.apache.clerezza.rdf.core.Triple;
import org.apache.clerezza.rdf.core.TripleCollection;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.event.FilterTriple;
import org.apache.clerezza.rdf.core.event.GraphListener;

/**
 * Decorator for Clerezza TripleCollection. This decorator add a proper toString() to the original
 * type.
 * 
 */
public class TripleCollectionDecorator implements TripleCollection, Decorator<TripleCollection> {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private final TripleCollection decorated;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public TripleCollectionDecorator(TripleCollection decorated) {
    this.decorated = decorated;
  }

  // -------------------------------------------------------------------------
  // Implementing Decorator
  // -------------------------------------------------------------------------

  @Override
  public TripleCollection decorate(TripleCollection decorated) {
    return new TripleCollectionDecorator(decorated);
  };

  @Override
  public TripleCollection getDecorated() {
    return decorated;
  }

  // -------------------------------------------------------------------------
  // Implementing Graph
  // -------------------------------------------------------------------------

  @Override
  public Iterator<Triple> filter(NonLiteral subject, UriRef predicate, Resource object) {
    return decorated.filter(subject, predicate, object);
  }

  @Override
  public void addGraphListener(GraphListener listener, FilterTriple filter, long delay) {
    decorated.addGraphListener(listener, filter, delay);
  }

  @Override
  public void addGraphListener(GraphListener listener, FilterTriple filter) {
    decorated.addGraphListener(listener, filter);
  }

  @Override
  public void removeGraphListener(GraphListener listener) {
    decorated.removeGraphListener(listener);
  }

  @Override
  public int size() {
    return decorated.size();
  }

  @Override
  public boolean isEmpty() {
    return decorated.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return decorated.contains(o);
  }

  @Override
  public Iterator<Triple> iterator() {
    return decorated.iterator();
  }

  @Override
  public Object[] toArray() {
    return decorated.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return decorated.toArray(a);
  }

  @Override
  public boolean add(Triple e) {
    return decorated.add(e);
  }

  @Override
  public boolean remove(Object o) {
    return decorated.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return decorated.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends Triple> c) {
    return decorated.addAll(c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return decorated.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return decorated.retainAll(c);
  }

  @Override
  public void clear() {
    decorated.clear();
  }

  // -------------------------------------------------------------------------
  // Object overrides
  // -------------------------------------------------------------------------

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    for (Triple triple : this) {
      builder.append(triple.getSubject()).append(",");
      builder.append(triple.getPredicate()).append(",");
      builder.append(triple.getObject()).append("\n");
    }

    String response = builder.toString();
    if (response.isEmpty()) {
      response = "(empty)";
    }
    return response;
  }

}
