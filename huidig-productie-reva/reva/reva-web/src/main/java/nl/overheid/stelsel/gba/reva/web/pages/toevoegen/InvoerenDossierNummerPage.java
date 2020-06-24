package nl.overheid.stelsel.gba.reva.web.pages.toevoegen;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.shiro.ShiroConstraint;
import org.wicketstuff.shiro.annotation.ShiroSecurityConstraint;

import nl.overheid.stelsel.gba.reva.web.components.BsnTextField;
import nl.overheid.stelsel.gba.reva.web.components.CijfersTextField;
import nl.overheid.stelsel.gba.reva.web.pages.RevaPage;

/**
 * Pagina voor het invoeren van het BSN of RNI dossiernummer. Deze is benodigd
 * alvorens het eerste verblijf adres kan worden ingevoerd.
 * 
 */
@ShiroSecurityConstraint( constraint = ShiroConstraint.HasPermission, value = "opvoeren:eva" )
public class InvoerenDossierNummerPage extends RevaPage {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final String ID_FORM = "dossierNummerFormulier";
  private static final String ID_BSN_BEKEND = "bsnBekend";
  private static final String ID_DOSSIERNUMMER = "dossierNummer";
  private static final String ID_BSN = "bsn";

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 6739723022550717512L;

  private String dossiernummerWaarde = "";
  private String bsnWaarde = "";
  private String isBsnBekend = "";

  private TextField<String> bsn = null;
  private Button doorgaanButton = null;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public InvoerenDossierNummerPage( String bsnWaarde ) {
    this();
    this.bsnWaarde = bsnWaarde;
    isBsnBekend = "Ja";
    if( bsn != null ) {
      bsn.error( getString( "bsn.invalid" ) );
      bsn.setVisible( true );
      doorgaanButton.setEnabled( true );
    }
  }

  @SuppressWarnings( "serial" )
  public InvoerenDossierNummerPage() {
    super();

    Form<Object> form = new Form<Object>( ID_FORM );

    // Invoervelden
    final TextField<String> dossierNummer = new CijfersTextField( ID_DOSSIERNUMMER, new PropertyModel<String>( this,
            "dossiernummerWaarde" ) );
    final Component dossierNummerFeedback = borderize( ID_DOSSIERNUMMER + "Feedback", dossierNummer )
            .setOutputMarkupPlaceholderTag( true );
    dossierNummer.setVisible( false );
    form.add( dossierNummerFeedback );
    bsn = new BsnTextField( ID_BSN, new PropertyModel<String>( this, "bsnWaarde" ) );
    final Component bsnFeedback = borderize( ID_BSN + "Feedback", bsn ).setOutputMarkupPlaceholderTag( true );
    bsn.setVisible( false );
    form.add( bsnFeedback );
    // Keuze veld
    final List<String> opties = Arrays.asList( new String[] { "Ja", "Nee" } );
    final RadioChoice<String> bsnBekend = new RadioChoice<String>( ID_BSN_BEKEND, new PropertyModel<String>( this, "isBsnBekend" ),
            opties );
    form.add( borderize( ID_BSN_BEKEND + "Feedback", bsnBekend ) );

    bsnBekend.add( new AjaxFormChoiceComponentUpdatingBehavior() {
      @Override
      protected void onUpdate( AjaxRequestTarget target ) {
        boolean bsnOpgeven = "Ja".equals( isBsnBekend );
        dossierNummer.setVisible( !bsnOpgeven );
        dossierNummer.setRequired( !bsnOpgeven );
        bsn.setVisible( bsnOpgeven );
        bsn.setRequired( bsnOpgeven );
        doorgaanButton.setEnabled( true );
        target.add( dossierNummerFeedback );
        target.add( bsnFeedback );
        target.add( doorgaanButton );
      }
    } );

    doorgaanButton = new Button( "doorgaan" ) {
      @Override
      public void onSubmit() {
        PageParameters pageParameters = new PageParameters();
        if( "Ja".equals( bsnBekend.getDefaultModelObject() ) ) {
          pageParameters.add( EersteVerblijfadresPage.BSN_PARAM_NAME, bsnWaarde );
        } else {
          pageParameters.add( EersteVerblijfadresPage.DOSSIERNUMMER_PARAM_NAME, dossiernummerWaarde );
        }
        setResponsePage( EersteVerblijfadresPage.class, pageParameters );
        super.onSubmit();
      }
    };
    doorgaanButton.setEnabled( false );
    form.add( doorgaanButton );

    // Add the form to the page
    add( form );
  }
}
