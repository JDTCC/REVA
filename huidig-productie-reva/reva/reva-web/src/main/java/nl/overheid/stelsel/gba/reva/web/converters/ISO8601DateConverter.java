package nl.overheid.stelsel.gba.reva.web.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.util.convert.IConverter;

/**
 * Wicket converter to convert to and from the standard UUID.
 * 
 */
public class ISO8601DateConverter implements IConverter<Date> {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final int COLON_POSITION_FROM_END = 3;

  private static final SimpleDateFormat ISO8601FORMAT = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ" );

  // -------------------------------------------------------------------------
  // Class Attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = -8232527767404314237L;

  // -------------------------------------------------------------------------
  // Implementing IConverter
  // -------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized Date convertToObject( String value, Locale locale ) {
    String date = value;
    if( ":".equals( value.substring( value.length() - COLON_POSITION_FROM_END, value.length() - 2 ) ) ) {
      // convert YYYYMMDDTHH:mm:ss+HH:00 into YYYYMMDDTHH:mm:ss+HH00
      // - note the added colon for the Timezone
      date = value.substring( 0, value.length() - COLON_POSITION_FROM_END ) + value.substring( value.length() - 2 );
    }
    try {
      return ISO8601FORMAT.parse( date );
    } catch( ParseException e ) {
      throw new IllegalArgumentException( "Value '" + value + "' not according to ISO 8601!  ", e );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized String convertToString( Date value, Locale locale ) {
    String result = ISO8601FORMAT.format( value );
    // convert YYYYMMDDTHH:mm:ss+HH00 into YYYYMMDDTHH:mm:ss+HH:00
    // - note the added colon for the Timezone
    result = result.substring( 0, result.length() - 2 ) + ":" + result.substring( result.length() - 2 );
    return result;
  }
}
