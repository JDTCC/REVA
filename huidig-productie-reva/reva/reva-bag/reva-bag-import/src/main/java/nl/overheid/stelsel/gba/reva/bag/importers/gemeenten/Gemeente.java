package nl.overheid.stelsel.gba.reva.bag.importers.gemeenten;

/**
 * Object voor gebruikte gemeente gegevens uit BZK tabel 33 (Gemeenten). Deze
 * tabel is te vinden op:
 * 
 * http://www.bprbzk.nl/BRP/Informatiebank/Procedures/Landelijke_tabellen
 * 
 */
public class Gemeente {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private String code;
  private String naam;
  private String nieuweCode;

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public String getCode() {
    return code;
  }

  public void setCode( String code ) {
    this.code = code;
  }

  public String getNaam() {
    return naam;
  }

  public void setNaam( String naam ) {
    this.naam = naam;
  }

  public String getNieuweCode() {
    return nieuweCode;
  }

  public void setNieuweCode( String nieuweCode ) {
    this.nieuweCode = nieuweCode;
  }
}
