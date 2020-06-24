package nl.overheid.stelsel.gba.reva.web.components;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;

import nl.overheid.stelsel.gba.reva.web.validators.LettersValidator;

/**
 * A {@link TextField} for HTML5 &lt;input&gt;.
 * 
 * <p>
 * Automatically validates that the input is a only letters.
 * </p>
 * <p>
 * <strong>Note</strong>: This component does <strong>not</strong> support
 * multiple values!
 */
public class LettersTextField extends TextField<String> {

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
   * @param lettersInvoer
   *          the email input value
   */
  public LettersTextField( String id, final String lettersInvoer ) {
    this( id, new Model<String>( lettersInvoer ) );
  }

  /**
   * Construct.
   * 
   * @param id
   *          see Component
   * @param model
   *          the model
   */
  public LettersTextField( String id, IModel<String> model ) {
    this( id, model, LettersValidator.getInstance() );
  }

  /**
   * Construct.
   * 
   * @param id
   *          see Component
   */
  public LettersTextField( String id ) {
    this( id, null, LettersValidator.getInstance() );
  }

  /**
   * Construct.
   * 
   * @param id
   *          the component id
   * @param model
   *          the input value
   * @param lettersValidator
   *          the validator that will check the correctness of the input value
   */
  public LettersTextField( String id, IModel<String> model, IValidator<String> lettersValidator ) {
    super( id, model, String.class );

    add( lettersValidator );
  }
}
