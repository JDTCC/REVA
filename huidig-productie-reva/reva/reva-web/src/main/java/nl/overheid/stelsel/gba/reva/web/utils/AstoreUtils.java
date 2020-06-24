package nl.overheid.stelsel.gba.reva.web.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import org.apache.clerezza.rdf.core.impl.SimpleMGraph;
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat;
import org.osgi.framework.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.gba.reva.web.locators.AnnotationStoreServiceLocator;
import nl.overheid.stelsel.gba.reva.web.locators.RdfParserServiceLocator;
import nl.xup.template.Bindings;
import nl.xup.template.Template;

/**
 * Simpele utility class voor het converteren van date object naar ISO 8601
 * string representatie van die objecten.
 * 
 */
public final class AstoreUtils {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger( AstoreUtils.class );

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Hide utility class constructor
   */
  private AstoreUtils() {
    // prevent instantiation
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------
  /**
   * Store data in the Reva AStore repository.
   * 
   * @param id
   *          the id under which to store the data.
   * @param bindings
   *          the actual data used for the template.
   * @param template
   *          the template to result in annotation data.
   * @throws Exception
   */
  public static void store( UUID id, Bindings bindings, Template template ) throws Exception {
    // construct response from template.
    InputStream is = null;
    String content = null;
    try {
      content = template.execute( bindings );
      is = new ByteArrayInputStream( content.getBytes( "UTF-8" ) );
    } catch( Exception e ) {
      throw new ServiceException( e.getMessage(), e );
    }

    Annotation annotation = new Annotation();
    annotation.setStam( id );
    annotation.setGraph( new SimpleMGraph() );
    try {
      annotation.getGraph().addAll( RdfParserServiceLocator.getService().parse( is, SupportedFormat.RDF_XML ) );
    } catch ( Exception e ) {
      logger.error( "Failed to parse annotation data:" + content );
      throw new ServiceException( e.getMessage(), e );
    }

    AnnotationStoreServiceLocator.getService().put( annotation );
  }

}
