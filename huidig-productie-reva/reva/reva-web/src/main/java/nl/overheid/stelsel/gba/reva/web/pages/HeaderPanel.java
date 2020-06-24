package nl.overheid.stelsel.gba.reva.web.pages;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * Header panel voor de header boven vrijwel elke web pagina.
 * 
 */
public class HeaderPanel extends Panel {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 9096139498393850534L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public HeaderPanel( String id ) {
    super( id );

    add( new Image( "overheidLogo", new PackageResourceReference( this.getClass(), "logo_overheid.png" ) ) );
  }

}