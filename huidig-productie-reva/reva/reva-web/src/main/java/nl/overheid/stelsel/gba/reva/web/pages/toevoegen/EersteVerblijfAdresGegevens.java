package nl.overheid.stelsel.gba.reva.web.pages.toevoegen;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Simple model voor de eerste verblijfadres gegevens.
 * 
 */
public class EersteVerblijfAdresGegevens implements Serializable {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private UUID stamId;
  private Date registratieTijdstempel;
  private Date correctieTijdstempel;
  private String wijzigingDatum;  // alleen gebruik op zoek scherm
  private String bsn;
  private String dossierNummer;
  private String gemeenteCode;
  private String woonPlaats;
  private String openbareRuimte;
  private String postCode;
  private String huisNummer;
  private String huisLetter;
  private String huisNummerToevoeging;
  private String aanduidingHuisNummer;
  private String adresType;
  private String toelichting;
  private String idCodeObject;
  private String idCodeNummerAanduiding;
  private String bagGebruiksdoelen;
  private String gemeenteVanInschrijving;

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public UUID getStamId() {
    return stamId;
  }

  public void setStamId( UUID stamId ) {
    this.stamId = stamId;
  }

  public Date getRegistratieTijdstempel() {
    return registratieTijdstempel;
  }

  public void setRegistratieTijdstempel( Date tijdstempel ) {
    this.registratieTijdstempel = tijdstempel;
  }

  public Date getCorrectieTijdstempel() {
    return correctieTijdstempel;
  }

  public void setCorrectieTijdstempel( Date tijdstempel ) {
    this.correctieTijdstempel = tijdstempel;
  }

  public String getWijzigingDatum() {
    return wijzigingDatum;
  }

  public void setWijzigingDatum( String wijzigingDatum ) {
    this.wijzigingDatum = wijzigingDatum;
  }

  public String getBsn() {
    return bsn;
  }

  public void setBsn( String bsn ) {
    this.bsn = bsn;
  }

  public String getDossierNummer() {
    return dossierNummer;
  }

  public void setDossierNummer( String dossierNummer ) {
    this.dossierNummer = dossierNummer;
  }

  public String getGemeenteCode() {
	return gemeenteCode;
  }
  
  public void setGemeenteCode(String gemeenteCode) {
	this.gemeenteCode = gemeenteCode;
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

  public String getHuisLetter() {
    return huisLetter;
  }

  public void setHuisLetter( String huisLetter ) {
    this.huisLetter = huisLetter;
  }

  public String getHuisNummerToevoeging() {
    return huisNummerToevoeging;
  }

  public void setHuisNummerToevoeging( String huisNummerToevoeging ) {
    this.huisNummerToevoeging = huisNummerToevoeging;
  }

  public String getAanduidingHuisNummer() {
    return aanduidingHuisNummer;
  }

  public void setAanduidingHuisNummer( String aanduidingHuisNummer ) {
    this.aanduidingHuisNummer = aanduidingHuisNummer;
  }

  public String getAdresType() {
    return adresType;
  }

  public void setAdresType( String adresType ) {
    this.adresType = adresType;
  }

  public String getToelichting() {
    return toelichting;
  }

  public void setToelichting( String toelichting ) {
    this.toelichting = toelichting;
  }

  public String getIdCodeObject() {
    return idCodeObject;
  }

  public void setIdCodeObject( String idCodeObject ) {
    this.idCodeObject = idCodeObject;
  }

  public String getIdCodeNummerAanduiding() {
    return idCodeNummerAanduiding;
  }

  public void setIdCodeNummerAanduiding( String idCodeNummerAanduiding ) {
    this.idCodeNummerAanduiding = idCodeNummerAanduiding;
  }

  public String getBagGebruiksdoelen() {
    return bagGebruiksdoelen;
  }

  public void setBagGebruiksdoelen( String bagGebruiksdoelen ) {
    this.bagGebruiksdoelen = bagGebruiksdoelen;
  }

  public String getGemeenteVanInschrijving() {
    return gemeenteVanInschrijving;
  }

  public void setGemeenteVanInschrijving( String gemeenteVanInschrijving ) {
    this.gemeenteVanInschrijving = gemeenteVanInschrijving;
  }
}
