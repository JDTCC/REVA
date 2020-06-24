package nl.overheid.stelsel.gba.reva.web.validators;

import java.util.regex.Pattern;

import org.apache.wicket.validation.validator.PatternValidator;

/**
 * Validator om te controleren of invoer alleen uit cijfers bestaat.
 * 
 */
public class CijfersValidator extends PatternValidator {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  private static final String REGEX_CIJFERS = "^[0-9]*$";

  /** singleton instance */
  private static final CijfersValidator INSTANCE = new CijfersValidator();

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Retrieves the singleton instance of <code>CijfersValidator</code>.
   * 
   * @return the singleton instance of <code>CijfersValidator</code>
   */
  public static CijfersValidator getInstance() {
    return INSTANCE;
  }

  // -------------------------------------------------------------------------
  // Protected interface
  // -------------------------------------------------------------------------

  /**
   * Protected constructor to force use of static singleton accessor. Override
   * this constructor to implement resourceKey(Component).
   */
  protected CijfersValidator() {
    super( REGEX_CIJFERS, Pattern.CASE_INSENSITIVE );
  }

}
