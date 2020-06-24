package nl.overheid.stelsel.gba.reva.web.components;

import java.util.HashMap;

import org.apache.clerezza.rdf.core.Resource;
import org.apache.clerezza.rdf.core.sparql.SolutionMapping;
import org.apache.clerezza.rdf.core.sparql.query.Variable;
import org.apache.wicket.model.util.GenericBaseModel;

public class SolutionMappingModel extends GenericBaseModel<SolutionMapping> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Creates an empty model.
   */
  public SolutionMappingModel() {
  }

  public SolutionMappingModel( SolutionMapping mapping ) {
    setObject( mapping );
  }

  // -------------------------------------------------------------------------
  // Overrides
  // -------------------------------------------------------------------------

  /** {@inheritDoc} */
  @Override
  protected SolutionMapping createSerializableVersionOf( SolutionMapping object ) {
    return new SerializableSolutionMapping( object );
  }

  // -------------------------------------------------------------------------
  //
  // -------------------------------------------------------------------------

  private static class SerializableSolutionMapping extends HashMap<Variable, Resource> implements SolutionMapping {

    private static final long serialVersionUID = 1L;

    public SerializableSolutionMapping( SolutionMapping mapping ) {
      super( mapping );
    }

    @Override
    public Resource get( String name ) {
      return get( new Variable( name ) );
    }
  }
}
