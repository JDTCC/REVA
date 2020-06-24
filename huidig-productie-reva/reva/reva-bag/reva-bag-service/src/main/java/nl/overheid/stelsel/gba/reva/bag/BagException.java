package nl.overheid.stelsel.gba.reva.bag;

/**
 * The exception gets thrown when there are other exceptions during bag
 * processing.
 * 
 */
public class BagException extends RuntimeException {

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
   * @param message
   *          the message
   * @param cause
   *          the cause
   */
  public BagException( String message, Throwable cause ) {
    super( message, cause );
  }
}
