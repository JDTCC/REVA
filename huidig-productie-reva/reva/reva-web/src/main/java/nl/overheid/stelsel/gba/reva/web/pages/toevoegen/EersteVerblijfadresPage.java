package nl.overheid.stelsel.gba.reva.web.pages.toevoegen;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.clerezza.rdf.core.Resource;
import org.apache.clerezza.rdf.core.sparql.ResultSet;
import org.apache.clerezza.rdf.core.sparql.SolutionMapping;
import org.apache.clerezza.rdf.core.sparql.query.Variable;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.Validatable;
import org.ops4j.pax.wicket.api.PaxWicketMountPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.shiro.ShiroConstraint;
import org.wicketstuff.shiro.annotation.ShiroSecurityConstraint;

import nl.overheid.stelsel.digimelding.astore.utils.DateUtils;
import nl.overheid.stelsel.digimelding.astore.utils.ResourceUtils;
import nl.overheid.stelsel.gba.reva.bag.model.BCAdres;
import nl.overheid.stelsel.gba.reva.bag.model.BCWoonplaats;
import nl.overheid.stelsel.gba.reva.bag.model.Gebruiksdoel;
import nl.overheid.stelsel.gba.reva.web.components.BsnTextField;
import nl.overheid.stelsel.gba.reva.web.components.CijfersEnOfLettersTextField;
import nl.overheid.stelsel.gba.reva.web.components.CijfersTextField;
import nl.overheid.stelsel.gba.reva.web.components.LettersTextField;
import nl.overheid.stelsel.gba.reva.web.components.OpenbareruimteTextField;
import nl.overheid.stelsel.gba.reva.web.components.PostcodeTextField;
import nl.overheid.stelsel.gba.reva.web.components.WoonplaatsTextField;
import nl.overheid.stelsel.gba.reva.web.locators.AnnotationStoreServiceLocator;
import nl.overheid.stelsel.gba.reva.web.locators.BagServiceLocator;
import nl.overheid.stelsel.gba.reva.web.locators.TemplatingServiceLocator;
import nl.overheid.stelsel.gba.reva.web.pages.HomePage;
import nl.overheid.stelsel.gba.reva.web.pages.RevaPage;
import nl.overheid.stelsel.gba.reva.web.pages.confirmation.ConfirmationButton;
import nl.overheid.stelsel.gba.reva.web.security.RevaPermissions;
import nl.overheid.stelsel.gba.reva.web.utils.AstoreUtils;
import nl.overheid.stelsel.gba.reva.web.utils.ProfileUtils;
import nl.overheid.stelsel.gba.reva.web.utils.StringUtils;
import nl.overheid.stelsel.gba.reva.web.validators.BsnValidator;
import nl.xup.template.Bindings;
import nl.xup.template.SimpleBindings;
import nl.xup.template.Template;

/**
 * Reva pagina voor het invoeren van het eerste verblijfadres. Voorwaarde is een
 * page parameter met het RNI dossiernummer.
 * 
 */
@PaxWicketMountPoint( mountPoint = "/opvoeren/eersteverblijfadres" )
@ShiroSecurityConstraint( constraint = ShiroConstraint.LoggedIn )
public class EersteVerblijfadresPage extends RevaPage {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  public static final String DOSSIERNUMMER_PARAM_NAME = "dossiernummer";
  public static final String BSN_PARAM_NAME = "bsn";
  public static final String STAM_PARAM_NAME = "stam";

  private static final String TEMPLATE_EERSTEVERBLIJFADRES = "eersteVerblijfAdres.ftl";
  private static final String TEMPLATE_BSN = "bsn.ftl";
  private static final String TEMPLATE_NIEUWADRES = "nieuwAdres.ftl";
  private static final List<String> AANDUIDING_HUISNUMMER_CHOICES = new ArrayList<>();

  static {
    AANDUIDING_HUISNUMMER_CHOICES.add( "by" );
    AANDUIDING_HUISNUMMER_CHOICES.add( "to" );
  }

  private static final List<String> ADRES_TYPES = new ArrayList<>();

  static {
    ADRES_TYPES.add( "Eigen adres" );
    ADRES_TYPES.add( "Adres van werkgever" );
    ADRES_TYPES.add( "Adres met bewijsstuk" );
    ADRES_TYPES.add( "Anders" );
  }

  private static final String ID_FORM = "verblijfadresFormulier";
  private static final String ID_DOSSIERNUMMER = "dossierNummer";
  private static final String ID_STAM = "stamId";
  private static final String ID_BSN = "bsn";
  private static final String ID_GEMEENTECODE = "gemeenteVanInschrijving";
  private static final String ID_REGISTRATIETIJDSTEMPEL = "registratieTijdstempel";
  private static final String ID_CORRECTIETIJDSTEMPEL = "correctieTijdstempel";
  private static final String ID_WOONPLAATS = "woonPlaats";
  private static final String ID_OPENBARERUIMTE = "openbareRuimte";
  private static final String ID_POSTCODE = "postCode";
  private static final String ID_HUISNUMMER = "huisNummer";
  private static final String ID_HUISLETTER = "huisLetter";
  private static final String ID_HUISNUMMER_TOEVOEGING = "huisNummerToevoeging";
  private static final String ID_AANDUIDING_HUISNUMMER = "aanduidingHuisNummer";
  private static final String ID_ADRESTYPE = "adresType";
  private static final String ID_TOELICHTING = "toelichting";

  private static final String ID_BAG_ID_WOONPLAATS = "idCodeObject";
  private static final String ID_BAG_ID_NUMMERAANDUIDING = "idCodeNummerAanduiding";
  private static final String ID_BAG_GEBRUIKSDOELEN = "bagGebruiksdoelen";

  private static final String ID_BUTTON_BAG_CONTROLE = "bag-controle";
  private static final String ID_BUTTON_BEWAREN = "bewaren";
  private static final String ID_BUTTON_OPVOEREN_BSN = "opvoeren.bsn";
  private static final String ID_BUTTON_BEWAREN_BSN = "bewaren.bsn";
  private static final String ID_BUTTON_VERWIJDEREN = "verwijderen";
  private static final String ID_BUTTON_NIEUW_ADRES = "opvoeren.nieuwAdres";
  private static final String ID_BUTTON_BEWAREN_NIEUW_ADRES = "bewaren.nieuwAdres";

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger( EersteVerblijfadresPage.class );

  private static final long serialVersionUID = 5437959295049106734L;
  private static Map<String, Template> templates = new HashMap<>();

  private enum PageMode {
    EersteVerblijfAdresInvoer, ReadOnly, BsnInvoer, NieuwAdres;
  }

  private enum BagCheckStatus {
    Onbekend( "" ), Groen( "bag-green" ), Geel( "bag-yellow" ), Rood( "bag-red" );

    private String classAttribute;

    BagCheckStatus( String classAttribute ) {
      this.classAttribute = classAttribute;
    }

    public String getClassAttribute() {
      return classAttribute;
    }
  }

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private PageMode paginaMode = PageMode.ReadOnly;
  private final EersteVerblijfAdresGegevens gegevens = new EersteVerblijfAdresGegevens();
  private Map<String, Component> componentCache = new HashMap<>();

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  @SuppressWarnings( "serial" )
  public EersteVerblijfadresPage( final PageParameters parameters ) throws Exception {
    super( parameters );

    paginaMode = bepaalPaginaModus( parameters );
    if( paginaMode == null ) {
      // Geen pagina mode dan eerst een dossier nummer invoeren (als dat mag).
      SecurityUtils.getSubject().checkPermission( RevaPermissions.OPVOEREN_EVA.getStringPermission() );
      InvoerenDossierNummerPage page = new InvoerenDossierNummerPage();
      page.info( getString( "dossierNummer.ontbreekt" ) );
      setResponsePage( page );
      return;
    }

    if( gegevens.getBsn() != null && !isValidBSN( gegevens.getBsn() ) ) {
      InvoerenDossierNummerPage page = new InvoerenDossierNummerPage( gegevens.getBsn() );
      setResponsePage( page );
      return;
    }

    final Form<EersteVerblijfAdresGegevens> form = new Form<EersteVerblijfAdresGegevens>( ID_FORM );
    form.setDefaultModel( new CompoundPropertyModel<EersteVerblijfAdresGegevens>( gegevens ) );
    form.setOutputMarkupId( true );
    cache( form );

    final WoonplaatsTextField woonplaatsVeld = new WoonplaatsTextField( ID_WOONPLAATS );
    final OpenbareruimteTextField openbareRuimteVeld = new OpenbareruimteTextField( ID_OPENBARERUIMTE,
            "woonPlaatsFeedback:woonPlaatsFeedback_body:woonPlaats" );

    final DropDownChoice<String> aanduidingHuisNummerVeld = new DropDownChoice<String>( ID_AANDUIDING_HUISNUMMER );
    aanduidingHuisNummerVeld.setNullValid( true );
    aanduidingHuisNummerVeld.setChoices( AANDUIDING_HUISNUMMER_CHOICES );

    DateTextField registratieTijdstempelVeld = new DateTextField( ID_REGISTRATIETIJDSTEMPEL, "EEEE d MMMM yyyy HH:mm:ss" );
    DateTextField correctieTijdstempelVeld = new DateTextField( ID_CORRECTIETIJDSTEMPEL, "EEEE d MMMM yyyy HH:mm:ss" );

    form.add( cache( new TextField<String>( ID_DOSSIERNUMMER ) ).setEnabled( false ) );
    form.add( borderize( "bsnFeedback", cache( new BsnTextField( ID_BSN ) ) ) );
    form.add( cache( new TextField<String>( ID_GEMEENTECODE ) ).setEnabled( false ) );
    form.add( cache( registratieTijdstempelVeld ).setEnabled( false ) );
    form.add( cache( correctieTijdstempelVeld ).setEnabled( false ) );
    form.add( borderize( "woonPlaatsFeedback", cache( woonplaatsVeld ) ) );
    form.add( borderize( "openbareRuimteFeedback", cache( openbareRuimteVeld ) ) );
    form.add( borderize( "postCodeFeedback", cache( new PostcodeTextField( ID_POSTCODE ) ) ) );
    form.add( borderize( "huisNummerFeedback", cache( new CijfersTextField( ID_HUISNUMMER ) ) ) );
    form.add( borderize( "huisLetterFeedback", cache( new LettersTextField( ID_HUISLETTER ) ) ) );
    form.add( borderize( "huisNummerToevoegingFeedback", cache( new CijfersEnOfLettersTextField( ID_HUISNUMMER_TOEVOEGING ) ) ) );
    form.add( borderize( "aanduidingHuisNummerFeedback", cache( aanduidingHuisNummerVeld ) ) );
    form.add( borderize( "adresTypeFeedback", cache( new RadioChoice<>( ID_ADRESTYPE, ADRES_TYPES ) ) ) );
    form.add( borderize( "toelichtingFeedback", cache( new TextArea<String>( ID_TOELICHTING ) ) ) );

    // BAG velden
    form.add( cache( new TextField<String>( ID_BAG_ID_WOONPLAATS ).setEnabled( false ) ) );
    form.add( cache( new TextField<String>( ID_BAG_ID_NUMMERAANDUIDING ).setEnabled( false ) ) );
    form.add( cache( new TextField<String>( ID_BAG_GEBRUIKSDOELEN ).setEnabled( false ) ) );

    Button bewarenButton = new Button( ID_BUTTON_BEWAREN ) {
      @Override
      public void onSubmit() {
        super.onSubmit();

        try {
          bagControleUitvoeren( gegevens );
          if( bewaren( gegevens, TEMPLATE_EERSTEVERBLIJFADRES ) ) {
            PageParameters parameters = new PageParameters();
            parameters.add( STAM_PARAM_NAME, gegevens.getStamId().toString() );
            EersteVerblijfadresPage newPage = new EersteVerblijfadresPage( parameters );
            newPage.success( getString( "bewaren.succes" ) );
            setResponsePage( newPage );
          }
        } catch( Exception e ) {
          logger.error( "Storing eerste verblijfadres failed due to: ", e );
          error( getString( "bewaren.error" ) );
        }
      }
    };
    form.add( cache( bewarenButton ) );

    Button bagControleButton = new Button( ID_BUTTON_BAG_CONTROLE ) {
      @Override
      public void onSubmit() {
        super.onSubmit();
        try {
          bagControleUitvoeren( gegevens );
        } catch( Exception e ) {
          logger.error( "BAG controle failed due to: ", e );
          error( getString( "bag.controle.error" ) );
        }
        getCachedComponent( ID_BAG_ID_WOONPLAATS ).setVisible( true );
        getCachedComponent( ID_BUTTON_BEWAREN ).setVisible( paginaMode == PageMode.EersteVerblijfAdresInvoer );
      }
    };
    form.add( cache( bagControleButton ) );

    form.add( cache( pagemodeButton( ID_BUTTON_OPVOEREN_BSN, PageMode.BsnInvoer ) ) );

    Button bewarenBsnButton = new Button( ID_BUTTON_BEWAREN_BSN ) {
      @Override
      public void onSubmit() {
        super.onSubmit();

        try {
          if( bewaren( gegevens, TEMPLATE_BSN ) ) {
            PageParameters parameters = new PageParameters();
            parameters.add( STAM_PARAM_NAME, gegevens.getStamId().toString() );
            EersteVerblijfadresPage newPage = new EersteVerblijfadresPage( parameters );
            newPage.success( getString( "bewaren.bsn.succes" ) );
            setResponsePage( newPage );
          }
        } catch( Exception e ) {
          logger.error( "Storing bsn failed due to: ", e );
          error( getString( "bewaren.bsn.error" ) );
        }
      }
    };
    form.add( cache( bewarenBsnButton ) );

    form.add( cache( pagemodeButton( ID_BUTTON_NIEUW_ADRES, PageMode.NieuwAdres ) ) );

    Button bewarenNieuwAdresButton = new Button( ID_BUTTON_BEWAREN_NIEUW_ADRES ) {
      @Override
      public void onSubmit() {
        super.onSubmit();

        try {
          bagControleUitvoeren( gegevens );
          if( bewaren( gegevens, TEMPLATE_NIEUWADRES ) ) {
            PageParameters parameters = new PageParameters();
            parameters.add( STAM_PARAM_NAME, gegevens.getStamId().toString() );
            EersteVerblijfadresPage newPage = new EersteVerblijfadresPage( parameters );
            newPage.success( getString( "bewaren.nieuwAdres.succes" ) );
            setResponsePage( newPage );
          }
        } catch( Exception e ) {
          logger.error( "Storing 'nieuw adres' failed due to: ", e );
          error( getString( "bewaren.nieuwAdres.error" ) );
        }
      }
    };
    form.add( cache( bewarenNieuwAdresButton ) );

    ConfirmationButton verwijderenButton = new ConfirmationButton( ID_BUTTON_VERWIJDEREN, getString( ID_BUTTON_VERWIJDEREN ),
            getString( "verwijderen.confirm.message" ) ) {
      @Override
      protected void onAccept( AjaxRequestTarget target ) {
        try {
          if( verwijderen( gegevens ) ) {
            HomePage pagina = new HomePage();
            pagina.success( getString( "verwijderen.registratie.succes" ) );
            setResponsePage( pagina );
          }
        } catch( Exception e ) {
          logger.error( "Removing registration failed due to: ", e );
          error( getString( "verwijderen.registratie.error" ) );
        }
      }

      @Override
      protected void onReject( AjaxRequestTarget target ) {
        // ignore
      }
    };
    verwijderenButton.getConfirmationPanel().setPanelTitle( getString( "verwijderen.confirm.title" ) );
    verwijderenButton.getConfirmationPanel().setAcceptCaption( getString( "verwijderen.confirm.accept" ) );
    verwijderenButton.getConfirmationPanel().setRejectCaption( getString( "verwijderen.confirm.reject" ) );
    form.add( cache( verwijderenButton ) );

    // Formulier aan de pagina toevoegen.
    add( form );

    // Fine tune de componenten afhankelijk van de pagina mode.
    prepare( paginaMode );
  }

  // -------------------------------------------------------------------------
  // Overrides: RevaPage
  // -------------------------------------------------------------------------

  @Override
  public String getTitleLabelResourceName() {
    return "screenTitleLabel.opvoeren";
  };

  @Override
  protected void onBeforeRender() {
    super.onBeforeRender();

    // Properly report bag check results.
    BagCheckStatus bagCheckStatus = BagCheckStatus.Rood;
    if( gegevens.getBagGebruiksdoelen() != null ) {
      bagCheckStatus = BagCheckStatus.Geel;
      if( gegevens.getBagGebruiksdoelen().contains( Gebruiksdoel.WOONFUNCTIE.value() )
              || gegevens.getBagGebruiksdoelen().contains( Gebruiksdoel.LOGIESFUNCTIE.value() )
              || gegevens.getBagGebruiksdoelen().contains( Gebruiksdoel.GEZONDHEIDSZORGFUNCTIE.value() ) ) {
        bagCheckStatus = BagCheckStatus.Groen;
      } else if( gegevens.getBagGebruiksdoelen().contains( Gebruiksdoel.BIJEENKOMSTFUNCTIE.value() )
              || gegevens.getBagGebruiksdoelen().contains( Gebruiksdoel.CELFUNCTIE.value() ) ) {
        bagCheckStatus = BagCheckStatus.Rood;
      }
    }
    WebMarkupContainer container = new WebMarkupContainer( "bagCheck" );
    container.add( new AttributeAppender( "class", new Model<String>( bagCheckStatus.getClassAttribute() ), " " ) );
    if( this.get( ID_FORM ) != null ) {
      MarkupContainer.class.cast( this.get( ID_FORM ) ).addOrReplace( container );
    }
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  @SuppressWarnings( "serial" )
  private Button pagemodeButton( final String buttonId, final PageMode pagemode ) {
    return new Button( buttonId ) {
      @Override
      public void onSubmit() {
        super.onSubmit();
        paginaMode = pagemode;
        prepare( paginaMode );
      }
    };
  }
  
  /**
   * Bepaal de te gebruiken pagin modus op basis van aanwezige gegevens.
   * 
   * @param parameters
   * @return
   * @throws Exception
   */
  private PageMode bepaalPaginaModus( PageParameters parameters ) throws Exception {
    // Dossiernummer of bsn is noodzakelijk.
    String dossiernummer = parameters.get( DOSSIERNUMMER_PARAM_NAME ).toOptionalString();
    String bsn = parameters.get( BSN_PARAM_NAME ).toOptionalString();
    String stam = parameters.get( STAM_PARAM_NAME ).toOptionalString();
    if( StringUtils.isEmpty( dossiernummer ) && StringUtils.isEmpty( bsn ) && StringUtils.isEmpty( stam ) ) {
      // Geen dossiernummer, bsn of stam dan kunnen we de modus ook niet
      // bepalen.
      return null;
    }
    if( !StringUtils.isEmpty( stam ) ) {
      gegevens.setStamId( UUID.fromString( stam ) );
    }
    gegevens.setDossierNummer( dossiernummer );
    gegevens.setBsn( bsn );

    ResultSet resultaat = retrieveEersteVerblijfAdres( gegevens );
    plaatsResultaatInGegevens( resultaat );

    if( gegevens.getStamId() != null ) {
      // Eerste verblijfadres reeds bekend, dus alleen alleen tonen
      return PageMode.ReadOnly;
    }

    // Dossier nummer bekend maar nog geen verblijfadres registreerd.
    return PageMode.EersteVerblijfAdresInvoer;
  }

  private boolean isValidBSN( String bsn ) {
    IValidatable<String> validatable = new Validatable<String>( bsn );
    BsnValidator.getInstance().validate( validatable );
    return validatable.isValid();
  }

  private void prepare( PageMode modus ) {
    if( modus == PageMode.EersteVerblijfAdresInvoer ) {
      prepareEersteVerblijfAdresInvoer(RevaPermissions.OPVOEREN_EVA);
    } else if( modus == PageMode.ReadOnly ) {
      prepareReadOnly();
    } else if( modus == PageMode.BsnInvoer ) {
      prepareBsnInvoer();
    } else if( modus == PageMode.NieuwAdres ) {
      prepareNieuwAdresInvoer();
    }
  }

  private void prepareEersteVerblijfAdresInvoer( RevaPermissions permission ) {
    // Security part
    SecurityUtils.getSubject().checkPermission( permission.getStringPermission() );
    boolean showBsnField = SecurityUtils.getSubject().isPermitted( RevaPermissions.OPVOEREN_BSN.getStringPermission() );
    boolean bsnReedsBekend = (gegevens.getBsn() != null);

    getCachedComponent( ID_DOSSIERNUMMER ).setEnabled( false );
    getCachedComponent( ID_DOSSIERNUMMER ).setVisible( !bsnReedsBekend );
    getCachedComponent( ID_BSN ).setVisible( showBsnField );
    getCachedComponent( ID_BSN ).setEnabled( !bsnReedsBekend );
    getCachedComponent( ID_GEMEENTECODE ).setVisible( false );
    getCachedComponent( ID_REGISTRATIETIJDSTEMPEL ).setVisible( false );
    getCachedComponent( ID_CORRECTIETIJDSTEMPEL ).setVisible( false );
    makeRequired( getCachedComponent( ID_WOONPLAATS ), true ).setEnabled( true );
    makeRequired( getCachedComponent( ID_OPENBARERUIMTE ), true ).setEnabled( true );
    getCachedComponent( ID_POSTCODE ).setEnabled( true );
    makeRequired( getCachedComponent( ID_HUISNUMMER ), true ).setEnabled( true );
    getCachedComponent( ID_HUISLETTER ).setEnabled( true );
    getCachedComponent( ID_HUISNUMMER_TOEVOEGING ).setEnabled( true );
    getCachedComponent( ID_AANDUIDING_HUISNUMMER ).setEnabled( true );
    makeRequired( getCachedComponent( ID_ADRESTYPE ), true ).setEnabled( true );
    getCachedComponent( ID_TOELICHTING ).setEnabled( true );

    // Bag velden
    getCachedComponent( ID_BAG_ID_WOONPLAATS ).setVisible( false );

    // Buttons
    getCachedComponent( ID_BUTTON_BAG_CONTROLE ).setVisible( true );
    getCachedComponent( ID_BUTTON_BEWAREN ).setVisible( false );
    getCachedComponent( ID_BUTTON_OPVOEREN_BSN ).setVisible( false );
    getCachedComponent( ID_BUTTON_BEWAREN_BSN ).setVisible( false );
    getCachedComponent( ID_BUTTON_VERWIJDEREN ).setVisible( false );
    getCachedComponent( ID_BUTTON_NIEUW_ADRES ).setVisible( false );
    getCachedComponent( ID_BUTTON_BEWAREN_NIEUW_ADRES ).setVisible( false );
  }

  private void prepareReadOnly() {
    // Security part
    SecurityUtils.getSubject().checkPermission( RevaPermissions.RAADPLEGEN_DETAIL.getStringPermission() );
    boolean showBsnField = SecurityUtils.getSubject().isPermitted( RevaPermissions.RAADPLEGEN_BSN.getStringPermission() );
    boolean showBsnButton = SecurityUtils.getSubject().isPermitted( RevaPermissions.OPVOEREN_BSN.getStringPermission() )
            && (gegevens.getBsn() == null);
    boolean showVerwijderenButton = SecurityUtils.getSubject().isPermitted(
            RevaPermissions.VERWIJDEREN_REGISTRATIE.getStringPermission() );
    boolean showNieuwAdresButton = SecurityUtils.getSubject().isPermitted(
            RevaPermissions.OPVOEREN_NIEUW_ADRES.getStringPermission() );
    boolean heeftAdresCorrectie = !gegevens.getRegistratieTijdstempel().equals(gegevens.getCorrectieTijdstempel());

    getCachedComponent( ID_DOSSIERNUMMER ).setEnabled( false );
    getCachedComponent( ID_BSN ).setEnabled( false ).setVisible( showBsnField );
    getCachedComponent( ID_GEMEENTECODE ).setVisible( true );
    getCachedComponent( ID_REGISTRATIETIJDSTEMPEL ).setVisible( true );
    getCachedComponent( ID_CORRECTIETIJDSTEMPEL ).setVisible( heeftAdresCorrectie );
    getCachedComponent( ID_WOONPLAATS ).setEnabled( false );
    getCachedComponent( ID_OPENBARERUIMTE ).setEnabled( false );
    getCachedComponent( ID_POSTCODE ).setEnabled( false );
    getCachedComponent( ID_HUISNUMMER ).setEnabled( false );
    getCachedComponent( ID_HUISLETTER ).setEnabled( false );
    getCachedComponent( ID_HUISNUMMER_TOEVOEGING ).setEnabled( false );
    getCachedComponent( ID_AANDUIDING_HUISNUMMER ).setEnabled( false );
    getCachedComponent( ID_ADRESTYPE ).setEnabled( false );
    getCachedComponent( ID_TOELICHTING ).setEnabled( false );

    // Bag velden
    getCachedComponent( ID_BAG_ID_WOONPLAATS ).setVisible( true );

    // Buttons
    getCachedComponent( ID_BUTTON_BAG_CONTROLE ).setVisible( false );
    getCachedComponent( ID_BUTTON_BEWAREN ).setVisible( false );
    getCachedComponent( ID_BUTTON_OPVOEREN_BSN ).setVisible( showBsnButton );
    getCachedComponent( ID_BUTTON_BEWAREN_BSN ).setVisible( false );
    getCachedComponent( ID_BUTTON_VERWIJDEREN ).setVisible( showVerwijderenButton );
    getCachedComponent( ID_BUTTON_NIEUW_ADRES ).setVisible( showNieuwAdresButton );
    getCachedComponent( ID_BUTTON_BEWAREN_NIEUW_ADRES ).setVisible( false );
  }

  private void prepareBsnInvoer() {
    // Security part
    SecurityUtils.getSubject().checkPermission( RevaPermissions.OPVOEREN_BSN.getStringPermission() );

    // Minimale verschillen van read-only scherm
    prepareReadOnly();

    // Verschil bij velden
    makeRequired( getCachedComponent( ID_BSN ), true ).setEnabled( true ).setVisible( true );

    // Verschil bij Buttons
    getCachedComponent( ID_BUTTON_OPVOEREN_BSN ).setVisible( false );
    getCachedComponent( ID_BUTTON_BEWAREN_BSN ).setVisible( true );
    getCachedComponent( ID_BUTTON_VERWIJDEREN ).setVisible( false );
    getCachedComponent( ID_BUTTON_NIEUW_ADRES ).setVisible( false );
    getCachedComponent( ID_BUTTON_BEWAREN_NIEUW_ADRES ).setVisible( false );
  }

  private void prepareNieuwAdresInvoer() {
    // Minimale verschillen met het eerste adres scherm
    prepareEersteVerblijfAdresInvoer(RevaPermissions.OPVOEREN_NIEUW_ADRES);

    // Verschil bij velden
    getCachedComponent( ID_GEMEENTECODE ).setVisible( true );
    getCachedComponent( ID_REGISTRATIETIJDSTEMPEL ).setVisible( true );

    // Verschil bij Buttons
    getCachedComponent( ID_BUTTON_BAG_CONTROLE ).setVisible( true );
    getCachedComponent( ID_BUTTON_BEWAREN ).setVisible( false );
    getCachedComponent( ID_BUTTON_NIEUW_ADRES ).setVisible( false );
    getCachedComponent( ID_BUTTON_BEWAREN_NIEUW_ADRES ).setVisible( true );
  }

  /**
   * Geeft de template gevraagde template.
   * 
   * @return Template object.
   * @throws Exception
   */
  private Template getTemplate( String templateName ) throws Exception {
    if( templates.get( templateName ) == null ) {
      Reader reader = new InputStreamReader( getClass().getResourceAsStream( templateName ) );
      templates.put( templateName, TemplatingServiceLocator.getService().getTemplate( reader ) );
    }
    return templates.get( templateName );
  }

  /**
   * Haal het eerste verblijfadres op bij het dossiernummer. Indien er nog geen
   * eerste verblijfadres is geregistreerd dan zal het gegevens object niet
   * worden aangevuld en blijft het stamId leeg (null).
   * 
   * @param gegevens
   * @throws Exception
   */
  private ResultSet retrieveEersteVerblijfAdres( EersteVerblijfAdresGegevens gegevens ) throws Exception {
    // Check data store if the dossier number is known.
    Map<String, String> parameters = new HashMap<>();
    if( gegevens.getStamId() != null ) {
      parameters.put( ID_STAM, gegevens.getStamId().toString() );
    } else {
      parameters.put( ID_DOSSIERNUMMER, gegevens.getDossierNummer() );
      parameters.put( ID_BSN, gegevens.getBsn() );
    }

    Object resultaat = AnnotationStoreServiceLocator.getService().namedQuery( "zoekInReva", parameters );
    if( resultaat instanceof ResultSet ) {
      return ResultSet.class.cast( resultaat );
    }

    return null;
  }

  private void plaatsResultaatInGegevens( ResultSet result ) {
    if( result.hasNext() ) {
      CompoundPropertyModel<EersteVerblijfAdresGegevens> model = new CompoundPropertyModel<>( gegevens );
      SolutionMapping mapping = result.next();
      for( Map.Entry<Variable, Resource> entry : mapping.entrySet() ) {
        String value = null;
        if( entry.getValue() != null ) {
          value = ResourceUtils.getResourceValue( entry.getValue() );
        }
        model.bind( entry.getKey().getName() ).setObject( value );
      }
    }
  }

  private boolean bewaren( EersteVerblijfAdresGegevens gegevens, String templateName ) throws Exception {
    if( gegevens.getStamId() == null ) {
      gegevens.setStamId( UUID.randomUUID() );
    }

    // Check BSN ter voorkoming van dubbele invoer op hetzelfde BSN. Dit geldt niet voor het vastleggen
    // van een nieuw adres.
    if( gegevens.getBsn() != null && paginaMode != PageMode.NieuwAdres ) {
      EersteVerblijfAdresGegevens zoekGegevens = new EersteVerblijfAdresGegevens();
      zoekGegevens.setBsn( gegevens.getBsn() );
      ResultSet resultaat = retrieveEersteVerblijfAdres( zoekGegevens );
      if( resultaat.hasNext() ) {
        // BSN is reeds opgevoerd, nogmaals opvoeren mag niet.
        error( getString( "bewaren.bsn.bestaat" ) );
        return false;
      }
    }

    gegevens.setRegistratieTijdstempel( new Date() );

    // Prepare bindings
    String gebruiker = SecurityUtils.getSubject().getPrincipal().toString();
    String gemeenteCode = ProfileUtils.getGemeentecode( SecurityUtils.getSubject() );

    Bindings bindings = new SimpleBindings();
    bindings.put( "uuidRoot", gegevens.getStamId() );
    bindings.put( "reva", gegevens );
    bindings.put( "gebruiker", gebruiker );
    bindings.put( "gemeenteVanInschrijving", gemeenteCode );
    String timestamp = DateUtils.getDateAsISO8601String( gegevens.getRegistratieTijdstempel() );
    bindings.put( "timestamp", timestamp );
    bindings.put( "timestampDate", timestamp.substring(0, 10) );
    bindings.put( "timestampDateInt", String.valueOf(DateUtils.getDateEEJJMMDD( gegevens.getRegistratieTijdstempel() ) ) );
    bindings.put( "uuidBsn", UUID.randomUUID() );
    bindings.put( "uuidDossiernummer", UUID.randomUUID() );
    bindings.put( "uuidAdres", UUID.randomUUID() );

    AstoreUtils.store( gegevens.getStamId(), bindings, getTemplate( templateName ) );
    return true;
  }

  private boolean verwijderen( EersteVerblijfAdresGegevens gegevens ) {
    try {
      AnnotationStoreServiceLocator.getService().remove( gegevens.getStamId() );
    } catch( Exception ex ) {
      logger.warn( "Registratie verwijderen mislukt!", ex );
      return false;
    }
    return true;
  }

  private void bagControleUitvoeren( EersteVerblijfAdresGegevens gegevens ) {
    Collection<BCAdres> adressen = BagServiceLocator.getService().getAdressen( gegevens.getWoonPlaats(),
            gegevens.getOpenbareRuimte(), null, gegevens.getHuisNummer(), gegevens.getHuisLetter(),
            gegevens.getHuisNummerToevoeging() );

    if( adressen.size() == 1 ) {
      BCAdres adres = adressen.iterator().next();
      bagGegevensOvernemen( gegevens, adres );
    } else {
      if( adressen.size() > 0 ) {
        BCAdres adres = adressen.iterator().next();
        gegevens.setIdCodeObject( adres.getBCWoonplaats().getIdentificatie() );
      } else {
        BCWoonplaats woonplaats = BagServiceLocator.getService().getWoonplaats( gegevens.getWoonPlaats() );
        if( woonplaats != null ) {
          gegevens.setIdCodeObject( woonplaats.getIdentificatie() );
        } else {
          gegevens.setIdCodeObject( null );
        }
      }
      gegevens.setIdCodeNummerAanduiding( null );
      gegevens.setBagGebruiksdoelen( null );
    }
  }

  private void bagGegevensOvernemen( EersteVerblijfAdresGegevens gegevens, BCAdres adres ) {
    gegevens.setWoonPlaats( adres.getBCWoonplaats().getWoonplaatsNaam() );
    gegevens.setOpenbareRuimte( adres.getBCOpenbareRuimte().getOpenbareRuimteNaam() );
    gegevens.setIdCodeObject( adres.getBCWoonplaats().getIdentificatie() );
    gegevens.setIdCodeNummerAanduiding( adres.getBCNummeraanduiding().getIdentificatie() );
    gegevens.setPostCode( adres.getBCNummeraanduiding().getPostcode() );

    StringBuilder gebruiksdoelen = new StringBuilder();
    if( adres.getAdresseerbaarobjectKoppeling().getBCVerblijfsobject() != null ) {
      List<Gebruiksdoel> gebruiksdoelLijst = adres.getAdresseerbaarobjectKoppeling().getBCVerblijfsobject()
              .getGebruiksdoelVerblijfsobject();
      String separator = "";
      for( Gebruiksdoel gebruiksdoel : gebruiksdoelLijst ) {
        gebruiksdoelen.append( separator );
        gebruiksdoelen.append( gebruiksdoel.value() );
        separator = ", ";
      }
    } else {
      // Geen adresseerbaar object dan ook geen gebruiksdoelen. Neem het
      // type adresseerbaar object dan maar als gebruiksdoel om de gebruiker
      // toch enige feedback te geven over het type adres.
      gebruiksdoelen.append( adres.getBCNummeraanduiding().getTypeAdresseerbaarObject().value() );
    }
    gegevens.setBagGebruiksdoelen( gebruiksdoelen.toString() );
  }

  /**
   * Voeg het ontvangen toe aan de componenten cache voor eenvoudige toegang
   * later.
   * 
   * @param component
   * @return het ontvangen component.
   */
  private Component cache( Component component ) {
    componentCache.put( component.getId(), component );
    return component;
  }

  private Component getCachedComponent( String id ) {
    return componentCache.get( id );
  }

  private Component makeRequired( Component component, boolean required ) {
    if( component instanceof FormComponent ) {
      ((FormComponent<?>) component).setRequired( required );
    }
    return component;
  }
}
