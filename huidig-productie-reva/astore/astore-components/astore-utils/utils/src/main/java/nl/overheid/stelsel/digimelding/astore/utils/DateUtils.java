package nl.overheid.stelsel.digimelding.astore.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simpele utility class voor het converteren van date object naar ISO 8601
 * string representatie van die objecten.
 * 
 */
public final class DateUtils {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final SimpleDateFormat ISO8601FORMAT = new SimpleDateFormat(
      "yyyy-MM-dd'T'HH:mm:ssZ");

  private static final SimpleDateFormat EEJJMMDD = new SimpleDateFormat(
      "yyyyMMdd");

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Hide utility class constructor
   */
  private DateUtils() {
    // prevent instantiation
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  /**
   * Converteer de gegeven date naar een ISO 8601 string representatie van die
   * datum.
   * 
   * @param date
   *            te converteren date
   * @return String met de string representatie van de gegeven date.
   */
  public static synchronized String getDateAsISO8601String(Date date) {
    String result = ISO8601FORMAT.format(date);
    // convert YYYYMMDDTHH:mm:ss+HH00 into YYYYMMDDTHH:mm:ss+HH:00
    // - note the added colon for the Timezone
    result = result.substring(0, result.length() - 2) + ":"
        + result.substring(result.length() - 2);
    return result;
  }

  /**
   * Converteer de gegeven date naar een Integer formaat zoals de TMV deze
   * gebruikt.
   * 
   * @param date
   *            te converteren date
   * @return Integer met de nummerieke waarde voor de gegeven date.
   */
  public static synchronized Integer getDateEEJJMMDD(Date date) {
    return Integer.valueOf(EEJJMMDD.format(date));
  }
}
