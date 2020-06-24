package nl.overheid.stelsel.gba.reva.web.validators;

import java.util.regex.Pattern;

import org.apache.wicket.validation.validator.PatternValidator;

/**
 * Validator om te controleren dat postcodes voldoen aan het nederlandse formaat
 * 'NNNNAA' voor postcodes. Hierbij staat N voor nummeriek en A voor alfabet.
 * 
 */
public class PostcodeValidator extends PatternValidator {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  private static final String REGEX_POSTCODE = "^[1-9][0-9]{3}[A-Za-z]{2}$";

  /** singleton instance */
  private static final PostcodeValidator INSTANCE = new PostcodeValidator();

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Retrieves the singleton instance of <code>PostcodeValidator</code>.
   * 
   * @return the singleton instance of <code>PostcodeValidator</code>
   */
  public static PostcodeValidator getInstance() {
    return INSTANCE;
  }

  // -------------------------------------------------------------------------
  // Protected interface
  // -------------------------------------------------------------------------

  /**
   * Protected constructor to force use of static singleton accessor. Override
   * this constructor to implement resourceKey(Component).
   */
  protected PostcodeValidator() {
    super( REGEX_POSTCODE, Pattern.CASE_INSENSITIVE );
  }
}
