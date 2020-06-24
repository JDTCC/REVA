package nl.overheid.stelsel.gba.reva.bag.triemap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

import nl.overheid.stelsel.gba.reva.bag.model.BCAdres;
import nl.overheid.stelsel.gba.reva.bag.model.BCVerblijfsobject;
import nl.overheid.stelsel.gba.reva.bag.model.Gebruiksdoel;
import nl.overheid.stelsel.gba.reva.bag.model.Indicatie;

public class TriemapBagImport {

  private String storageLocation;
  private Path indexPath;
  private Path dataPath;
  private Path metaPath;

  private TrieMap<TrieMap<TrieMap<FileStoragePointer>>> index;
  private FileStorageController<BagAdres> data;
  private HashMap<String, String> woonplaatscodetabel;
  private Properties metaInfo = new Properties();

  public TriemapBagImport( String location ) {
    storageLocation = location;
  }

  public void initialize() {
    dataPath = Paths.get( storageLocation, "bagcompact.dat" );
    indexPath = Paths.get( storageLocation, "bagcompact.idx" );
    metaPath = Paths.get( storageLocation, "bagcompact.meta" );

    data = new FileStorageController<>( dataPath );
    index = new TrieMap<>();
    woonplaatscodetabel = new HashMap<>();
    metaInfo.clear();
  }

  public void addMetaInfo( String name, String value ) {
    metaInfo.put( name, value );
  }

  public void addAdres( BCAdres adres ) throws IOException {
    if( Indicatie.N.equals( adres.getAanduidingRecordInactief() ) ) {
      BagAdres bca = new BagAdres();

      BCVerblijfsobject verblijfsobject = adres.getAdresseerbaarobjectKoppeling().getBCVerblijfsobject();
      if( verblijfsobject != null ) {
        for( Gebruiksdoel doel : verblijfsobject.getGebruiksdoelVerblijfsobject() ) {
          String gebruiksdoel = bca.getGebruiksdoel();
          if( gebruiksdoel == null ) {
            gebruiksdoel = doel.value();
          } else {
            gebruiksdoel = gebruiksdoel.concat( "|" ).concat( doel.value() );
          }
          bca.setGebruiksdoel( gebruiksdoel );
        }
      }

      bca.setHuisletter( adres.getBCNummeraanduiding().getHuisletter() );
      bca.setHuisnummer( String.valueOf( adres.getBCNummeraanduiding().getHuisnummer() ) );
      bca.setHuisnummertoevoeging( adres.getBCNummeraanduiding().getHuisnummertoevoeging() );
      bca.setNummeraanduidingId( adres.getBCNummeraanduiding().getIdentificatie() );
      bca.setOpenbareRuimteNaam( adres.getBCOpenbareRuimte().getOpenbareRuimteNaam() );
      bca.setPostcode( adres.getBCNummeraanduiding().getPostcode() );
      bca.setType( adres.getBCNummeraanduiding().getTypeAdresseerbaarObject().value() );
      bca.setWoonplaatsId( adres.getBCWoonplaats().getIdentificatie() );
      bca.setWoonplaatsNaam( adres.getBCWoonplaats().getWoonplaatsNaam() );

      // BagAdres opslaan in adressenbestand.
      FileStoragePointer pointer = data.add( bca );

      // INDEX naar POINTER adhv woonplaats, straat en huisnummer.
      addIndexWoonplaats( bca, pointer );

      // INDEX(woonplaats) naar STRING(woonplaatscode)
      addIndexWoonplaatscode( bca );
    }
  }

  private void addIndexWoonplaats( BagAdres adres, FileStoragePointer pointer ) {
    // Woonplaats
    TrieMap<TrieMap<FileStoragePointer>> straten = index.get( adres.getWoonplaatsNaam() );
    if( straten == null ) {
      straten = new TrieMap<>();
      index.put( adres.getWoonplaatsNaam(), straten );
    }

    // Straat
    TrieMap<FileStoragePointer> huisnummers = straten.get( adres.getOpenbareRuimteNaam() );
    if( huisnummers == null ) {
      huisnummers = new TrieMap<>();
      straten.put( adres.getOpenbareRuimteNaam(), huisnummers );
    }

    // Huisnummer / -letter / -toevoeging
    huisnummers.put( adres.getNummeraanduiding(), pointer );
  }

  private void addIndexWoonplaatscode( BagAdres adres ) {
    woonplaatscodetabel.put( adres.getWoonplaatsNaam(), adres.getWoonplaatsId() );
  }

  /**
   * Schrijft de index naar file. Voordat de index naar file wordt geschreven,
   * wordt deze gesplits. Van iedere stad wordt het stratenboek apart opgeslagen
   * in het data-bestand. Vervolgens wordt een nieuwe index opgebouwd met voor
   * iedere stad een <tt>FileStoragePointer</tt> naar het stratenboek. Op deze
   * manier blijft de index klein en compact, waardoor deze snel kan worden
   * ingeladen en runtime weinig geheugen nodig is.
   */
  public void writeIndexToFile() throws Exception {
    // Controller voor het opslaan van de stratenboeken.
    FileStorageController<TrieMap<TrieMap<FileStoragePointer>>> streetmap = new FileStorageController<>( dataPath );

    // Nieuwe index van stad en FileStoragePointer naar stratenboek.
    TrieMap<FileStoragePointer> citymap = new TrieMap<>();

    // Splitsen (oude) index.
    for( String woonplaats : index.tail( "" ) ) {
      TrieMap<TrieMap<FileStoragePointer>> stratenplan = index.get( woonplaats );
      FileStoragePointer pointer = streetmap.add( stratenplan );
      citymap.put( woonplaats, pointer );
    }

    // Woonplaatscodetabel toevoegen. (#211)
    FileStorageController<HashMap<String, String>> tabel = new FileStorageController<>( dataPath );
    FileStoragePointer pointer = tabel.add( woonplaatscodetabel );
    citymap.put( TriemapBagService.WOONPLAATSCODETABEL, pointer );

    // Opslaan van de (nieuwe) index.
    FileStorageController<TrieMap<FileStoragePointer>> file = new FileStorageController<>( indexPath );
    file.add( citymap );
  }

  /**
   * Schrijft de meta info naar een property file die later weer eenvoudig te
   * lezen is.
   */
  public void writeMetaInfo() throws IOException {
    File file = metaPath.toFile();
    FileOutputStream fileOut = new FileOutputStream( file );
    metaInfo.store( fileOut, "BAG import meta data" );
    fileOut.close();
  }
}
