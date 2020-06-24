package nl.overheid.stelsel.gba.reva.web.pages.zoeken;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.clerezza.rdf.core.sparql.SolutionMapping;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.convert.IConverter;
import org.ops4j.pax.wicket.api.PaxWicketMountPoint;
import org.wicketstuff.shiro.ShiroConstraint;
import org.wicketstuff.shiro.annotation.ShiroSecurityConstraint;

import nl.overheid.stelsel.digimelding.astore.utils.ResourceUtils;
import nl.overheid.stelsel.gba.reva.web.components.BsnTextField;
import nl.overheid.stelsel.gba.reva.web.components.CijfersTextField;
import nl.overheid.stelsel.gba.reva.web.components.FormattedDateTextField;
import nl.overheid.stelsel.gba.reva.web.components.OpenbareruimteTextField;
import nl.overheid.stelsel.gba.reva.web.components.PageableResultSetPanel;
import nl.overheid.stelsel.gba.reva.web.components.PostcodeTextField;
import nl.overheid.stelsel.gba.reva.web.components.WoonplaatsTextField;
import nl.overheid.stelsel.gba.reva.web.converters.GUIInputDateConverter;
import nl.overheid.stelsel.gba.reva.web.pages.RevaPage;
import nl.overheid.stelsel.gba.reva.web.pages.toevoegen.EersteVerblijfadresPage;
import nl.overheid.stelsel.gba.reva.web.resources.QueryAsXlsResource;
import nl.overheid.stelsel.gba.reva.web.resources.QueryAsXlsResourceReference;
import nl.overheid.stelsel.gba.reva.web.security.RevaPermissions;
import nl.overheid.stelsel.gba.reva.web.utils.ProfileUtils;
import nl.overheid.stelsel.gba.reva.web.validators.DateRangeValidator;
import nl.overheid.stelsel.gba.reva.web.validators.DatumNietInToekomstValidator;

/**
 * Reva hoofdpagina voor de zoek functionaliteit.
 * 
 */
@PaxWicketMountPoint( mountPoint = "/zoeken" )
@ShiroSecurityConstraint( constraint = ShiroConstraint.HasPermission, value = "zoeken:basis" )
public class ZoekenPage extends RevaPage {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  private static final String PARAM_SUBMIT = "submit";

  private static final String FIELD_STAM = "stamId";
  private static final String FIELD_DOSSIERNUMMER = "dossierNummer";
  private static final String FIELD_BSN = "bsn";
  private static final String FIELD_WIJZIGINGDATUM = "wijzigingDatum";
  private static final String FIELD_WOONPLAATS = "woonPlaats";
  private static final String FIELD_OPENBARERUIMTE = "openbareRuimte";
  private static final String FIELD_POSTCODE = "postCode";
  private static final String FIELD_HUISNUMMER = "huisNummer";
  private static final String FIELD_HUISLETTER = "huisLetter";
  private static final String FIELD_HUISNUMMER_TOEVOEGING = "huisNummerToevoeging";

  private static final String FIELD_VANAFDATUM = "vanafDatum";
  private static final String FIELD_TOTENMETDATUM = "totenmetDatum";

  private static final List<String> VALID_PARAMS = new ArrayList<>();

  static {
    VALID_PARAMS.add( FIELD_DOSSIERNUMMER );
    VALID_PARAMS.add( FIELD_BSN );
    VALID_PARAMS.add( FIELD_VANAFDATUM );
    VALID_PARAMS.add( FIELD_TOTENMETDATUM );
    VALID_PARAMS.add( FIELD_WOONPLAATS );
    VALID_PARAMS.add( FIELD_OPENBARERUIMTE );
    VALID_PARAMS.add( FIELD_POSTCODE );
    VALID_PARAMS.add( FIELD_HUISNUMMER );
  }

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final List<String> COLUMN_NAMES = new ArrayList<String>();
  private static final List<String> COLUMN_NAMES_WITHOUT_BSN = new ArrayList<String>();

  static {
    COLUMN_NAMES.add( FIELD_DOSSIERNUMMER );
    COLUMN_NAMES.add( FIELD_BSN );
    COLUMN_NAMES.add( FIELD_WIJZIGINGDATUM );
    COLUMN_NAMES.add( FIELD_WOONPLAATS );
    COLUMN_NAMES.add( FIELD_OPENBARERUIMTE );
    COLUMN_NAMES.add( FIELD_POSTCODE );
    COLUMN_NAMES.add( FIELD_HUISNUMMER );
    COLUMN_NAMES.add( FIELD_HUISLETTER );
    COLUMN_NAMES.add( FIELD_HUISNUMMER_TOEVOEGING );

    COLUMN_NAMES_WITHOUT_BSN.addAll( COLUMN_NAMES );
    COLUMN_NAMES_WITHOUT_BSN.remove( FIELD_BSN );
  }

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  @SuppressWarnings( "serial" )
  public ZoekenPage( PageParameters parameters ) throws Exception {
    boolean showBsn = SecurityUtils.getSubject().isPermitted( RevaPermissions.ZOEKEN_BSN.getStringPermission() );
    boolean showResults = isSubmittable( parameters );
    PageParameters filtered = filteredParameters( parameters );

    // Scherm deel voor het tonen van zoek resultaten.
    final PageableResultSetPanel resultatenPanel = new PageableResultSetPanel( "resultaten", (showBsn ? COLUMN_NAMES
            : COLUMN_NAMES_WITHOUT_BSN), null ) {
      @Override
      protected void onClick( Item<SolutionMapping> item ) {
        SolutionMapping mapping = item.getModelObject();
        String stamId = ResourceUtils.getResourceValue( mapping.get( FIELD_STAM ) );
        PageParameters pageParameters = new PageParameters();
        pageParameters.add( EersteVerblijfadresPage.STAM_PARAM_NAME, stamId );
        setResponsePage( EersteVerblijfadresPage.class, pageParameters );
      }
    };
    resultatenPanel.setParameters( buildQueryParameters( filtered ) );
    resultatenPanel.setVisible( showResults );
    resultatenPanel.setResultQueryName( "zoekInReva" );
    resultatenPanel.setCountQueryName( "zoekInRevaCount" );
    add( resultatenPanel );

    PageParameters queryParameters = new PageParameters( filtered );
    queryParameters.add( QueryAsXlsResource.QUERY_NAME_PARAM, "zoeken" );
    final ResourceLink downloadLink = new ResourceLink( "download", new QueryAsXlsResourceReference(), queryParameters );
    downloadLink.setVisible( showResults );
    add( downloadLink );

    // Scherm deel voor de invoer van zoek criteria.
    final ZoekCriteria zoekCriteria = new ZoekCriteria();
    if( showResults ) {
      populateZoekCriteria( zoekCriteria, filtered );
    }
    final DateTextField vanafDatum = new FormattedDateTextField( FIELD_VANAFDATUM );
    final DateTextField totenmetDatum = new FormattedDateTextField( FIELD_TOTENMETDATUM );
    Form<ZoekCriteria> form = new Form<ZoekCriteria>( "zoekFormulier" ) {
      @Override
      public void onSubmit() {
        PageParameters parameters = new PageParameters();
        addParameter( parameters, FIELD_DOSSIERNUMMER, zoekCriteria.getDossierNummer() );
        addParameter( parameters, FIELD_BSN, zoekCriteria.getBsn() );
        addParameter( parameters, FIELD_VANAFDATUM, convertDatum( vanafDatum, zoekCriteria.getVanafDatum() ) );
        addParameter( parameters, FIELD_TOTENMETDATUM, convertDatum( totenmetDatum, zoekCriteria.getTotenmetDatum() ) );
        addParameter( parameters, FIELD_WOONPLAATS, zoekCriteria.getWoonPlaats() );
        addParameter( parameters, FIELD_OPENBARERUIMTE, zoekCriteria.getOpenbareRuimte() );
        addParameter( parameters, FIELD_POSTCODE, zoekCriteria.getPostCode() );
        addParameter( parameters, FIELD_HUISNUMMER, zoekCriteria.getHuisNummer() );

        if( parameters.isEmpty() ) {
          parameters.add( PARAM_SUBMIT, "true" );
        }

        setResponsePage( ZoekenPage.class, parameters );
        super.onSubmit();
      }
    };

    form.setDefaultModel( new CompoundPropertyModel<ZoekCriteria>( zoekCriteria ) );
    form.add( borderize( "dossierNummerFeedback", new CijfersTextField( FIELD_DOSSIERNUMMER ).setRequired( false ) ) );
    form.add( borderize( "bsnFeedback", new BsnTextField( FIELD_BSN ).setRequired( false ).setVisible( showBsn ) ) );
    form.add( borderize( "vanafDatumFeedback", vanafDatum.setRequired( false ) ) );
    form.add( borderize( "totenmetDatumFeedback", totenmetDatum.setRequired( false ) ) );
    form.add( borderize( "woonPlaatsFeedback", new WoonplaatsTextField( FIELD_WOONPLAATS ) ) );
    form.add( borderize( "openbareRuimteFeedback", new OpenbareruimteTextField( FIELD_OPENBARERUIMTE,
            "woonPlaatsFeedback:woonPlaatsFeedback_body:woonPlaats" ) ) );
    form.add( borderize( "postCodeFeedback", new PostcodeTextField( FIELD_POSTCODE ).setRequired( false ) ) );
    form.add( borderize( "huisNummerFeedback", new CijfersTextField( FIELD_HUISNUMMER ).setRequired( false ) ) );
    add( form );

    // Datum validatie
    vanafDatum.add( DatumNietInToekomstValidator.getInstance() );
    totenmetDatum.add( DatumNietInToekomstValidator.getInstance() );
    form.add( new DateRangeValidator( vanafDatum, totenmetDatum ) );
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private boolean isSubmittable( PageParameters parameters ) {
    return !parameters.isEmpty();
  }

  private PageParameters filteredParameters( PageParameters parameters ) {
    PageParameters filtered = new PageParameters();

    // Alle toegestane page parameters overnemen.
    for( String paramName : parameters.getNamedKeys() ) {
      if( VALID_PARAMS.contains( paramName ) ) {
        filtered.add( paramName, parameters.get( paramName ) );
      }
    }

    // Verwijder BSN zoekmogelijkheden indien gebruiker hiertoe niet is
    // geauthoriseerd.
    boolean maySearchOnBsn = SecurityUtils.getSubject().isPermitted( RevaPermissions.ZOEKEN_BSN.getStringPermission() );
    int bsnIndex = filtered.getPosition( FIELD_BSN );
    if( !maySearchOnBsn && bsnIndex >= 0 ) {
      filtered.remove( bsnIndex );
    }

    return filtered;
  }

  private Map<String, String> buildQueryParameters( PageParameters parameters ) {
    Map<String, String> queryParameters = new HashMap<>();

    // Default is zoeken op basis van de gemeente code, tenzij de gebruiker
    // rechten heeft om op alles te zoeken (zoeken:alles).
    if( !SecurityUtils.getSubject().isPermitted( RevaPermissions.ZOEKEN_ALLES.getStringPermission() ) ) {
      String gemeentecode = ProfileUtils.getGemeentecode( SecurityUtils.getSubject() );
      addParameter( queryParameters, "gemeenteCode", gemeentecode );
    }

    // Alle toegestane page parameters overnemen.
    for( String paramName : parameters.getNamedKeys() ) {
      addParameter( queryParameters, paramName, parameters.get( paramName ) );
    }

    return queryParameters;
  }

  private void addParameter( Map<String, String> parameters, String name, Object value ) {
    if( value != null ) {
      parameters.put( name, value.toString() );
    }
  }

  private void addParameter( PageParameters parameters, String name, Object value ) {
    if( value != null ) {
      parameters.add( name, value.toString() );
    }
  }

  private String convertDatum( DateTextField field, Date value ) {
    if( value == null ) {
      return null;
    }
    return field.getConverter( Date.class ).convertToString( value, field.getLocale() );
  }

  private void populateZoekCriteria( ZoekCriteria zoekCriteria, PageParameters parameters ) {
    Set<String> namedParametersPresent = parameters.getNamedKeys();
    IConverter<Date> converter = new GUIInputDateConverter();

    if( namedParametersPresent.contains( FIELD_DOSSIERNUMMER ) ) {
      zoekCriteria.setDossierNummer( parameters.get( FIELD_DOSSIERNUMMER ).toString() );
    }
    if( namedParametersPresent.contains( FIELD_BSN ) ) {
      zoekCriteria.setBsn( parameters.get( FIELD_BSN ).toString() );
    }
    if( namedParametersPresent.contains( FIELD_VANAFDATUM ) ) {
      zoekCriteria.setVanafDatum( converter.convertToObject( parameters.get( FIELD_VANAFDATUM ).toString(), this.getLocale() ) );
    }
    if( namedParametersPresent.contains( FIELD_TOTENMETDATUM ) ) {
      zoekCriteria
              .setTotenmetDatum( converter.convertToObject( parameters.get( FIELD_TOTENMETDATUM ).toString(), this.getLocale() ) );
    }
    if( namedParametersPresent.contains( FIELD_POSTCODE ) ) {
      zoekCriteria.setPostCode( parameters.get( FIELD_POSTCODE ).toString() );
    }
    if( namedParametersPresent.contains( FIELD_HUISNUMMER ) ) {
      zoekCriteria.setHuisNummer( parameters.get( FIELD_HUISNUMMER ).toString() );
    }
  }
}
