package nl.overheid.stelsel.gba.reva.web.pages.error;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.http.WebResponse;

import nl.overheid.stelsel.gba.reva.web.pages.RevaPage;

/**
 * Eenvoudige reva fout pagina.
 * 
 */
public class ErrorPage extends RevaPage {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public ErrorPage() {
    add( new FeedbackPanel( "feedback" ) );
  }

  // -------------------------------------------------------------------------
  // Overrides
  // -------------------------------------------------------------------------

  @Override
  protected void setHeaders( final WebResponse response ) {
    response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
  }
}
