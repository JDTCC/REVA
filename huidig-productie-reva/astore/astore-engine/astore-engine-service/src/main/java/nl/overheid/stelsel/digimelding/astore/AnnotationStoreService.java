package nl.overheid.stelsel.digimelding.astore;

import java.util.Map;
import java.util.UUID;

public interface AnnotationStoreService {
  /**
   * Retrieves the annotation tree by it unique root ID.
   * 
   * @param id of the annotation tree to retrieve.
   * @return The AnnotationTree if present null otherwise.
   * @throws Exception the exception thrown
   */
  AnnotationTree get(UUID id) throws Exception;

  AnnotationTree get(AnnotationContext context, UUID id) throws Exception;

  /**
   * Stores the given annotation.
   * 
   * @param annotation the annotation to be stored.
   * @throws Exception the exception thrown
   */
  void put(Annotation annotation) throws Exception;

  void put(AnnotationContext context, Annotation annotation) throws Exception;

  /**
   * 
   * 
   * @param query
   * @return The results of the query.
   * @throws Exception the exception thrown
   */
  Object query(String query) throws Exception;

  Object query(AnnotationContext context, String query) throws Exception;

  /**
   * 
   * @param name
   * @param parameters
   * @return The results of the query
   * @throws Exception the exception thrown
   */
  Object namedQuery(String name, Map<String, String> parameters) throws Exception;

  Object namedQuery(AnnotationContext context, String name, Map<String, String> parameters)
      throws Exception;

  /**
   * Removes the annotation tree with the specified id from the annotation store.
   * 
   * @param id of the annotation tree to remove
   * @throws Exception the exception thrown
   */
  void remove(UUID id) throws Exception;

  void remove(AnnotationContext context, UUID id) throws Exception;

  /**
   * Removes all annotation trees from the annotation store.
   * 
   * REMARK: Be careful since this method drops all existing annotations.
   * 
   * @throws Exception the exception thrown
   */
  void remove() throws Exception;

  void remove(AnnotationContext context) throws Exception;
}
