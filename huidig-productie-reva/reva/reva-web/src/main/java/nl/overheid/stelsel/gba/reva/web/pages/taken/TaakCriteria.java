package nl.overheid.stelsel.gba.reva.web.pages.taken;

import java.util.Date;

/**
 * Simple model voor de taak zoek criteria van de reva informatie.
 * 
 */
public class TaakCriteria {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private String naam;
  private Date vanafDatum;
  private Date totenmetDatum;
  private String postcode;
  private String huisnummer;

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public String getNaam() {
    return naam;
  }

  public void setNaam( String naam ) {
    this.naam = naam;
  }

  public Date getVanafDatum() {
    return vanafDatum;
  }

  // @Past
  public void setVanafDatum( Date vanafDatum ) {
    this.vanafDatum = vanafDatum;
  }

  public Date getTotenmetDatum() {
    return totenmetDatum;
  }

  // @Past
  public void setTotenmetDatum( Date totenmetDatum ) {
    this.totenmetDatum = totenmetDatum;
  }

  public String getPostcode() {
    return postcode;
  }

  // @Pattern(regexp = "[1-9]{1}[0-9]{3}?[A-Z]{2}")
  public void setPostcode( String postcode ) {
    this.postcode = postcode;
  }

  public String getHuisnummer() {
    return huisnummer;
  }

  public void setHuisnummer( String huisnummer ) {
    this.huisnummer = huisnummer;
  }
}
