package nl.overheid.stelsel.digimelding.astore.namedquery;

/**
 * The exception gets thrown when building a query from the named query template fails.
 * 
 */
public class QueryBuilderException extends RuntimeException {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 3089255381494517521L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Creates a new expection with the given message and cause.
   * 
   * @param message the message
   * @param cause the cause
   */
  public QueryBuilderException(String message, Throwable cause) {
    super(message, cause);
  }
}
