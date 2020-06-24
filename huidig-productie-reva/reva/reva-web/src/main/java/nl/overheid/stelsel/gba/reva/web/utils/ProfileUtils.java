package nl.overheid.stelsel.gba.reva.web.utils;

import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.overheid.stelsel.gba.reva.web.locators.ProfileServiceLocator;

/**
 * Zoekt het profiel / gemeentecode bij de ingelogde gebruiker.
 * 
 */
public final class ProfileUtils {

  private static final Logger LOG = LoggerFactory.getLogger( ProfileUtils.class );

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  private ProfileUtils() {
    // Private constructor.
  }

  /**
   * Geeft de gemeentecode van de ingelogde gebruiker terug. Eerst wordt er
   * gezocht in de lijst van principals die de gebruiker bij het inloggen heeft
   * gekregen. Als dat niks oplevert wordt de
   * {@link nl.overheid.stelsel.gba.reva.web.locators.ProfileServiceLocator}
   * geraadpleegd.
   * 
   * @param subject
   *          Het {@link org.apache.shiro.subject.Subject} van de ingelogde
   *          gebruiker.
   * @return De gemeentecode waar de gebruiker toe behoort, of <code>null</code>
   *         als voor de gebruiker geen gemeentecode kan worden gevonden.
   */
  public static String getGemeentecode( Subject subject ) {
    String gemeentecode = null;

    for( String principal : subject.getPrincipals().byType( String.class ) ) {
      if( principal.startsWith( "gc=" ) ) {
        gemeentecode = principal.substring( principal.indexOf( '=' ) + 1 );
      }
    }

    if( gemeentecode == null ) {
      String gebruiker = subject.getPrincipal().toString();
      gemeentecode = ProfileServiceLocator.getService().getProfile( gebruiker ).getGemeenteCode();
    }

    if( LOG.isDebugEnabled() ) {
      LOG.debug( "Gevonden gemeentecode: {}", gemeentecode );
    }

    return gemeentecode;
  }

}
