package nl.overheid.stelsel.gba.reva.web.validators;

import java.util.Date;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

public class DateRangeValidator extends AbstractFormValidator {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  /** form components to be checked. */
  private final FormComponent<Date>[] components;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Construct.
   * 
   * @param formComponent1
   *          a form component
   * @param rangeEnd
   *          a form component
   */
  @SuppressWarnings( "unchecked" )
  public DateRangeValidator( FormComponent<Date> rangeStart, FormComponent<Date> rangeEnd ) {
    if( rangeStart == null ) {
      throw new IllegalArgumentException( "argument rangeStart cannot be null" );
    }
    if( rangeEnd == null ) {
      throw new IllegalArgumentException( "argument endStart cannot be null" );
    }
    components = new FormComponent[] { rangeStart, rangeEnd };
  }

  // -------------------------------------------------------------------------
  // Implementing IFormValidator
  // -------------------------------------------------------------------------

  @Override
  public FormComponent<?>[] getDependentFormComponents() {
    return components;
  }

  @Override
  public void validate( Form<?> form ) {
    // we have a choice to validate the type converted values or the raw
    // input values, we validate the type converted values.
    final Date rangeStart = components[0].getConvertedInput();
    final Date rangeEnd = components[1].getConvertedInput();

    if( rangeStart != null && rangeEnd != null && rangeStart.after( rangeEnd ) ) {
      error( components[1] );
    }
  }

}
