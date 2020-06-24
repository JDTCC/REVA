package nl.overheid.stelsel.gba.reva.profile.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import nl.overheid.stelsel.gba.reva.profile.MissingProfileException;
import nl.overheid.stelsel.gba.reva.profile.Profile;
import nl.overheid.stelsel.gba.reva.profile.ProfileService;
import nl.overheid.stelsel.gba.reva.profile.SimpleProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Properties gebaseerde implementatie van een ProfileService.
 * 
 */
public class PropertyProfileService implements ProfileService {

  // -------------------------------------------------------------------------
  // Class atrributes
  // -------------------------------------------------------------------------

  private static final Logger LOGGER = LoggerFactory.getLogger( PropertyProfileService.class );

  // -------------------------------------------------------------------------
  // Object atrributes
  // -------------------------------------------------------------------------

  private String propertiesFile;
  private Properties profileProperties = new Properties();

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public PropertyProfileService( String propertiesFile ) {
    this.propertiesFile = propertiesFile;
    try (InputStream stream = new FileInputStream( propertiesFile )) {
      // load properties file with profiles
      profileProperties.load( stream );
    } catch( IOException ex ) {
      LOGGER.error( "Check profiles file '{}'", propertiesFile );
      throw new IllegalArgumentException( "Could not load profiles from: " + propertiesFile, ex );
    }
  }

  // -------------------------------------------------------------------------
  // Implementing ProfileService
  // -------------------------------------------------------------------------

  /** {@inheritDoc} */
  @Override
  public Profile getProfile( String username ) {
    if( !profileProperties.containsKey( username ) ) {
      LOGGER.error( "No profile for '{}' in profiles file '{}'", username, propertiesFile );
      throw new MissingProfileException( username );
    }
    SimpleProfile profile = new SimpleProfile( username );
    profile.setGemeenteCode( profileProperties.getProperty( username ) );
    return profile;
  };

}
