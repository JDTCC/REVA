package nl.overheid.stelsel.gba.reva.web.components;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;

import nl.overheid.stelsel.gba.reva.web.validators.PostcodeValidator;

/**
 * A {@link TextField} for HTML5 &lt;input&gt;.
 * 
 * <p>
 * Automatically validates that the input is a valid post code.
 * </p>
 * <p>
 * <strong>Note</strong>: This component does <strong>not</strong> support
 * multiple values!
 */
public class PostcodeTextField extends TextField<String> {

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
   * @param postcode
   *          the postcode input value
   */
  public PostcodeTextField( String id, final String postcode ) {
    this( id, new Model<String>( postcode ) );
  }

  /**
   * Construct.
   * 
   * @param id
   *          see Component
   * @param model
   *          the model
   */
  public PostcodeTextField( String id, IModel<String> model ) {
    this( id, model, PostcodeValidator.getInstance() );
  }

  /**
   * Construct.
   * 
   * @param id
   *          see Component
   */
  public PostcodeTextField( String id ) {
    this( id, null, PostcodeValidator.getInstance() );
  }

  /**
   * Construct.
   * 
   * @param id
   *          the component id
   * @param model
   *          the input value
   * @param postcodeValidator
   *          the validator that will check the correctness of the input value
   */
  public PostcodeTextField( String id, IModel<String> model, IValidator<String> postcodeValidator ) {
    super( id, model, String.class );

    add( postcodeValidator );
  }
}
