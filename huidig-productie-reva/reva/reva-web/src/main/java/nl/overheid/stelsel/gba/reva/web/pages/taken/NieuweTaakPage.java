package nl.overheid.stelsel.gba.reva.web.pages.taken;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

import nl.overheid.stelsel.gba.reva.web.pages.RevaPage;
import nl.overheid.stelsel.gba.reva.web.pages.zoeken.ZoekCriteria;

/**
 * Reva pagina voor het beheren van taken. Taken zijn opdrachten die 's nachts
 * worden uitgevoerd en de volden ochtend beschikbaar zijn als CSV download. De
 * gebruiker moet taken (met bijgehorende download) zelf verwijderen.
 * 
 */
public class NieuweTaakPage extends RevaPage {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = -8057521565297747741L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  @SuppressWarnings( "serial" )
  public NieuweTaakPage() {
    Button annulerenButton = new Button( "annuleren" ) {
      @Override
      public void onSubmit() {
        setResponsePage( TakenPage.class );
        super.onSubmit();
      }
    };
    Button bewarenButton = new Button( "bewaren" ) {
      @Override
      public void onSubmit() {
        // TODO: bewaren
        setResponsePage( TakenPage.class );
        super.onSubmit();
      }
    };

    TaakCriteria taakCriteria = new TaakCriteria();

    Form<ZoekCriteria> form = new Form<ZoekCriteria>( "nieuweTaakFormulier" );
    form.setDefaultModel( new CompoundPropertyModel<TaakCriteria>( taakCriteria ) );

    form.add( new TextField<String>( "naam" ).setRequired( false ) );
    form.add( new DateTextField( "vanafDatum" ).setRequired( false ) );
    form.add( new DateTextField( "totenmetDatum" ).setRequired( false ) );
    form.add( new TextField<String>( "postcode" ).setRequired( false ) );
    form.add( new TextField<String>( "huisnummer" ).setRequired( false ) );

    form.add( annulerenButton );
    form.add( bewarenButton );

    // Add the form to the page
    add( form );
  }
}
