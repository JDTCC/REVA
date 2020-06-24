package nl.overheid.stelsel.gba.reva.profile;

/**
 * Dit interface beschrijft een gebruikersprofiel.
 * 
 */
public interface Profile {

  /**
   * Geeft de naam van de gebruiker.
   * 
   * @return String met de naam van de gebruiker.
   */
  String getUsername();

  /**
   * Geeft de gemeentecode waartoe de gebruiker behoort.
   * 
   * @return String met de gemeentecode
   */
  String getGemeenteCode();
}
