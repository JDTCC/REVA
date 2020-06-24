package nl.overheid.stelsel.gba.reva.web.pages.rapportage;

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

import nl.overheid.stelsel.gba.reva.web.components.CijfersTextField;
import nl.overheid.stelsel.gba.reva.web.components.FormattedDateTextField;
import nl.overheid.stelsel.gba.reva.web.components.PageableResultSetPanel;
import nl.overheid.stelsel.gba.reva.web.converters.GUIInputDateConverter;
import nl.overheid.stelsel.gba.reva.web.pages.RevaPage;
import nl.overheid.stelsel.gba.reva.web.resources.QueryAsXlsResource;
import nl.overheid.stelsel.gba.reva.web.resources.QueryAsXlsResourceReference;
import nl.overheid.stelsel.gba.reva.web.security.RevaPermissions;
import nl.overheid.stelsel.gba.reva.web.utils.ProfileUtils;
import nl.overheid.stelsel.gba.reva.web.validators.DateRangeValidator;
import nl.overheid.stelsel.gba.reva.web.validators.DatumNietInToekomstValidator;

/**
 * Eenvoudige rapportage pagina van Reva. Deze pagina toont het aantal
 * registraties dat voor doelgemeenten zijn aangemaakt.
 * 
 */
@PaxWicketMountPoint( mountPoint = "/rapportage/voorDoelGemeente" )
@ShiroSecurityConstraint( constraint = ShiroConstraint.HasPermission, value = "query:rapportageVoorDoelGemeente" )
public class VoorGemeenteRapportagePage extends RevaPage {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  private static final String PARAM_SUBMIT = "submit";

  private static final String FIELD_GEMEENTECODE = "gemeenteCode";
  private static final String FIELD_VANAFDATUM = "vanafDatum";
  private static final String FIELD_TOTENMETDATUM = "totenmetDatum";
  private static final String FIELD_GEMEENTE = "gemeente";
  private static final String FIELD_AANTAL_REGISTRATIES = "aantalRegistraties";

  private static final List<String> VALID_PARAMS = new ArrayList<>();

  static {
    VALID_PARAMS.add( FIELD_GEMEENTECODE );
    VALID_PARAMS.add( FIELD_VANAFDATUM );
    VALID_PARAMS.add( FIELD_TOTENMETDATUM );
  }

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final List<String> COLUMN_NAMES = new ArrayList<String>();

  static {
    COLUMN_NAMES.add( FIELD_GEMEENTECODE );
    COLUMN_NAMES.add( FIELD_GEMEENTE );
    COLUMN_NAMES.add( FIELD_AANTAL_REGISTRATIES );
  }

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  @SuppressWarnings( "serial" )
  public VoorGemeenteRapportagePage( PageParameters parameters ) throws Exception {
    boolean showGemeenteCode = SecurityUtils.getSubject().isPermitted( RevaPermissions.ZOEKEN_ALLES.getStringPermission() );
    boolean showResults = isSubmittable( parameters );
    PageParameters filtered = filteredParameters( parameters );

    // Scherm deel voor het tonen van zoek resultaten.
    final PageableResultSetPanel resultatenPanel = new PageableResultSetPanel( "resultaten", COLUMN_NAMES, null ) {
      @Override
      protected void onClick( Item<SolutionMapping> item ) {
        // Not clickable.
      }
    };
    resultatenPanel.setParameters( buildQueryParameters( filtered ) );
    resultatenPanel.setVisible( showResults );
    resultatenPanel.setResultQueryName( "rapportageVoorDoelGemeente" );
    resultatenPanel.setCountQueryName( "rapportageVoorDoelGemeenteCount" );
    add( resultatenPanel );

    PageParameters queryParameters = new PageParameters( filtered );
    queryParameters.add( QueryAsXlsResource.QUERY_NAME_PARAM, "rapportageVoorDoelGemeente" );
    final ResourceLink downloadLink = new ResourceLink( "download", new QueryAsXlsResourceReference(), queryParameters );
    downloadLink.setVisible( showResults );
    add( downloadLink );

    // Scherm deel voor de invoer van zoek criteria.
    final RapportageCriteria rapportageCriteria = new RapportageCriteria();
    if( showResults ) {
      populateRapportageCriteria( rapportageCriteria, filtered );
    }
    final DateTextField vanafDatum = new FormattedDateTextField( FIELD_VANAFDATUM );
    final DateTextField totenmetDatum = new FormattedDateTextField( FIELD_TOTENMETDATUM );
    Form<RapportageCriteria> form = new Form<RapportageCriteria>( "rapportageFormulier" ) {
      @Override
      public void onSubmit() {
        PageParameters parameters = new PageParameters();
        addParameter( parameters, FIELD_GEMEENTECODE, rapportageCriteria.getGemeenteCode() );
        addParameter( parameters, FIELD_VANAFDATUM, convertDatum( vanafDatum, rapportageCriteria.getVanafDatum() ) );
        addParameter( parameters, FIELD_TOTENMETDATUM, convertDatum( totenmetDatum, rapportageCriteria.getTotenmetDatum() ) );

        if( parameters.isEmpty() ) {
          parameters.add( PARAM_SUBMIT, "true" );
        }

        setResponsePage( VoorGemeenteRapportagePage.class, parameters );
        super.onSubmit();
      }
    };

    form.setDefaultModel( new CompoundPropertyModel<RapportageCriteria>( rapportageCriteria ) );
    form.add( borderize( "gemeenteCodeFeedback",
            new CijfersTextField( FIELD_GEMEENTECODE ).setRequired( false ).setVisible( showGemeenteCode ) ) );
    form.add( borderize( "vanafDatumFeedback", vanafDatum.setRequired( true ) ) );
    form.add( borderize( "totenmetDatumFeedback", totenmetDatum.setRequired( true ) ) );
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

    // Forceer gemeentcode indien gebruiker niet vrij mag zoeken.
    boolean magAllesZoeken = SecurityUtils.getSubject().isPermitted( RevaPermissions.ZOEKEN_ALLES.getStringPermission() );
    if( !magAllesZoeken ) {
      String gemeentecode = ProfileUtils.getGemeentecode( SecurityUtils.getSubject() );
      filtered.add( FIELD_GEMEENTECODE, gemeentecode );
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

  private void populateRapportageCriteria( RapportageCriteria rapportageCriteria, PageParameters parameters ) {
    Set<String> namedParametersPresent = parameters.getNamedKeys();
    IConverter<Date> converter = new GUIInputDateConverter();

    if( namedParametersPresent.contains( FIELD_GEMEENTECODE ) ) {
      rapportageCriteria.setGemeenteCode( parameters.get( FIELD_GEMEENTECODE ).toString() );
    }
    if( namedParametersPresent.contains( FIELD_VANAFDATUM ) ) {
      rapportageCriteria
              .setVanafDatum( converter.convertToObject( parameters.get( FIELD_VANAFDATUM ).toString(), this.getLocale() ) );
    }
    if( namedParametersPresent.contains( FIELD_TOTENMETDATUM ) ) {
      rapportageCriteria.setTotenmetDatum( converter.convertToObject( parameters.get( FIELD_TOTENMETDATUM ).toString(),
              this.getLocale() ) );
    }
  }
}
