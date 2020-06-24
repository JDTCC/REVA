package nl.overheid.stelsel.gba.reva.web.validators;

import java.util.regex.Pattern;

import org.apache.wicket.validation.validator.PatternValidator;

/**
 * Validator om te controleren of invoer alleen uit letters bestaat.
 * 
 */
public class LettersValidator extends PatternValidator {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  private static final String REGEX_PATTERN = "^[a-zA-Z]*$";

  /** singleton instance */
  private static final LettersValidator INSTANCE = new LettersValidator();

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Retrieves the singleton instance of <code>LettersValidator</code>.
   * 
   * @return the singleton instance of <code>LettersValidator</code>
   */
  public static LettersValidator getInstance() {
    return INSTANCE;
  }

  // -------------------------------------------------------------------------
  // Protected interface
  // -------------------------------------------------------------------------

  /**
   * Protected constructor to force use of static singleton accessor. Override
   * this constructor to implement resourceKey(Component).
   */
  protected LettersValidator() {
    super( REGEX_PATTERN, Pattern.CASE_INSENSITIVE );
  }

}
