package nl.overheid.stelsel.gba.reva.profile;

/**
 * Dit is het interface van de Gebruikersprofiel service.
 * 
 */
public interface ProfileService {

  /**
   * Geeft het profiel voor de opgegeven gebruiker terug.
   * 
   * @param usernaam
   *          de naam van de gebruiker.
   * @return Het Profile object behorende bij de opgegeven gebruiker.
   * 
   * @throws MissingProfileException
   *           indien er geen profiel voor de opgegeven gebruiker voorhanden is.
   */
  Profile getProfile( String username );
}
