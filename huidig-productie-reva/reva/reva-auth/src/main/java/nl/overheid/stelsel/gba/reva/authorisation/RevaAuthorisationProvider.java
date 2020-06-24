package nl.overheid.stelsel.gba.reva.authorisation;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationContext;
import nl.overheid.stelsel.digimelding.astore.authorisation.AuthorisationProvider;

/**
 * AStore Authorisatie provider voor Reva.
 * 
 */
public class RevaAuthorisationProvider implements AuthorisationProvider {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger( RevaAuthorisationProvider.class );

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private Boolean removeAllEnabled = false;

  // -------------------------------------------------------------------------
  // Public methods
  // -------------------------------------------------------------------------

  public void setRemoveAllEnabled( String removeAllEnabled ) {
    this.removeAllEnabled = Boolean.valueOf( removeAllEnabled );
  }

  // -------------------------------------------------------------------------
  // Implementing AuthorisationProvider
  // -------------------------------------------------------------------------

  @Override
  public boolean isStoreAuthorised( AnnotationContext ctx, Annotation annotation ) {
    // TODO: real authorisation
    return true;
  }

  @Override
  public boolean isRetrieveAuthorised( AnnotationContext ctx, String query ) {
    // Als we vanuit de karaf console werken is er geen Shiro security manager.
    // In dat geval
    // (vanuit de console) mogen we alles.
    try {
      SecurityUtils.getSecurityManager();
    } catch( UnavailableSecurityManagerException usmx ) {
      // Geen security manager dan werken we vanaf de console.
      return true;
    }

    // Gebruiker mag de query niet uitvoeren totdat we zeker weten dat het wel
    // mag.
    boolean magQueryUitvoeren = false;
    Subject subject = SecurityUtils.getSubject();

    // Mag de gebruiker deze query uitvoeren?
    magQueryUitvoeren |= subject.isPermitted( "query:" + query );

    return magQueryUitvoeren;
  }

  @Override
  public boolean isClearAuthorised( AnnotationContext ctx ) {
	// Controleren of een volledige opschoon actie is gevraagd en
	// of deze wel is toegestaan.
	if ( (new UriRef("*")).equals(ctx.getRef()) && !removeAllEnabled ) {
	  // Remove all is niet toegestaan.
	  logger.info( "Remove all is disabled!" );
	  return false;
	}
	  
    // Als we vanuit de karaf console werken is er geen Shiro security manager.
    // In dat geval (vanuit de console) mogen we alles.
    try {
      SecurityUtils.getSecurityManager();
    } catch( UnavailableSecurityManagerException usmx ) {
      // Geen security manager dan werken we vanaf de console.
      return true;
    }

    // Controleer de permissies of de gebruiker weg registraties mag
    // verwijderen.
    Subject subject = SecurityUtils.getSubject();
    return subject.isPermitted( "verwijderen:registratie" );
  }
}
