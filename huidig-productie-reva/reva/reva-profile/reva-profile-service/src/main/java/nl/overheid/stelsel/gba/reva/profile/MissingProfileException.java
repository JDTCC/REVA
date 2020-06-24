package nl.overheid.stelsel.gba.reva.profile;

/**
 * Deze exceptie treed op wanneer er geen profile voor een gebruiker bestaat en
 * deze toch wordt opgevraagd.
 * 
 */
public class MissingProfileException extends RuntimeException {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = -7158029401885105342L;

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
  public MissingProfileException( String username ) {
    super( "Geen profiel voor gebruiker '" + username + "'" );
  }
}
