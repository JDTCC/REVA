package nl.overheid.stelsel.gba.reva.web.utils;

/**
 * Simpele utility class voor veel voorkomende string operaties.
 * 
 */
public final class StringUtils {

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Hide utility class constructor
   */
  private StringUtils() {
    // prevent instantiation
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  /**
   * <p>
   * Checks if a String is empty ("") or null.
   * </p>
   * 
   * @param str
   *          the String to check, may be null
   * @return <code>true</code> if the String is empty or null
   */
  public static boolean isEmpty( String str ) {
    return str == null || str.length() == 0;
  }
}
