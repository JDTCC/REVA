package nl.overheid.stelsel.digimelding.astore.utils;

import java.util.Collections;

/**
 * Simpele utility class voor het collections.
 * 
 */
public final class CollectionUtils {

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Hide utility class constructor
   */
  private CollectionUtils() {
    // prevent instantiation
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  /**
   * Geeft een lege collection indien de gegeven collection null is.
   * 
   * @param iterable
   * @return 
   */
  public static <T> Iterable<T> nullIsEmpty(Iterable<T> iterable) {
    return iterable == null ? Collections.<T>emptyList() : iterable;
  }
}
