package nl.overheid.stelsel.gba.reva.web.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

import nl.overheid.stelsel.gba.reva.web.locators.BagServiceLocator;

/**
 * Validator om te controleren of string invoer een bestaande woonplaats is.
 * 
 */
public class WoonplaatsValidator implements IValidator<String> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  /** singleton instance */
  private static final WoonplaatsValidator INSTANCE = new WoonplaatsValidator();

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Retrieves the singleton instance of <code>WoonplaatsValidator</code>.
   * 
   * @return the singleton instance of <code>WoonplaatsValidator</code>
   */
  public static WoonplaatsValidator getInstance() {
    return INSTANCE;
  }

  // -------------------------------------------------------------------------
  // Protected interface
  // -------------------------------------------------------------------------

  /**
   * Protected constructor to force use of static singleton accessor. Override
   * this constructor to implement resourceKey(Component).
   */
  protected WoonplaatsValidator() {
  }

  /**
   * Allows subclasses to decorate reported errors
   * 
   * @param error
   * @param validatable
   * @return decorated error
   */
  protected ValidationError decorate( ValidationError error, IValidatable<String> validatable ) {
    return error;
  }

  // -------------------------------------------------------------------------
  // Implementing IValidator
  // -------------------------------------------------------------------------

  @Override
  public void validate( IValidatable<String> validatable ) {
    if( !BagServiceLocator.getService().isWoonplaatsnaam( validatable.getValue() ) ) {
      ValidationError error = new ValidationError( this );
      validatable.error( decorate( error, validatable ) );
    }
  }
}
