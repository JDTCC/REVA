package nl.overheid.stelsel.digimelding.astore.processor;

import java.util.UUID;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationContext;

/**
 * Plugin interface for Annotation Processors.
 * 
 */
public interface AnnotationProcessor {

  /**
   * Pre-processes the given annotation.
   * 
   * @param context the context
   * @param annotation the annotation to validate
   */
  void process(AnnotationContext context, Annotation annotation);

  /**
   * Pre-processes the removal of the annotation with the given id.
   * 
   * @param context the context
   * @param uuid the id of the 
   */
  void processRemoval(AnnotationContext context, UUID uuid);
}
