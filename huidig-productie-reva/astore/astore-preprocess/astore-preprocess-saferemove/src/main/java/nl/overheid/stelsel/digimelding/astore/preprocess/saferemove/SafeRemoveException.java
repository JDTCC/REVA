package nl.overheid.stelsel.digimelding.astore.preprocess.saferemove;

import nl.overheid.stelsel.digimelding.astore.AnnotationStoreException;

/**
 * Generic exception for safe remove component.
 * 
 */
public class SafeRemoveException extends AnnotationStoreException {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = -1423037066595524781L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------
  
  public SafeRemoveException(Throwable cause) {
    super(cause.getMessage(), cause);
  }
}
