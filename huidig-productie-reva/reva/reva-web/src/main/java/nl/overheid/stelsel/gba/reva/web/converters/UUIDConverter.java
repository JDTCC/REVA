package nl.overheid.stelsel.gba.reva.web.converters;

import java.util.Locale;
import java.util.UUID;

import org.apache.wicket.util.convert.IConverter;

/**
 * Wicket converter to convert to and from the standard UUID.
 * 
 */
public class UUIDConverter implements IConverter<UUID> {

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
  public UUID convertToObject( String value, Locale locale ) {
    return UUID.fromString( value );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String convertToString( UUID value, Locale locale ) {
    return value.toString();
  }

}
