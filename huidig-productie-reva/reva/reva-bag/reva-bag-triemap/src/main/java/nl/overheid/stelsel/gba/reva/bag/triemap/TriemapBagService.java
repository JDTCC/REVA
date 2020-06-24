package nl.overheid.stelsel.gba.reva.bag.triemap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import nl.overheid.stelsel.gba.reva.bag.BagException;
import nl.overheid.stelsel.gba.reva.bag.BagService;
import nl.overheid.stelsel.gba.reva.bag.model.AdresseerbaarobjectKoppeling;
import nl.overheid.stelsel.gba.reva.bag.model.BCAdres;
import nl.overheid.stelsel.gba.reva.bag.model.BCNummeraanduiding;
import nl.overheid.stelsel.gba.reva.bag.model.BCOpenbareRuimte;
import nl.overheid.stelsel.gba.reva.bag.model.BCVerblijfsobject;
import nl.overheid.stelsel.gba.reva.bag.model.BCWoonplaats;
import nl.overheid.stelsel.gba.reva.bag.model.Gebruiksdoel;
import nl.overheid.stelsel.gba.reva.bag.model.TypeAdresseerbaarObject;

public class TriemapBagService implements BagService {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  public static final String WOONPLAATSCODETABEL = "!gemeentecodetabel";

  private static final String CACHE_NAME = "reva.bag.TriemapBagService";

  private static final String FILENAME_ADRESSEERBAREOBJECTEN = "bagcompact.dat";
  private static final String FILENAME_INDEX = "bagcompact.idx";
  private static final String FILENAME_META = "bagcompact.meta";

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final Logger LOGGER = LoggerFactory.getLogger( TriemapBagService.class );

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private Ehcache cache = null;
  private Boolean cacheEnabled = false;
  private String cacheConfigurationfile;

  private Path indexPath;
  private TrieMap<FileStoragePointer> index;

  private Path dataPath;
  private FileStorageController<BagAdres> data;
  private FileStorageController<TrieMap<TrieMap<FileStoragePointer>>> stratenboek;

  private Path metaPath;
  private Properties metaInfo;

  // -------------------------------------------------------------------------
  // Constructor
  // -------------------------------------------------------------------------

  public TriemapBagService() {
  }

  // -------------------------------------------------------------------------
  // Public methods
  // -------------------------------------------------------------------------

  public void setBagFilesLocation( String bagFilesLocation ) {
    indexPath = Paths.get( bagFilesLocation, FILENAME_INDEX );
    dataPath = Paths.get( bagFilesLocation, FILENAME_ADRESSEERBAREOBJECTEN );
    metaPath = Paths.get( bagFilesLocation, FILENAME_META );

    initialize();
  }

  public void setBagCacheEnabled( String bagCacheEnabled ) {
    cacheEnabled = Boolean.valueOf( bagCacheEnabled ) && cacheConfigurationfile != null;
    initializeCache();
  }
  
  /**
   * Automatically enabled
   * @param bagCacheConfigFile
   */
  public void setBagCacheConfigFile( String bagCacheConfigFile ) {
	if ( cacheEnabled ) {
      cacheEnabled = bagCacheConfigFile != null;
      File file = new File( bagCacheConfigFile );
      if (!file.exists()) {
        LOGGER.error("BAG Cache configuration file '{}' does not exist, caching disabled", bagCacheConfigFile );
        cacheEnabled = Boolean.FALSE;
      } else {
        if (!file.isFile()) {
          LOGGER.error("BAG Cache configuration '{}' is not a file, caching disabled", bagCacheConfigFile );
          cacheEnabled = Boolean.FALSE;
        }
      }
      cacheConfigurationfile = bagCacheConfigFile;
      initializeCache();
	}
  }

  // -------------------------------------------------------------------------
  // Implementing BagService
  // -------------------------------------------------------------------------

  @Override
  public boolean isWoonplaatsnaam( String woonplaatsnaam ) {
    return index.get( woonplaatsnaam ) != null;
  }

  @Override
  public BCWoonplaats getWoonplaats( String woonplaatsnaam ) {
    BCWoonplaats woonplaats = null;

    if (index.get( woonplaatsnaam ) != null) {
      woonplaats = new BCWoonplaats();
      woonplaats.setWoonplaatsNaam( woonplaatsnaam );
      woonplaats.setIdentificatie( getWoonplaatscode( woonplaatsnaam ) );
    }

    return woonplaats;
  }

  @Override
  public Collection<BCWoonplaats> getWoonplaatsen( String partialWoonplaatsnaam ) {
    ArrayList<BCWoonplaats> woonplaatsen = new ArrayList<>();

    for( String s : index.tail( partialWoonplaatsnaam ) ) {
      BCWoonplaats woonplaats = new BCWoonplaats();
      woonplaats.setWoonplaatsNaam( s );
      woonplaats.setIdentificatie( getWoonplaatscode( s ) );

      woonplaatsen.add( woonplaats );
    }

    return woonplaatsen;
  }

  @Override
  public boolean isOpenbareruimtenaam( String woonplaatsnaam, String openbareruimtenaam ) {
    boolean isOpenbareruimtenaam = false;

    FileStoragePointer pointer = index.get( woonplaatsnaam );
    TrieMap<TrieMap<FileStoragePointer>> openbareruimtenamen = getStraatnaamIndex( pointer );
    if( openbareruimtenamen != null ) {
      isOpenbareruimtenaam = openbareruimtenamen.get( openbareruimtenaam ) != null;
    }

    return isOpenbareruimtenaam;
  }

  @Override
  public Collection<BCOpenbareRuimte> getOpenbareruimtes( String woonplaats, String partialOpenbareruimtenaam ) {
    ArrayList<BCOpenbareRuimte> openbareruimtes = new ArrayList<>();

    FileStoragePointer pointer = index.get( woonplaats );
    TrieMap<TrieMap<FileStoragePointer>> openbareruimtenamen = getStraatnaamIndex( pointer );
    if( openbareruimtenamen != null ) {
      for( String s : openbareruimtenamen.tail( partialOpenbareruimtenaam ) ) {
        BCOpenbareRuimte openbareruimte = new BCOpenbareRuimte();
        openbareruimte.setOpenbareRuimteNaam( s );

        openbareruimtes.add( openbareruimte );
      }
    }

    return openbareruimtes;
  }

  @Override
  public Collection<BCAdres> getAdressen( String woonplaatsnaam, String openbareruimtenaam, String postcode, String huisnummer,
          String huisletter, String huisnummerToevoeging ) {
    ArrayList<BCAdres> adressen = new ArrayList<>();

    // Minimaal woonplaatsnaam en openbareruimtenaam, of postcode moeten zijn
    // opgegeven,
    // anders is de impact van deze functie op het systeem wel erg zwaar.

    if( woonplaatsnaam != null && !woonplaatsnaam.isEmpty() && openbareruimtenaam != null && !openbareruimtenaam.isEmpty() ) {
      FileStoragePointer pointer = index.get( woonplaatsnaam );
      TrieMap<TrieMap<FileStoragePointer>> openbareruimtenamen = getStraatnaamIndex( pointer );

      if( openbareruimtenamen == null ) {
        // Dit betekent dat de woonplaatsnaam partial of leeg is.
        return adressen;
      }

      TrieMap<FileStoragePointer> nummeraanduidingen = openbareruimtenamen.get( openbareruimtenaam );

      if( nummeraanduidingen == null ) {
        // Dit betekent dat de openbareruimtenaam partial of leeg is.
        return adressen;
      }

      String nummeraanduiding = bepaalNummeraanduidingZoekTerm( huisnummer, huisletter, huisnummerToevoeging );
      FileStoragePointer adresPointer = nummeraanduidingen.get( nummeraanduiding );

      if( adresPointer != null ) {
        adressen.add( getBagAdres( adresPointer ) );
      } else {
        for( String s : nummeraanduidingen.tail( nummeraanduiding ) ) {
          adresPointer = nummeraanduidingen.get( s );
          adressen.add( getBagAdres( adresPointer ) );
        }
      }
    }

    return adressen;
  }

  @Override
  public void reset() {
    // Initialiseer opnieuw.
    initialize();
    initializeCache();
  }

  @Override
  public Properties getMetaInfo() {
    return metaInfo;
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private String bepaalNummeraanduidingZoekTerm( String huisnummer, String huisletter, String huisnummerToevoeging ) {
    StringBuffer zoekOpNummeraanduiding = new StringBuffer();
    if( huisnummer != null ) {
      zoekOpNummeraanduiding.append( huisnummer );
    }
    if( huisletter != null ) {
      zoekOpNummeraanduiding.append( huisletter );
    }
    if( huisnummerToevoeging != null ) {
      zoekOpNummeraanduiding.append( huisnummerToevoeging );
    }
    return zoekOpNummeraanduiding.toString();
  }

  private void initialize() {
    index = loadIndex( indexPath );
    data = new FileStorageController<>( dataPath );
    stratenboek = new FileStorageController<>( dataPath );

    metaInfo = loadMetaInfo( metaPath );
  }

  private void initializeCache() {
    // If we have an existing cache, drop it
    if (cache != null) {
      cache.flush();
      cache = null;
    }
    
    if ( cacheEnabled ) {
      try {
        CacheManager manager = CacheManager.create( cacheConfigurationfile );
        if( !manager.cacheExists( CACHE_NAME ) ) {
          manager.addCache( CACHE_NAME );
        }

        cache = manager.getEhcache( CACHE_NAME );
      } catch( CacheException ce ) {
        LOGGER.error("Cache configuration failed due to: ", ce);
        cache = null;
      }
    }
  }

  private TrieMap<FileStoragePointer> loadIndex( Path file ) {
    TrieMap<FileStoragePointer> idx = null;

    // Controleer of de file bestaat.
    if( !file.toFile().exists() ) {
      LOGGER.error( "Bag index bestand '{}' bestaat niet, controleer de configuratie in 'reva.bag.cfg'", file.toString() );
      return new TrieMap<>();
    }

    try( FileChannel channel = FileChannel.open( file, StandardOpenOption.READ ) ) {
      FileStoragePointer pointer = new FileStoragePointer( 0, (int) channel.size() );
      idx = new FileStorageController<TrieMap<FileStoragePointer>>( file ).get( pointer );
    } catch( IOException ioe ) {
      throw new BagException( "BagCompact index kan niet worden gelezen.", ioe );
    }

    return idx;
  }

  private Properties loadMetaInfo( Path file ) {
    Properties properties = new Properties();

    // Controleer of de file bestaat.
    if( !file.toFile().exists() ) {
      LOGGER.error( "Bag meta bestand '{}' bestaat niet, controleer de configuratie in 'reva.bag.cfg'", file.toString() );
      return properties;
    }

    try( InputStream inStream = new FileInputStream( file.toFile() ) ) {
      properties = new Properties();
      properties.load( inStream );
    } catch( IOException ioe ) {
      throw new BagException( "BagCompact meta kan niet worden gelezen.", ioe );
    }

    return properties;
  }

  @SuppressWarnings( "unchecked" )
  private String getWoonplaatscode( String woonplaats ) {
    FileStoragePointer pointer = index.get( WOONPLAATSCODETABEL );
    if( pointer == null ) {
      return null;
    }

    String woonplaatscode = null;

    try {
      HashMap<String, String> woonplaatscodetabel = null;

      Element element = getCacheElement( pointer );
      if( element != null ) {
        woonplaatscodetabel = (HashMap<String, String>) element.getObjectValue();
      } else {
        FileStorageController<HashMap<String, String>> tabel = new FileStorageController<>( dataPath );
        woonplaatscodetabel = tabel.get( pointer );
        putCacheElement( new Element( pointer, woonplaatscodetabel ) );
      }

      woonplaatscode = woonplaatscodetabel.get( woonplaats );
    } catch( IOException ioe ) {
      throw new BagException( "Het inladen van de woonplaatscodetabel is mislukt.", ioe );
    }

    return woonplaatscode;
  }

  @SuppressWarnings( "unchecked" )
  private TrieMap<TrieMap<FileStoragePointer>> getStraatnaamIndex( FileStoragePointer pointer ) {
    if( pointer == null ) {
      return null;
    }

    TrieMap<TrieMap<FileStoragePointer>> straatnaamIndex = null;

    try {
      Element element = getCacheElement( pointer );
      if( element != null ) {
        straatnaamIndex = (TrieMap<TrieMap<FileStoragePointer>>) element.getObjectValue();
      } else {
        straatnaamIndex = stratenboek.get( pointer );
        putCacheElement( new Element( pointer, straatnaamIndex ) );
      }
    } catch( IOException ioe ) {
      throw new BagException( "Het inladen van de straatnaam-index is mislukt.", ioe );
    }

    return straatnaamIndex;
  }

  private BCAdres getBagAdres( FileStoragePointer pointer ) {
    BCAdres bcAdres = null;

    try {
      BagAdres adres = data.get( pointer );

      BCWoonplaats bcWoonplaats = new BCWoonplaats();
      bcWoonplaats.setIdentificatie( adres.getWoonplaatsId() );
      bcWoonplaats.setWoonplaatsNaam( adres.getWoonplaatsNaam() );

      BCOpenbareRuimte bcOpenbareRuimte = new BCOpenbareRuimte();
      bcOpenbareRuimte.setOpenbareRuimteNaam( adres.getOpenbareRuimteNaam() );

      BCNummeraanduiding bcNummeraanduiding = new BCNummeraanduiding();
      bcNummeraanduiding.setIdentificatie( adres.getNummeraanduidingId() );
      bcNummeraanduiding.setHuisnummer( Integer.parseInt( adres.getHuisnummer() ) );
      bcNummeraanduiding.setHuisnummertoevoeging( adres.getHuisnummertoevoeging() );
      bcNummeraanduiding.setHuisletter( adres.getHuisletter() );
      bcNummeraanduiding.setPostcode( adres.getPostcode() );
      bcNummeraanduiding.setTypeAdresseerbaarObject( TypeAdresseerbaarObject.fromValue( adres.getType() ) );

      AdresseerbaarobjectKoppeling koppeling = new AdresseerbaarobjectKoppeling();

      if( adres.getType().equals( TypeAdresseerbaarObject.VERBLIJFSOBJECT.value() ) ) {
        BCVerblijfsobject bcVerblijfsobject = new BCVerblijfsobject();
        bcVerblijfsobject.getGebruiksdoelVerblijfsobject();

        if( adres.getGebruiksdoel().contains( "|" ) ) {
          for( String s : adres.getGebruiksdoel().split( "\\|" ) ) {
            bcVerblijfsobject.getGebruiksdoelVerblijfsobject().add( Gebruiksdoel.fromValue( s ) );
          }
        } else {
          bcVerblijfsobject.getGebruiksdoelVerblijfsobject().add( Gebruiksdoel.fromValue( adres.getGebruiksdoel() ) );
        }

        koppeling.setBCVerblijfsobject( bcVerblijfsobject );
      }

      bcAdres = new BCAdres();
      bcAdres.setBCNummeraanduiding( bcNummeraanduiding );
      bcAdres.setBCOpenbareRuimte( bcOpenbareRuimte );
      bcAdres.setBCWoonplaats( bcWoonplaats );
      bcAdres.setAdresseerbaarobjectKoppeling( koppeling );
    } catch( IOException ioe ) {
      throw new BagException( "Pointer niet te herleiden naar BagAdres.", ioe );
    }

    return bcAdres;
  }
  
  private Element getCacheElement( Object pointer ) {
    Element cachedElement = null;
    if ( cacheEnabled && cache != null ) {
      cachedElement = cache.get( pointer );
    }
    return cachedElement;
  }

  private void putCacheElement( Element element ) {
    if ( cacheEnabled && cache != null ) {
      cache.put( element );
    }
  }
}
