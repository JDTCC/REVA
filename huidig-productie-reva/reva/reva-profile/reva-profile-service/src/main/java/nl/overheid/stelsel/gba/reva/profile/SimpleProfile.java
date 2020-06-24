package nl.overheid.stelsel.gba.reva.profile;

/**
 * Eenvoudige generieke implementatie van een gebruikersprofiel object.
 * 
 */
public class SimpleProfile implements Profile {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private String username;
  private String gemeenteCode;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Maakt een nieuwe profile object aan voor de opgegeven gebruiker.
   * 
   * @param username
   *          de gebruikersnaam.
   */
  public SimpleProfile( String username ) {
    if( username == null ) {
      throw new IllegalArgumentException( "Username can not be null" );
    }
    this.username = username;
  }

  // -------------------------------------------------------------------------
  // Implementing Profile
  // -------------------------------------------------------------------------

  /** {@inheritDoc} */
  @Override
  public String getUsername() {
    return username;
  }

  /** {@inheritDoc} */
  @Override
  public String getGemeenteCode() {
    return gemeenteCode;
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  /**
   * Maakt het mogelijk om de gemeentecode te zetten.
   * 
   * @param gemeenteCode
   *          de te gebruiken gemeentecode.
   */
  public void setGemeenteCode( String gemeenteCode ) {
    this.gemeenteCode = gemeenteCode;
  }
}
