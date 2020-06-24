package nl.overheid.stelsel.gba.reva.web.pages.rapportage;

import org.apache.wicket.RestartResponseException;
import org.wicketstuff.shiro.ShiroConstraint;
import org.wicketstuff.shiro.annotation.ShiroSecurityConstraint;

import nl.overheid.stelsel.gba.reva.web.pages.RevaPage;

/**
 * Eenvoudige rapportage page van Reva. Deze pagina toont wat algemene
 * rapportage informatie.
 * 
 */
@ShiroSecurityConstraint( constraint = ShiroConstraint.HasPermission, value = "rapportage:opvragen" )
public class RapportagePage extends RevaPage {

  // -------------------------------------------------------------------------
  // Class Attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public RapportagePage() {
    throw new RestartResponseException( DoorGemeenteRapportagePage.class );
  }
}
