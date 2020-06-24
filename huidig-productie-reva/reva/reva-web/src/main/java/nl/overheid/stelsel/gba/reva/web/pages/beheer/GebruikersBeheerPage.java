package nl.overheid.stelsel.gba.reva.web.pages.beheer;

import org.wicketstuff.shiro.ShiroConstraint;
import org.wicketstuff.shiro.annotation.ShiroSecurityConstraint;

import nl.overheid.stelsel.gba.reva.web.pages.RevaPage;

/**
 * Eenvoudige help page van Reva. Deze pagina toont wat algemene informatie.
 * 
 */
@ShiroSecurityConstraint( constraint = ShiroConstraint.HasRole, value = "beheerder" )
public class GebruikersBeheerPage extends RevaPage {

  // -------------------------------------------------------------------------
  // Class Attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public GebruikersBeheerPage() {
  }
}
