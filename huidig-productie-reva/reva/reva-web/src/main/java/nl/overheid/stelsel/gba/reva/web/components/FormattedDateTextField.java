package nl.overheid.stelsel.gba.reva.web.components;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.convert.converter.DateConverter;

/**
 * A {@link TextField} for HTML5 &lt;input&gt;.
 * 
 * <p>
 * Automatically validates that the input is a date conforming to a regexp based format.
 * </p>
 * <p>
 * <strong>Note</strong>: This component does <strong>not</strong> support multiple values!
 */
public class FormattedDateTextField extends DateTextField {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final String DATUM_PATTERN = "yyyy-MM-dd";
  private static final String REGEX_PATTERN = "^(\\d{4})-(\\d{2})-(\\d{2})$";
  private static final Pattern pattern = Pattern.compile(REGEX_PATTERN);

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  // No need to be serializable, gets (re)created on first use.
  transient private IConverter<Date> converter = null;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public FormattedDateTextField(String id) {
    super(id, DATUM_PATTERN);
  }

  // -------------------------------------------------------------------------
  // Component overrides
  // -------------------------------------------------------------------------

  @SuppressWarnings("unchecked")
  @Override
  public <C> IConverter<C> getConverter(final Class<C> type) {
    if (Date.class.isAssignableFrom(type)) {
      return (IConverter<C>) getOwnConverter(super.getConverter(Date.class));
    }

    return super.getConverter(type);
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  @SuppressWarnings("unchecked")
  private IConverter<Date> getOwnConverter(final IConverter<Date> delegate) {
    if (converter == null) {
      converter =
          (IConverter<Date>) Proxy.newProxyInstance(FormattedDateTextField.class.getClassLoader(),
              new Class[] {IConverter.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                  // Only perform regexp test on 'convertToString' method
                  if ("convertToObject".equals(method.getName())) {
                    FormattedDateTextField.this.checkDateRegexp(delegate, (String) args[0]);
                    // Overflow into default behavior.
                  }
                  return method.invoke(delegate, args);
                }
              });
    }

    return converter;
  }

  /**
   * Throws an exception if the given date as string does not correspond to the requested format.
   * 
   * @param date the string to check
   */
  private void checkDateRegexp(IConverter<Date> converter, String date) {
    if (date != null && !"".equals(date) && !pattern.matcher(date).matches()) {
      ConversionException ex = new ConversionException("Invalid date format");
      ex.setFormat(((DateConverter)converter).getDateFormat(null));
      throw ex;
    }
  }
}
