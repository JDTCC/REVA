package nl.overheid.stelsel.digimelding.astore.authorisation;

public class AuthorisationException extends RuntimeException {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = -4675291866233364373L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public AuthorisationException(String message) {
    super(message);
  }

  public AuthorisationException(String message, Throwable cause) {
    super(message, cause);
  }
}
