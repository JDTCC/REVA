package nl.overheid.stelsel.gba.reva.web.validators;

import java.util.regex.Pattern;

import org.apache.wicket.validation.validator.PatternValidator;

/**
 * Validator om te controleren of invoer alleen uit letters bestaat.
 * 
 */
public class CijfersEnOfLettersValidator extends PatternValidator {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  private static final String REGEX_PATTERN = "^[a-zA-Z0-9]*$";

  /** singleton instance */
  private static final CijfersEnOfLettersValidator INSTANCE = new CijfersEnOfLettersValidator();

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Retrieves the singleton instance of <code>LettersValidator</code>.
   * 
   * @return the singleton instance of <code>LettersValidator</code>
   */
  public static CijfersEnOfLettersValidator getInstance() {
    return INSTANCE;
  }

  // -------------------------------------------------------------------------
  // Protected interface
  // -------------------------------------------------------------------------

  /**
   * Protected constructor to force use of static singleton accessor. Override
   * this constructor to implement resourceKey(Component).
   */
  protected CijfersEnOfLettersValidator() {
    super( REGEX_PATTERN, Pattern.CASE_INSENSITIVE );
  }

}
