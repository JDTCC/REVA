package nl.overheid.stelsel.gba.reva.web.validators;

import java.util.Date;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * Validator om te controleren of een ingevoerde datum niet in de toekomst ligt.
 * 
 */
public class DatumNietInToekomstValidator implements IValidator<Date> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  /** singleton instance */
  private static final DatumNietInToekomstValidator INSTANCE = new DatumNietInToekomstValidator();

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Retrieves the singleton instance of
   * <code>DatumNietInToekomstValidator</code>.
   * 
   * @return the singleton instance of <code>DatumNietInToekomstValidator</code>
   */
  public static DatumNietInToekomstValidator getInstance() {
    return INSTANCE;
  }

  // -------------------------------------------------------------------------
  // Protected interface
  // -------------------------------------------------------------------------

  /**
   * Protected constructor to force use of static singleton accessor. Override
   * this constructor to implement resourceKey(Component).
   */
  protected DatumNietInToekomstValidator() {
  }

  /**
   * Allows subclasses to decorate reported errors
   * 
   * @param error
   * @param validatable
   * @return decorated error
   */
  protected ValidationError decorate( ValidationError error, IValidatable<Date> validatable ) {
    return error;
  }

  // -------------------------------------------------------------------------
  // Implementing IValidator
  // -------------------------------------------------------------------------

  @Override
  public void validate( IValidatable<Date> validatable ) {
    Date now = new Date();

    if( now.before( validatable.getValue() ) ) {
      ValidationError error = new ValidationError( this );
      validatable.error( decorate( error, validatable ) );
    }
  }
}
