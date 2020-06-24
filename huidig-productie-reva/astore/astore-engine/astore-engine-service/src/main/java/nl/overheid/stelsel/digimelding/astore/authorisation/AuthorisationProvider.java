package nl.overheid.stelsel.digimelding.astore.authorisation;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationContext;

/**
 * Extension interface for Authorizers.
 * 
 */
public interface AuthorisationProvider {

  /**
   * Called by the annotation store on an attempt to store the given annotation. Implementers can
   * use this event to determine if the issuer is authorized to store the annotation.
   * 
   * @param ctx the context.
   * @param annotation the annotation to be stored.
   * @return {@code True} if authorized, {@code False} otherwise
   */
  boolean isStoreAuthorised(AnnotationContext ctx, Annotation annotation);

  /**
   * Called by the annotation store on an attempt to query the annotation store. Implementers can
   * use this event to determine if the issuer is authorized to query the annotation store.
   * 
   * @param ctx the context
   * @param query the query or named query to issued
   * @return {@code True} if authorized, {@code False} otherwise
   */
  boolean isRetrieveAuthorised(AnnotationContext ctx, String query);

  /**
   * Called by the annotation store on an attempt to clean the annotation store. Implementers can
   * use this event to determine if the issuer is authorized to clean the annotation store.
   * 
   * @param ctx the context.
   * @return {@code True} if authorized, {@code False} otherwise
   */
  boolean isClearAuthorised(AnnotationContext ctx);
}
