package nl.overheid.stelsel.digimelding.astore;

public abstract class AnnotationStoreException extends RuntimeException {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = -4292879048182434278L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public AnnotationStoreException(String message) {
    super(message);
  }

  public AnnotationStoreException(String message, Throwable cause) {
    super(message, cause);
  }
}
