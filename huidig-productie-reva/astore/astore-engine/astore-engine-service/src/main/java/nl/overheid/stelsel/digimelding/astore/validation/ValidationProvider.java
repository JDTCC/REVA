package nl.overheid.stelsel.digimelding.astore.validation;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationContext;

/**
 * 
 */
public interface ValidationProvider {

  /**
   * Validates the given annotation.
   * 
   * @param context the context
   * @param annotation the annotation to validate
   */
  void validate(AnnotationContext context, Annotation annotation);

}
