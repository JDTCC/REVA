package nl.overheid.stelsel.gba.reva.web.pages;

import org.ops4j.pax.wicket.api.PaxWicketMountPoint;

/**
 * Eenvoudige home page van Reva. Deze pagina toont wat algemene informatie.
 * 
 */
@PaxWicketMountPoint( mountPoint = "/home" )
public class HomePage extends RevaPage {

  // -------------------------------------------------------------------------
  // Class Attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public HomePage() {
  }
}
