package nl.overheid.stelsel.digimelding.astore.impl;

import java.util.UUID;

import org.apache.clerezza.rdf.core.UriRef;

/**
 * 
 */
public final class UUID2UriRefConverter {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final String URN_UUID = "urn:uuid:";

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  private UUID2UriRefConverter() {
    // prevent instantation
  }
  
  // -------------------------------------------------------------------------
  // Static interface
  // -------------------------------------------------------------------------

  /**
   * Converts a UUID into an UriRef.
   * 
   * @param id the UUID to convert.
   * @return UriRef
   */
  public static UriRef toUriRef(UUID id) {
    return new UriRef(URN_UUID + id.toString());
  }

  /**
   * Converts an UriRef to a UUID.
   * 
   * @param uriRef the UriRef to convert.
   * @return UUID
   */
  public static UUID toUUID(UriRef uriRef) {
    if (!uriRef.getUnicodeString().startsWith(URN_UUID)) {
      throw new IllegalArgumentException("UriRef does not contain a UUID");
    }

    return UUID.fromString(uriRef.getUnicodeString().substring(URN_UUID.length()));
  }
}
