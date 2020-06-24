package nl.overheid.stelsel.gba.reva.web.components;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.clerezza.rdf.core.sparql.ResultSet;
import org.apache.clerezza.rdf.core.sparql.SolutionMapping;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import nl.overheid.stelsel.digimelding.astore.AnnotationStoreService;
import nl.overheid.stelsel.digimelding.astore.utils.ResourceUtils;
import nl.overheid.stelsel.gba.reva.web.locators.AnnotationStoreServiceLocator;

public class ResultSetDataProvider implements IDataProvider<SolutionMapping> {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private String countQueryName;
  private String dataQueryName;
  private Map<String, String> parameters;
  private List<String> columnNames;

  // -------------------------------------------------------------------------
  // Constructor
  // -------------------------------------------------------------------------

  public ResultSetDataProvider( String countQueryName, String dataQueryName, Map<String, String> parameters ) {
    this.countQueryName = countQueryName;
    this.dataQueryName = dataQueryName;
    this.parameters = parameters;
  }

  // -------------------------------------------------------------------------
  // Implements IDataProvider
  // -------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override
  public long size() {
    Object result = null;
    try {
      result = getAnnotationStoreService().namedQuery( countQueryName, parameters );
    } catch( Exception e ) {
      throw new RuntimeException( "Executing size retrieval query failed, due to: ", e );
    }
    if( result instanceof ResultSet ) {
      ResultSet results = ResultSet.class.cast( result );
      String firstField = results.getResultVars().get( 0 );
      if( results.hasNext() ) {
        return Long.parseLong( ResourceUtils.getResourceValue( results.next().get( firstField ) ) );
      }
    } else {
      throw new RuntimeException( "Invalid size retrieval query '" + countQueryName + "' check you query." );
    }

    return 0;
  };

  /**
   * {@inheritDoc}
   */
  @Override
  public IModel<SolutionMapping> model( SolutionMapping object ) {
    return new SolutionMappingModel( object );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<? extends SolutionMapping> iterator( long first, long count ) {
    Map<String, String> pagedParameters = new HashMap<>( parameters );
    pagedParameters.put( "offset", String.valueOf(first) );
    pagedParameters.put( "limit", String.valueOf(count) );

    Object result = null;
    try {
      result = getAnnotationStoreService().namedQuery( dataQueryName, pagedParameters );
    } catch( Exception e ) {
      throw new RuntimeException( "Executing data retrieval query failed, due to: ", e );
    }
    if( result instanceof ResultSet ) {
      ResultSet results = ResultSet.class.cast( result );
      columnNames = results.getResultVars();
      return results;
    }

    throw new RuntimeException( "Invalid data retrieval query '" + dataQueryName + "', check your query!" );
  }

  // -------------------------------------------------------------------------
  // Implements IDetachable
  // -------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override
  public void detach() {
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  /**
   * Gives the names of ResultSet variable/header names.
   * 
   * @return
   */
  public List<String> getColumnNames() {
    return Collections.unmodifiableList( columnNames );
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  protected AnnotationStoreService getAnnotationStoreService() {
    return AnnotationStoreServiceLocator.getService();
  }

}
