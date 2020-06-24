package nl.overheid.stelsel.gba.reva.web.components;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;

import nl.overheid.stelsel.gba.reva.web.validators.BsnValidator;

/**
 * A {@link TextField} for HTML5 &lt;input&gt;.
 * 
 * <p>
 * Automatically validates that the input is a Bsn.
 * </p>
 * <p>
 * <strong>Note</strong>: This component does <strong>not</strong> support
 * multiple values!
 */
public class BsnTextField extends TextField<String> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Construct.
   * 
   * @param id
   *          component id
   * @param bsnInvoer
   *          the bsn input value
   */
  public BsnTextField( String id, final String bsnInvoer ) {
    this( id, new Model<String>( bsnInvoer ) );
  }

  /**
   * Construct.
   * 
   * @param id
   *          see Component
   * @param model
   *          the model
   */
  public BsnTextField( String id, IModel<String> model ) {
    this( id, model, BsnValidator.getInstance() );
  }

  /**
   * Construct.
   * 
   * @param id
   *          see Component
   */
  public BsnTextField( String id ) {
    this( id, null, BsnValidator.getInstance() );
  }

  /**
   * Construct.
   * 
   * @param id
   *          the component id
   * @param model
   *          the input value
   * @param bsnValidator
   *          the validator that will check the correctness of the input value
   */
  public BsnTextField( String id, IModel<String> model, IValidator<String> bsnValidator ) {
    super( id, model );

    add( bsnValidator );
  }
}
