package nl.overheid.stelsel.digimelding.astore.storage;

import java.util.Map;
import java.util.UUID;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationContext;
import nl.overheid.stelsel.digimelding.astore.AnnotationTree;

import org.apache.clerezza.rdf.core.UriRef;

public interface StorageProvider {

  /**
   * Store an annotation in the annotation store.
   * 
   * @param context the context.
   * @param annotation the annotation to store.
   */
  void store(AnnotationContext context, Annotation annotation);

  /**
   * Retrieves previously store annotations by specifying its stam (graph name).
   * 
   * @param context the context
   * @param stam the annotation stam (graph name) to retrieve.
   * @return AnnotationTree
   */
  AnnotationTree retrieve(AnnotationContext context, UUID stam);

  /**
   * Queries the annotation store. Depending on de kind of query the results may differ (Ask -&gt;
   * boolean, Select -&gt; ResultSet, Construct -&gt; Graph)
   * 
   * @param context the context
   * @param query the sparqle query to perform.
   * @return the resulting ResultSet, Graph or Boolean value
   */
  Object query(AnnotationContext context, String query);

  /**
   * Removes the annotation tree with the given reference from the annotation store.
   * 
   * @param uriRef of the annotation tree to remove.
   * @param context the context.
   */
  void remove(AnnotationContext context, UriRef uriRef);

  /**
   * Removes all annotation trees from the annotation store.
   * 
   * @param context the context.
   */
  void remove(AnnotationContext context);

  /**
   * Gives a list of possible query parameter known by the storage provider.
   * 
   * @return Map containing key/value entries for known paramters.
   */
  Map<String, String> getQueryParameters();
}
