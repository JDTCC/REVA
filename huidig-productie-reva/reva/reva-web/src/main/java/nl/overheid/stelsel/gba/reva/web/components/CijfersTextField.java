package nl.overheid.stelsel.gba.reva.web.components;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;

import nl.overheid.stelsel.gba.reva.web.validators.CijfersValidator;

/**
 * A {@link TextField} for HTML5 &lt;input&gt;.
 * 
 * <p>
 * Automatically validates that the input is a only ciphers.
 * </p>
 * <p>
 * <strong>Note</strong>: This component does <strong>not</strong> support
 * multiple values!
 */
public class CijfersTextField extends TextField<String> {

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
   * @param cijfersInvoer
   *          the cijfers input value
   */
  public CijfersTextField( String id, final String cijfersInvoer ) {
    this( id, new Model<String>( cijfersInvoer ) );
  }

  /**
   * Construct.
   * 
   * @param id
   *          see Component
   * @param model
   *          the model
   */
  public CijfersTextField( String id, IModel<String> model ) {
    this( id, model, CijfersValidator.getInstance() );
  }

  /**
   * Construct.
   * 
   * @param id
   *          see Component
   */
  public CijfersTextField( String id ) {
    this( id, null, CijfersValidator.getInstance() );
  }

  /**
   * Construct.
   * 
   * @param id
   *          the component id
   * @param model
   *          the input value
   * @param cijfersValidator
   *          the validator that will check the correctness of the input value
   */
  public CijfersTextField( String id, IModel<String> model, IValidator<String> cijfersValidator ) {
    super( id, model, String.class );

    add( cijfersValidator );
  }
}
