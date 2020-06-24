package nl.overheid.stelsel.gba.reva.bag.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import nl.overheid.stelsel.gba.reva.bag.BagService;
import nl.overheid.stelsel.gba.reva.bag.model.AdresseerbaarobjectKoppeling;
import nl.overheid.stelsel.gba.reva.bag.model.BCAdres;
import nl.overheid.stelsel.gba.reva.bag.model.BCNummeraanduiding;
import nl.overheid.stelsel.gba.reva.bag.model.BCOpenbareRuimte;
import nl.overheid.stelsel.gba.reva.bag.model.BCVerblijfsobject;
import nl.overheid.stelsel.gba.reva.bag.model.BCWoonplaats;
import nl.overheid.stelsel.gba.reva.bag.model.Gebruiksdoel;
import nl.overheid.stelsel.gba.reva.bag.model.Indicatie;

/**
 * Mock implementatie van een BagService.
 * 
 */
public class BagMockService implements BagService {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final int VERKORTENAAM_LENGTE = 4;
  private static final String UTRECHT = "Utrecht";
  private static final String DEN_HELDER = "Den Helder";
  private static final String DENBOSCH = "'s-Hertogenbosch";
  private static final String ASSENDELFT = "Assendelft";
  private static final String AMSTELVEEN = "Amstelveen";
  private static final String AMSTERDAM = "Amsterdam";
  private static final String ASSEN = "Assen";
  private static final String DENHAAG = "'s-Gravenhage";

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final List<String> PLAATSNAMEN = new ArrayList<>();
  private static final Map<String, List<String>> OPENBARERUIMTENAMEN = new HashMap<>();
  private static final Map<String, List<BCAdres>> ADRESSEN = new HashMap<>();
  private static final Map<String, String> PLAATS_ID = new HashMap<>();

  static {
    PLAATSNAMEN.add( AMSTERDAM );
    PLAATSNAMEN.add( AMSTELVEEN );
    PLAATSNAMEN.add( ASSEN );
    PLAATSNAMEN.add( ASSENDELFT );
    PLAATSNAMEN.add( DENHAAG );
    PLAATSNAMEN.add( DENBOSCH );
    PLAATSNAMEN.add( "Den Ham" );
    PLAATSNAMEN.add( DEN_HELDER );
    PLAATSNAMEN.add( UTRECHT );

    PLAATS_ID.put( AMSTERDAM, "1024" );
    PLAATS_ID.put( AMSTELVEEN, "1050" );
    PLAATS_ID.put( ASSEN, "2391" );
    PLAATS_ID.put( ASSENDELFT, "1878" );
    PLAATS_ID.put( DENHAAG, "1245" );
    PLAATS_ID.put( DENBOSCH, "1595" );
    PLAATS_ID.put( "Den Ham (Gr)", "2116" );
    PLAATS_ID.put( "Den Ham (Ov)", "3321" );
    PLAATS_ID.put( DEN_HELDER, "3056" );
    PLAATS_ID.put( UTRECHT, "3295" );

    List<String> namen = new ArrayList<>();
    namen.add( "Kerkstaat" );
    namen.add( "Kalverstraat" );
    namen.add( "Rokin" );
    OPENBARERUIMTENAMEN.put( AMSTERDAM.toUpperCase(), namen );

    namen = new ArrayList<>();
    namen.add( "Malieveld" );
    namen.add( "Spui" );
    namen.add( "Van Alphenstraat" );
    namen.add( "Van de Weteringelaan" );
    namen.add( "Van Arembergelaan" );
    namen.add( "Van Halewijnlaan" );
    namen.add( "Schenkkade" );
    namen.add( "Scheldestraat" );
    OPENBARERUIMTENAMEN.put( DENHAAG.toUpperCase(), namen );

    ADRESSEN.put( DENHAAG.toUpperCase(), createValidAdres( DENHAAG ) );
    ADRESSEN.put( AMSTERDAM.toUpperCase(), createDoubleAdres( AMSTERDAM ) );
    ADRESSEN.put( ASSEN.toUpperCase(), createInvalidAdres( ASSEN ) );
  }

  // -------------------------------------------------------------------------
  // Implementing BagService
  // -------------------------------------------------------------------------

  /** {@inheritDoc} */
  @Override
  public boolean isWoonplaatsnaam( String woonplaatsnaam ) {
    for( String plaatsnaam : PLAATSNAMEN ) {
      if( plaatsnaam.equalsIgnoreCase( woonplaatsnaam ) ) {
        return true;
      }
    }
    return false;
  };

  /** {@inheritDoc} */
  @Override
  public BCWoonplaats getWoonplaats(String woonplaatsnaam) {
    for( String plaatsnaam : PLAATSNAMEN ) {
      if( plaatsnaam.equalsIgnoreCase( woonplaatsnaam ) ) {
        BCWoonplaats woonplaats = new BCWoonplaats();
        woonplaats.setWoonplaatsNaam( plaatsnaam );
        woonplaats.setIdentificatie( PLAATS_ID.get( plaatsnaam ) );
        return woonplaats;
      }
    }
    return null;
  }
  
  /** {@inheritDoc} */
  @Override
  public Collection<BCWoonplaats> getWoonplaatsen( String partialWoonplaatsnaam ) {
    List<BCWoonplaats> matches = new ArrayList<>();
    for( String plaatsnaam : PLAATSNAMEN ) {
      if( plaatsnaam.toUpperCase().startsWith( partialWoonplaatsnaam.toUpperCase() ) ) {
        BCWoonplaats woonplaats = new BCWoonplaats();
        woonplaats.setWoonplaatsNaam( plaatsnaam );
        woonplaats.setIdentificatie( PLAATS_ID.get( plaatsnaam ) );
        matches.add( woonplaats );
      }
    }

    return matches;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOpenbareruimtenaam( String woonplaatsnaam, String openbareruimtenaam ) {
    if( isWoonplaatsnaam( woonplaatsnaam ) ) {
      List<String> ruimtenamen = OPENBARERUIMTENAMEN.get( woonplaatsnaam.toUpperCase() );
      for( String ruimtenaam : ruimtenamen ) {
        if( ruimtenaam.equalsIgnoreCase( openbareruimtenaam ) ) {
          return true;
        }
      }
    }
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public Collection<BCOpenbareRuimte> getOpenbareruimtes( String woonplaats, String partialOpenbareruimtenaam ) {
    List<BCOpenbareRuimte> matches = new ArrayList<>();
    List<String> ruimtesInWoonplaats = OPENBARERUIMTENAMEN.get( woonplaats.toUpperCase() );
    if( ruimtesInWoonplaats != null ) {
      for( String openbareruimtenaam : ruimtesInWoonplaats ) {
        if( openbareruimtenaam.toUpperCase().startsWith( partialOpenbareruimtenaam.toUpperCase() ) ) {
          BCOpenbareRuimte openbareruimte = new BCOpenbareRuimte();
          openbareruimte.setOpenbareRuimteNaam( openbareruimtenaam );
          openbareruimte.setVerkorteOpenbareruimteNaam( openbareruimtenaam.substring( 0, VERKORTENAAM_LENGTE ) );
          matches.add( openbareruimte );
        }
      }
    }

    return matches;
  }

  /** {@inheritDoc} */
  @Override
  public Collection<BCAdres> getAdressen( String woonplaatsnaam, String openbareruimtenaam, String postcode, String huisnummer,
          String huisletter, String huisnummerToevoeging ) {
    List<BCAdres> adressen = new ArrayList<>();
    if( ADRESSEN.containsKey( woonplaatsnaam.toUpperCase() ) ) {
      adressen.addAll( ADRESSEN.get( woonplaatsnaam.toUpperCase() ) );
    }
    return adressen;
  }

  /** {@inheritDoc} */
  @Override
  public void reset() {
    // Do nothing
  }

  @Override
  public Properties getMetaInfo() {
    return new Properties();
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private static List<BCAdres> createValidAdres( String woonplaatsnaam ) {
    List<BCAdres> adressen = new ArrayList<>();
    BCAdres adres = createAdres( woonplaatsnaam );

    adres.setAanduidingRecordInactief( Indicatie.N );
    adres.setBCNummeraanduiding( createNummerAanduiding( "19074913748" ) );
    BCVerblijfsobject verblijfsobject = createVerblijfsobject( "319248", Arrays.asList( Gebruiksdoel.WOONFUNCTIE ) );
    adres.setAdresseerbaarobjectKoppeling( createAdresseerbaarobjectKoppeling( verblijfsobject ) );

    adressen.add( adres );
    return adressen;
  }

  private static List<BCAdres> createDoubleAdres( String woonplaatsnaam ) {
    List<BCAdres> adressen = new ArrayList<>();
    BCAdres adres = createAdres( woonplaatsnaam );
    adres.setBCNummeraanduiding( createNummerAanduiding( "12384713" ) );
    BCVerblijfsobject verblijfsobject = createVerblijfsobject( "234987", Arrays.asList( Gebruiksdoel.WOONFUNCTIE ) );
    adres.setAdresseerbaarobjectKoppeling( createAdresseerbaarobjectKoppeling( verblijfsobject ) );
    adressen.add( adres );

    adres = createAdres( woonplaatsnaam );
    adres.setBCNummeraanduiding( createNummerAanduiding( "1908741873" ) );
    verblijfsobject = createVerblijfsobject( "302489", Arrays.asList( Gebruiksdoel.WOONFUNCTIE ) );
    adres.setAdresseerbaarobjectKoppeling( createAdresseerbaarobjectKoppeling( verblijfsobject ) );
    adressen.add( adres );
    return adressen;
  }

  private static List<BCAdres> createInvalidAdres( String woonplaatsnaam ) {
    List<BCAdres> adressen = new ArrayList<>();
    BCAdres adres = createAdres( woonplaatsnaam );
    adres.setBCNummeraanduiding( createNummerAanduiding( "9182375983" ) );
    BCVerblijfsobject verblijfsobject = createVerblijfsobject( "234987", Arrays.asList( Gebruiksdoel.INDUSTRIEFUNCTIE ) );
    adres.setAdresseerbaarobjectKoppeling( createAdresseerbaarobjectKoppeling( verblijfsobject ) );

    adressen.add( adres );
    return adressen;
  }

  private static BCAdres createAdres( String woonplaats ) {
    BCAdres adres = new BCAdres();
    BCWoonplaats bcWoonplaats = new BCWoonplaats();
    bcWoonplaats.setWoonplaatsNaam( woonplaats );
    bcWoonplaats.setIdentificatie( PLAATS_ID.get( woonplaats ) );
    adres.setBCWoonplaats( bcWoonplaats );
    return adres;
  }

  private static BCNummeraanduiding createNummerAanduiding( String identificatie ) {
    BCNummeraanduiding nummerAanduiding = new BCNummeraanduiding();
    nummerAanduiding.setIdentificatie( identificatie );
    return nummerAanduiding;
  }

  private static AdresseerbaarobjectKoppeling createAdresseerbaarobjectKoppeling( BCVerblijfsobject verblijfsobject ) {
    AdresseerbaarobjectKoppeling adresseerbaarobjectKoppeling = new AdresseerbaarobjectKoppeling();
    adresseerbaarobjectKoppeling.setBCVerblijfsobject( verblijfsobject );
    return adresseerbaarobjectKoppeling;
  }

  private static BCVerblijfsobject createVerblijfsobject( String identificatie, List<Gebruiksdoel> gebruiksdoelen ) {
    BCVerblijfsobject verblijfsobject = new BCVerblijfsobject();
    verblijfsobject.setIdentificatie( identificatie );
    verblijfsobject.getGebruiksdoelVerblijfsobject().addAll( gebruiksdoelen );
    return verblijfsobject;
  }
}
