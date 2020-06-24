package nl.overheid.stelsel.gba.reva.web.security;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.http.WebResponse;

import nl.overheid.stelsel.gba.reva.web.pages.LoginPage;
import nl.overheid.stelsel.gba.reva.web.pages.RevaPage;

/**
 * Eenvoudige pagina voor geen toegang berichten.
 * 
 */
public class AccessDeniedPage extends RevaPage {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public AccessDeniedPage() {
    add( new FeedbackPanel( "feedback" ) );
    add( new BookmarkablePageLink<Void>( "loginLink", LoginPage.class ) );
  }

  // -------------------------------------------------------------------------
  // Overrides
  // -------------------------------------------------------------------------

  @Override
  protected void setHeaders( final WebResponse response ) {
    response.setStatus( HttpServletResponse.SC_FORBIDDEN );
  }

  @Override
  public boolean isErrorPage() {
    return true;
  }

  @Override
  public boolean isVersioned() {
    return false;
  }
}
