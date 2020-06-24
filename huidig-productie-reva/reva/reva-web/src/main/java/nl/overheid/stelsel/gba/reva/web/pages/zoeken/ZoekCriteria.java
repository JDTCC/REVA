package nl.overheid.stelsel.gba.reva.web.pages.zoeken;

import java.io.Serializable;
import java.util.Date;

/**
 * Simple model voor het door zoeken van de reva informatie.
 * 
 */
public class ZoekCriteria implements Serializable {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private String dossierNummer;
  private Date vanafDatum;
  private Date totenmetDatum;
  private String postCode;
  private String woonPlaats;
  private String openbareRuimte;
  private String huisNummer;
  private String bsn;

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public String getDossierNummer() {
    return dossierNummer;
  }

  public void setDossierNummer( String dossierNummer ) {
    this.dossierNummer = dossierNummer;
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

  public String getWoonPlaats() {
    return woonPlaats;
  }

  public void setWoonPlaats( String woonPlaats ) {
    this.woonPlaats = woonPlaats;
  }

  public String getOpenbareRuimte() {
    return openbareRuimte;
  }

  public void setOpenbareRuimte( String openbareRuimte ) {
    this.openbareRuimte = openbareRuimte;
  }

  public String getPostCode() {
    return postCode;
  }

  // @Pattern(regexp = "[1-9]{1}[0-9]{3}[A-Z]{2}")
  public void setPostCode( String postCode ) {
    this.postCode = postCode;
    if( postCode != null ) {
      this.postCode = postCode.toUpperCase();
    }
  }

  public String getHuisNummer() {
    return huisNummer;
  }

  public void setHuisNummer( String huisNummer ) {
    this.huisNummer = huisNummer;
  }

  public String getBsn() {
    return bsn;
  }

  public void setBsn( String bsn ) {
    this.bsn = bsn;
  }
}
