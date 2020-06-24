package nl.overheid.stelsel.gba.reva.web.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * Validator om te controleren of invoer een geldige bsn nummer is.
 * 
 */
public class BsnValidator implements IValidator<String> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final int BSN_BOVENGRENS = 999999999;

  private static final int BSN_ONDERGRENS = 9999999;

  private static final long serialVersionUID = 1L;

  /** singleton instance */
  private static final BsnValidator INSTANCE = new BsnValidator();

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Retrieves the singleton instance of <code>BsnValidator</code>.
   * 
   * @return the singleton instance of <code>BsnValidator</code>
   */
  public static BsnValidator getInstance() {
    return INSTANCE;
  }

  // -------------------------------------------------------------------------
  // Protected interface
  // -------------------------------------------------------------------------

  /**
   * Protected constructor to force use of static singleton accessor. Override
   * this constructor to implement resourceKey(Component).
   */
  protected BsnValidator() {
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
    int bsn = 0;

    try {
      bsn = Integer.parseInt( validatable.getValue() );
    } catch( NumberFormatException e ) {
      // Negeren. bsn = 0 faalt toch op de 11-proef.
    }

    if( !isValidBSN( bsn ) ) {
      ValidationError error = new ValidationError( this );
      validatable.error( decorate( error, validatable ) );
    }
  }

  /** Validate BSN according to http://nl.wikipedia.org/wiki/Burgerservicenummer */
  private boolean isValidBSN( int candidate ) {
    int candidateRemainder = candidate;
    if( candidateRemainder <= BSN_ONDERGRENS || candidateRemainder > BSN_BOVENGRENS ) {
      return false;
    }
    int sum = -1 * candidateRemainder % 10;

    for( int multiplier = 2; candidateRemainder > 0; multiplier++ ) {
      candidateRemainder /= 10;
      int val = candidateRemainder % 10;
      sum += multiplier * val;
    }

    return sum != 0 && sum % 11 == 0;
  }
}
