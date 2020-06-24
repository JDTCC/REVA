package nl.overheid.stelsel.gba.reva.web.pages.rapportage;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Simple model voor het rapporteren van de reva informatie.
 * 
 */
public class RapportageCriteria implements Serializable {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private String gemeenteCode;
  private Date vanafDatum;
  private Date totenmetDatum;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public RapportageCriteria() {
    vanafDatum = today();
    totenmetDatum = today();
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public String getGemeenteCode() {
    return gemeenteCode;
  }

  public void setGemeenteCode( String gemeenteCode ) {
    this.gemeenteCode = gemeenteCode;
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

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private Date today() {
    Calendar c = new GregorianCalendar();
    c.set( Calendar.HOUR_OF_DAY, 0 ); // anything 0 - 23
    c.set( Calendar.MINUTE, 0 );
    c.set( Calendar.SECOND, 0 );
    return c.getTime();
  }
}
