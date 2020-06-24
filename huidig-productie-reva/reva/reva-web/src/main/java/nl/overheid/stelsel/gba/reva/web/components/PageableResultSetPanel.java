package nl.overheid.stelsel.gba.reva.web.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.clerezza.rdf.core.sparql.SolutionMapping;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.ResourceModel;

import nl.overheid.stelsel.digimelding.astore.utils.ResourceUtils;

/**
 * Pageable panel to show result set details.
 * 
 */
public abstract class PageableResultSetPanel extends Panel {

  // -------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  private static final String LISTVIEW_HEADER_ID = "headerRow";
  private static final String LISTVIEW_ROW_ID = "row";
  private static final String LISTVIEW_DATAROWS_ID = "dataRows";
  private static final String LISTVIEW_NAVIGATOR_ID = "navigator";

  private static final int DEFAULT_PAGE_SIZE = 25;

  // -------------------------------------------------------------------
  // Attributes
  // -------------------------------------------------------------------

  private final List<String> columnNames = new ArrayList<>();
  private final Map<String, String> parameters = new HashMap<String, String>();
  private int itemsPerPage = DEFAULT_PAGE_SIZE;
  private transient boolean repopulate = true;

  private String resultQueryName;
  private String countQueryName;

  // -------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------

  /**
   * Constructor.
   * 
   * @param id
   *          the identifcation for this component.
   * @param columnNames
   *          list with names of columns to be shown in this panel.
   */
  public PageableResultSetPanel( final String id, final List<String> columnNames, Map<String, String> parameters ) {
    super( id );

    storeColumns( columnNames );
    storeParameters( parameters );

    setOutputMarkupPlaceholderTag( true );
  }

  // -------------------------------------------------------------------
  // Public interface
  // -------------------------------------------------------------------

  public void setColumns( List<String> columns ) {
    storeColumns( columns );
  }

  public List<String> getColumns() {
    return Collections.unmodifiableList( columnNames );
  }

  public Map<String, String> getParameters() {
    return Collections.unmodifiableMap( parameters );
  }

  public void setParameters( Map<String, String> parameters ) {
    storeParameters( parameters );
  }

  public int getItemsPerPage() {
    return itemsPerPage;
  }

  public void setItemsPerPage( int itemsPerPage ) {
    this.itemsPerPage = itemsPerPage;
  }

  // -------------------------------------------------------------------
  // Overrides
  // -------------------------------------------------------------------

  @Override
  protected void onBeforeRender() {
    super.onBeforeRender();

    if( repopulate ) {
      populatePanel();
    }
  }

  protected abstract void onClick( final Item<SolutionMapping> item );

  // -------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------

  public String getResultQueryName() {
    return resultQueryName;
  }

  public void setResultQueryName( String resultQueryName ) {
    this.resultQueryName = resultQueryName;
  }

  public String getCountQueryName() {
    return countQueryName;
  }

  public void setCountQueryName( String countQueryName ) {
    this.countQueryName = countQueryName;
  }

  // -------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------

  private void storeColumns( List<String> columns ) {
    // Get rid of current columns.
    columnNames.clear();

    if( columns != null ) {
      // Keep new columns.
      columnNames.addAll( columns );
    }
    repopulate = true;
  }

  private void storeParameters( Map<String, String> params ) {
    // Get rid of current parameters.
    this.parameters.clear();

    if( params != null ) {
      // Keep new parameters.
      this.parameters.putAll( params );
    }
    repopulate = true;
  }

  private void populatePanel() {
    // Eerst de bestaande situatie schoon vegen.
    removeAll();

    // List viewer columns
    RepeatingView rv = new RepeatingView( LISTVIEW_HEADER_ID );
    add( rv );
    for( String columnName : columnNames ) {
      rv.add( new Label( columnName + ".header", new ResourceModel( getId() + "." + columnName, columnName ) ) );
    }

    // List viewer content
    ResultSetDataProvider dataProvider = new ResultSetDataProvider( getCountQueryName(), getResultQueryName(), parameters );
    boolean thereAreResults = dataProvider.size() > 0;
    DataView<SolutionMapping> dataView = new DataView<SolutionMapping>( LISTVIEW_DATAROWS_ID, dataProvider ) {
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem( final Item<SolutionMapping> item ) {
        item.add( new AjaxEventBehavior( "onclick" ) {
          private static final long serialVersionUID = 4493898120986691862L;

          @Override
          protected void onEvent( AjaxRequestTarget target ) {
            onClick( item );
          }
        } );
        RepeatingView repeatingView = new RepeatingView( LISTVIEW_ROW_ID );

        // Retrieve data for all columns.
        SolutionMapping mapping = item.getModelObject();
        for( String columnName : columnNames ) {
          String value = ResourceUtils.getResourceValue( mapping.get( columnName ) );
          repeatingView.add( new Label( repeatingView.newChildId(), value ) );
        }
        item.add( repeatingView );
      }
    };
    dataView.setItemsPerPage( itemsPerPage );
    add( dataView );

    // List viewer paging navigator
    add( new AjaxPagingNavigator( LISTVIEW_NAVIGATOR_ID, dataView ).setVisible( thereAreResults ) );

    add( new Label( "leeg", new ResourceModel( getId() + ".leeg" ) ).setVisible( !thereAreResults ) );

    // Just finished populating, no need to re-populate until
    // something changes.
    repopulate = false;
  }
}
