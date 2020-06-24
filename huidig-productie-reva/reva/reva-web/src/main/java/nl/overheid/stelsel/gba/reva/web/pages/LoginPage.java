package nl.overheid.stelsel.gba.reva.web.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.ops4j.pax.wicket.api.PaxWicketMountPoint;

import nl.overheid.stelsel.gba.reva.web.security.RevaLoginPanel;

/**
 * Login pagina
 * 
 */
@PaxWicketMountPoint( mountPoint = "/login" )
public class LoginPage extends RevaPage {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public LoginPage() {
    super();

    initialize();
  }

  public LoginPage( PageParameters pageParameters ) {
    super( pageParameters );

    initialize();
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private void initialize() {
    add( new RevaLoginPanel( "loginPanel", true ) );

    // Hide login button (on login page)
    get( "login" ).setVisible( false );
  }
}
