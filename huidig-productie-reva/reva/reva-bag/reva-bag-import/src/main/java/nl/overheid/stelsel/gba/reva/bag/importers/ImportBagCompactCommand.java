package nl.overheid.stelsel.gba.reva.bag.importers;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.overheid.stelsel.digimelding.astore.utils.DateUtils;
import nl.overheid.stelsel.gba.reva.bag.BagException;
import nl.overheid.stelsel.gba.reva.bag.model.BCAdres;
import nl.overheid.stelsel.gba.reva.bag.model.BCAdresProduct;
import nl.overheid.stelsel.gba.reva.bag.model.Indicatie;
import nl.overheid.stelsel.gba.reva.bag.model.ObjectFactory;
import nl.overheid.stelsel.gba.reva.bag.model.Tijdvakgeldigheid;
import nl.overheid.stelsel.gba.reva.bag.triemap.TriemapBagImport;

@Command( scope = "bag-import", name = "import-bag", description = "Importeer BAG compact." )
public class ImportBagCompactCommand extends OsgiCommandSupport {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final long NANOS_PER_SECOND = 1000000000;
  private static final long NANOS_PER_MINUTE = NANOS_PER_SECOND * 60;

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger( ImportBagCompactCommand.class );

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private File archiveDir = null;
  private File errorDir = null;
  private TriemapBagImport importer = null;
  private String laatsteMutatieDatum = "";

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option( name = "-d", aliases = { "--dryrun" }, description = "Reading the import files without actually importing.", required = false, multiValued = false )
  private boolean isDryRun;

  @Option( name = "-t", aliases = { "--timing" }, description = "Timing import duration.", required = false, multiValued = false )
  private boolean isTimingEnabled;

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument( index = 0, name = "triemap-location", description = "The directory where to store triemap data and index files.", required = true, multiValued = false )
  private String triemapLocation;

  @Argument( index = 1, name = "location", description = "The directory or file to import.", required = true, multiValued = false )
  private String importLocation;

  @Argument( index = 2, name = "outputLocation", description = "The directory where to place the output files.", required = true, multiValued = false )
  private String outputLocation;

  // -------------------------------------------------------------------------
  // NamedQueryCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    Object obj = null;
    importer = null;

    prepareArchiveLocation( outputLocation );
    prepareStorageLocation( triemapLocation );
    File[] importFiles = prepareImportLocation( importLocation );

    logger.info( "Started BAG import..." );
    try {
      Long totalStart = System.nanoTime();
      Long counter = 0L;
      getImporter().addMetaInfo( "timestampImport", DateUtils.getDateAsISO8601String( new Date() ) );
      for( File singleImport : importFiles ) {
        session.getConsole().println( String.format( "Starting with %d of %d", ++counter, importFiles.length ) );
        Long singleStart = System.nanoTime();
        List<BCAdres> adressen = getAdressenFromFile( singleImport );
        if( !isDryRun ) {
          File errorFile = new File( errorDir, singleImport.getName() );
          processAdressen( adressen, errorFile );

          // Move file to archive.
          singleImport.renameTo( new File( archiveDir, singleImport.getName() ) );
        }

        // Make sure to kick in the carbage collected once and a while.
        produceTimingInfo( singleStart, "File import took %d s" );
      }

      if( !isDryRun ) {
        // We have to wrap this up and terminate the importer otherwise
        // we will run out of memory after a while.
        session.getConsole().println( "Compacting triemap storage" );
        getImporter().writeIndexToFile();

        getImporter().addMetaInfo( "laatsteMutatie", laatsteMutatieDatum );
        getImporter().addMetaInfo( "importDuration", (System.nanoTime() - totalStart) / NANOS_PER_MINUTE + " min" );
        getImporter().writeMetaInfo();
      }
      produceTimingInfo( totalStart, "Total import took %d s" );
    } catch( Exception ex ) {
      session.getConsole().println( ex.getMessage() );
      logger.warn( ex.getMessage(), ex );
    }
    logger.info( "Finished BAG import!" );
    return obj;
  }

  // -------------------------------------------------------------------------
  // Private Methods
  // -------------------------------------------------------------------------

  public TriemapBagImport getImporter() {
    if( importer == null ) {
      importer = new TriemapBagImport( triemapLocation );
      importer.initialize();
    }
    return importer;
  }

  @SuppressWarnings( "unchecked" )
  private List<BCAdres> getAdressenFromFile( File file ) throws JAXBException {
    session.getConsole().println( "Reading file: " + file.getName() );

    // JAXB gebruiken voor de binding.
    JAXBContext jc = JAXBContext.newInstance( ObjectFactory.class.getPackage().getName(), ObjectFactory.class.getClassLoader() );
    Unmarshaller unmarshaller = jc.createUnmarshaller();
    JAXBElement<BCAdresProduct> doc = (JAXBElement<BCAdresProduct>) unmarshaller.unmarshal( file );

    // Resultaat teruggeven.
    BCAdresProduct adresProduct = doc.getValue();
    session.getConsole().println( "Done" );
    return adresProduct.getBCAdres();
  }

  private void processAdressen( List<BCAdres> adressen, File errorFile ) {
    session.getConsole().println( "Processing...  " );
    BCAdresProduct foutAdressen = new BCAdresProduct();

    for( BCAdres bcAdres : adressen ) {
      bewaarLaatsteMutatieDatum( bcAdres.getTijdvakgeldigheid() );

      // Alleen active adressen verwerken.
      if( Indicatie.N.equals( bcAdres.getAanduidingRecordInactief() ) ) {
        try {
          bewaarAdres( bcAdres );
        } catch( Exception e ) {
          String identificatie = bcAdres.getBCNummeraanduiding().getIdentificatie();
          session.getConsole().println( "Failed to process: " + identificatie );
          logger.warn( "Failed to process: " + identificatie, e );

          // log als adres
          foutAdressen.getBCAdres().add( bcAdres );
        }
      }
    }

    if( foutAdressen.getBCAdres().isEmpty() ) {
      session.getConsole().println( "Done" );
    } else {
      storeFoutAdressenToFile( foutAdressen, errorFile );
    }
  }

  private void storeFoutAdressenToFile( BCAdresProduct adressen, File errorFile ) {
    session.getConsole().println( "Writing error file...  " );

    // JAXB gebruiken voor de binding.
    JAXBContext jc;
    try {
      jc = JAXBContext.newInstance( ObjectFactory.class.getPackage().getName(), ObjectFactory.class.getClassLoader() );
      Marshaller marshaller = jc.createMarshaller();
      JAXBElement<BCAdresProduct> element = new JAXBElement<BCAdresProduct>( new QName( "uri", "local" ), BCAdresProduct.class,
              adressen );
      marshaller.marshal( element, errorFile );
    } catch( JAXBException e ) {
      logger.error( "Writing error file failed!", e );
      session.getConsole().println( "Writing error file failed!" );
      return;
    }

    session.getConsole().println( "Written " + adressen.getBCAdres().size() + " errors to " + errorFile.getName() );
  }

  private void bewaarAdres( BCAdres adres ) {
    try {
      getImporter().addAdres( adres );
    } catch( Exception e ) {
      throw new BagException( e.getMessage(), e );
    }
  }

  private void produceTimingInfo( long startTime, String formatStr ) {
    if( isTimingEnabled ) {
      long totalEnd = System.nanoTime();
      session.getConsole().println( String.format( formatStr, (totalEnd - startTime) / NANOS_PER_SECOND ) );
    }
  }

  private void prepareArchiveLocation( String outputLocation ) {
    File outputDir = new File( outputLocation );
    outputDir.mkdirs();
    if( !outputDir.isDirectory() ) {
      session.getConsole().println( "Output location needs to be a directory..." );
      throw new IllegalArgumentException( "Output location is not a directory" );
    }
    archiveDir = new File( outputDir, "imports" );
    archiveDir.mkdirs();
    errorDir = new File( outputDir, "errors" );
    errorDir.mkdirs();
  }

  private void prepareStorageLocation( String storageLocation ) {
    File storageDir = new File( storageLocation );
    storageDir.mkdirs();
    if( !storageDir.isDirectory() ) {
      session.getConsole().println( "Triemap location needs to be a directory..." );
      throw new IllegalArgumentException( "Triemap location is not a directory" );
    }
  }

  private File[] prepareImportLocation( String importLocation ) {
    File[] importFiles;
    File location = new File( importLocation );
    if( !location.exists() ) {
      String message = String.format( "Import not found: %s", location );
      session.getConsole().println( message );
      throw new IllegalArgumentException( message );
    }
    if( location.isDirectory() ) {
      session.getConsole().println( "Importing directory..." );
      importFiles = location.listFiles();
    } else {
      session.getConsole().println( "Importing single file..." );
      importFiles = new File[] { location };
    }

    getImporter().addMetaInfo( "importLocation", importLocation );

    return importFiles;
  }

  private void bewaarLaatsteMutatieDatum( Tijdvakgeldigheid tijdvakGeldigheid ) {
    // Check of er een begin datum is
    String waarde = tijdvakGeldigheid.getBegindatumTijdvakGeldigheid();
    if( waarde != null && waarde.length() > 0 && laatsteMutatieDatum.compareTo( waarde ) <= 0 ) {
      laatsteMutatieDatum = waarde;
    }

    // Check of er een eind datum is
    waarde = tijdvakGeldigheid.getEinddatumTijdvakGeldigheid();
    if( waarde != null && waarde.length() > 0 && laatsteMutatieDatum.compareTo( waarde ) <= 0 ) {
      laatsteMutatieDatum = waarde;
    }
  }
}
