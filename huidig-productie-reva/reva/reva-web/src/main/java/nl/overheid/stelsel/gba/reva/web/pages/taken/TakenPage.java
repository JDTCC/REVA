package nl.overheid.stelsel.gba.reva.web.pages.taken;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;

import nl.overheid.stelsel.gba.reva.web.pages.RevaPage;

/**
 * Reva pagina voor het beheren van taken. Taken zijn opdrachten die 's nachts
 * worden uitgevoerd en de volden ochtend beschikbaar zijn als CSV download. De
 * gebruiker moet taken (met bijgehorende download) zelf verwijderen.
 * 
 */
@SuppressWarnings( "serial" )
public class TakenPage extends RevaPage {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = -8057521565297747741L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public TakenPage() {
    Button nieuwButton = new Button( "nieuw" ) {
      @Override
      public void onSubmit() {
        setResponsePage( NieuweTaakPage.class );
        super.onSubmit();
      }
    };

    Form<Object> form = new Form<Object>( "buttonBar" );
    form.add( nieuwButton );

    // Add the form to the page
    add( form );
  }
}
