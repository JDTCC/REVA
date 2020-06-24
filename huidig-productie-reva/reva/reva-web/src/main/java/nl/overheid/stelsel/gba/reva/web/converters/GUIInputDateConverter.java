package nl.overheid.stelsel.gba.reva.web.converters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.wicket.util.convert.converter.DateConverter;

/**
 * Wicket converter to convert GUI date elements.
 * 
 */
public class GUIInputDateConverter extends DateConverter {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  private static final String DATUM_PATTERN = "yyyy-MM-dd";

  // -------------------------------------------------------------------------
  // DateConverter overrides
  // -------------------------------------------------------------------------

  /**
   * @see org.apache.wicket.util.convert.converter.DateConverter#getDateFormat(java.util.Locale)
   */
  @Override
  public DateFormat getDateFormat( Locale locale ) {
    Locale tmpLocale = locale;
    if( tmpLocale == null ) {
      tmpLocale = Locale.getDefault();
    }
    return new SimpleDateFormat( DATUM_PATTERN, tmpLocale );
  }
}
