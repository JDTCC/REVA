package nl.overheid.stelsel.gba.reva.bag.triemap;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class BagAdres implements Serializable {

  private static final long serialVersionUID = -733312179296502981L;

  /**
   * Een categorisering van de gebruiksdoelen van het betreffende
   * VERBLIJFSOBJECT, zoals dit formeel door de overheid als zodanig is
   * toegestaan. Een VERBLIJFSOBJECT is de kleinste binnen één of meerdere
   * panden gelegen en voor woon -, bedrijfsmatige - of recreatieve doeleinden
   * geschikte eenheid van gebruik, die ontsloten wordt via een eigen toegang
   * vanaf de openbare weg, een erf of een gedeelde verkeersruimte en die
   * onderwerp kan zijn van rechtshandelingen.
   */
  private String gebruiksdoel;

  /**
   * Een door of namens het gemeentebestuur ten aanzien van een adresseerbaar
   * object toegekende nummering.
   */
  private String huisnummer;

  /**
   * Een door of namens het gemeentebestuur ten aanzien van een adresseerbaar
   * object toegekende nadere toevoeging aan een huisnummer of een combinatie
   * van huisnummer en huisletter.
   */
  private String huisnummertoevoeging;

  /**
   * Een door of namens het gemeentebestuur ten aanzien van een adresseerbaar
   * object toegekende toevoeging aan een huisnummer in de vorm van een
   * alfanumeriek teken.
   */
  private String huisletter;

  /**
   * De unieke aanduiding van een NUMMERAANDUIDING. Een NUMMERAANDUIDING is een
   * door de gemeenteraad als zodanig toegekende aanduiding van een
   * adresseerbaar object.
   */
  private String nummeraanduidingId;

  /**
   * Een naam die aan een OPENBARE RUIMTE is toegekend in een daartoe strekkend
   * formeel gemeentelijk besluit. Een OPENBARE RUIMTE is een door de
   * gemeenteraad als zodanig aangewezen benaming van een binnen 1 woonplaats
   * gelegen buitenruimte.
   * 
   */
  private String openbareRuimteNaam;

  /**
   * De door TNT Post vastgestelde code behorende bij een bepaalde combinatie
   * van een straatnaam en een huisnummer.
   */
  private String postcode;

  /**
   * De aard van een als zodanig benoemde NUMMERAANDUIDING. Een NUMMERAANDUIDING
   * is een door de gemeenteraad als zodanig toegekende aanduiding van een
   * adresseerbaar object.
   */
  private String type;

  /**
   * De landelijk unieke aanduiding van een WOONPLAATS, zoals vastgesteld door
   * de beheerder van de landelijke tabel voor woonplaatsen. Een WOONPLAATS is
   * een door de gemeenteraad als zodanig aangewezen gedeelte van het
   * gemeentelijk grondgebied.
   */
  private String woonplaatsId;

  /**
   * De benaming van een door het gemeentebestuur aangewezen WOONPLAATS. Een
   * WOONPLAATS is een door de gemeenteraad als zodanig aangewezen gedeelte van
   * het gemeentelijk grondgebied.
   */
  private String woonplaatsNaam;

  public BagAdres() {
    // Lege constructor.
  }

  public String getGebruiksdoel() {
    return gebruiksdoel;
  }

  public void setGebruiksdoel( String gebruiksdoel ) {
    this.gebruiksdoel = gebruiksdoel;
  }

  public String getHuisnummer() {
    return huisnummer;
  }

  public void setHuisnummer( String huisnummer ) {
    this.huisnummer = huisnummer;
  }

  public String getHuisnummertoevoeging() {
    return huisnummertoevoeging;
  }

  public void setHuisnummertoevoeging( String huisnummertoevoeging ) {
    this.huisnummertoevoeging = huisnummertoevoeging;
  }

  public String getHuisletter() {
    return huisletter;
  }

  public void setHuisletter( String huisletter ) {
    this.huisletter = huisletter;
  }

  public String getNummeraanduidingId() {
    return nummeraanduidingId;
  }

  public void setNummeraanduidingId( String nummeraanduidingId ) {
    this.nummeraanduidingId = nummeraanduidingId;
  }

  public String getOpenbareRuimteNaam() {
    return openbareRuimteNaam;
  }

  public void setOpenbareRuimteNaam( String openbareRuimteNaam ) {
    this.openbareRuimteNaam = openbareRuimteNaam;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode( String postcode ) {
    this.postcode = postcode;
  }

  public String getType() {
    return type;
  }

  public void setType( String type ) {
    this.type = type;
  }

  public String getWoonplaatsId() {
    return woonplaatsId;
  }

  public void setWoonplaatsId( String woonplaatsId ) {
    this.woonplaatsId = woonplaatsId;
  }

  public String getWoonplaatsNaam() {
    return woonplaatsNaam;
  }

  public void setWoonplaatsNaam( String woonplaatsNaam ) {
    this.woonplaatsNaam = woonplaatsNaam;
  }

  /**
   * Geeft de NUMMERAANDUIDING bestaande uit de samenvoeging van:
   * (huisnummer)(huisletter)(huisnummertoevoeging)
   * 
   * @return De samengestelde nummeraanduiding.
   */
  public String getNummeraanduiding() {
    StringBuffer nummeraanduiding = new StringBuffer();
    if( huisnummer != null ) {
      nummeraanduiding.append( huisnummer );
    }
    if( huisletter != null ) {
      nummeraanduiding.append( huisletter );
    }
    if( huisnummertoevoeging != null ) {
      nummeraanduiding.append( huisnummertoevoeging );
    }

    return nummeraanduiding.toString();
  }

  @Override
  public boolean equals( Object obj ) {
    // Vergelijking met niks.
    if( obj == null ) {
      return false;
    }

    // Vergelijking met zichzelf.
    if( this == obj ) {
      return true;
    }

    if( obj instanceof BagAdres ) {
      BagAdres adres = (BagAdres) obj;

      // We gaan ervan uit dat de objecten gelijk zijn, totdat we zeker
      // weten dat dit niet zo is.
      boolean isEqual = true;

      isEqual &= StringUtils.equalsIgnoreCase( gebruiksdoel, adres.getGebruiksdoel() );
      isEqual &= StringUtils.equalsIgnoreCase( huisnummer, adres.getHuisnummer() );
      isEqual &= StringUtils.equalsIgnoreCase( huisnummertoevoeging, adres.getHuisnummertoevoeging() );
      isEqual &= StringUtils.equalsIgnoreCase( huisletter, adres.getHuisletter() );
      isEqual &= StringUtils.equalsIgnoreCase( nummeraanduidingId, adres.getNummeraanduidingId() );
      isEqual &= StringUtils.equalsIgnoreCase( openbareRuimteNaam, adres.getOpenbareRuimteNaam() );
      isEqual &= StringUtils.equalsIgnoreCase( postcode, adres.getPostcode() );
      isEqual &= StringUtils.equalsIgnoreCase( type, adres.getType() );
      isEqual &= StringUtils.equalsIgnoreCase( woonplaatsId, adres.getWoonplaatsId() );
      isEqual &= StringUtils.equalsIgnoreCase( woonplaatsNaam, adres.getWoonplaatsNaam() );

      return isEqual;
    }

    return false;
  }

  @Override
  public int hashCode() {
    StringBuffer string = new StringBuffer();
    string.append( gebruiksdoel );
    string.append( huisnummer );
    string.append( huisnummertoevoeging );
    string.append( huisletter );
    string.append( nummeraanduidingId );
    string.append( openbareRuimteNaam );
    string.append( postcode );
    string.append( type );
    string.append( woonplaatsId );
    string.append( woonplaatsNaam );

    return string.toString().hashCode();
  }

  @Override
  public String toString() {
    StringBuffer string = new StringBuffer();
    string.append( nummeraanduidingId ).append( "," );
    string.append( woonplaatsNaam ).append( " " ).append( woonplaatsId ).append( "," );
    string.append( openbareRuimteNaam ).append( "," );

    if( huisnummer != null ) {
      string.append( huisnummer ).append( "," );
    }
    if( huisletter != null ) {
      string.append( huisletter ).append( "," );
    }
    if( huisnummertoevoeging != null ) {
      string.append( huisnummertoevoeging ).append( "," );
    }

    string.append( postcode ).append( "," );
    string.append( type ).append( "," );
    string.append( gebruiksdoel );

    return string.toString();
  }

}
