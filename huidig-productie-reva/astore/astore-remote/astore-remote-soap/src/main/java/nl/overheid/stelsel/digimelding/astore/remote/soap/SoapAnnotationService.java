package nl.overheid.stelsel.digimelding.astore.remote.soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationStoreException;
import nl.overheid.stelsel.digimelding.astore.AnnotationStoreService;
import nl.overheid.stelsel.digimelding.astore.AnnotationTree;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.impl.SimpleMGraph;
import org.apache.clerezza.rdf.core.serializedform.Parser;
import org.apache.clerezza.rdf.core.serializedform.Serializer;
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat;
import org.apache.clerezza.rdf.core.sparql.ResultSet;
import org.osgi.framework.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The service interface for remote access to the annotation store.
 * 
 */
public class SoapAnnotationService implements AnnotationService {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(SoapAnnotationService.class);

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private AnnotationStoreService annotationStoreService;
  private Serializer serializerService;
  private Parser parserService;

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  /**
   * Retrieves an existing annotation tree from the repository.
   * 
   * @param stamId the root identifier of the tree.
   */
  @Override
  public String ophalen(UUID stamId) {
    String rdfData = null;
    try {
      AnnotationTree tree = getAnnotationStoreService().get(stamId);
      if (tree != null) {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        getSerializerService().serialize(boas, tree.getGraph(), SupportedFormat.RDF_XML);
        rdfData = boas.toString("UTF-8");
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
    }

    if (rdfData == null) {
      throw new WebApplicationException(Status.NOT_FOUND);
    }

    return rdfData;
  }

  /**
   * Places a new annotation into the annotation store. This either create a new annotation tree or
   * adds the annotation to an existing annotation tree.
   * 
   * @param stamId the root identifier of the tree.
   * @param annotation the annotation to store
   */
  @Override
  public void toevoegen(UUID stamId, String rdfData) {
    try {
      Annotation annotation = readAnnotation(rdfData);
      annotation.setStam(stamId);

      getAnnotationStoreService().put(annotation);
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Executes a named query with the given name using the parameters from the given UriInfo.
   * 
   * @param queryname name of the query to execute.
   * @param uriInfo to retrieve the query parameters from.
   */
  @Override
  public String bevragen(String queryname, Map<String, String> parameters) {
    // Execute the query.
    Object results = null;
    try {
      results = getAnnotationStoreService().namedQuery(queryname, parameters);
    } catch (AnnotationStoreException ex) {
      logger.warn(ex.getMessage(), ex);
      throw new WebApplicationException(ex, Status.BAD_REQUEST);
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
    }

    // Check if the query returns the right kind of result.
    if (!(results instanceof ResultSet)) {
      logger.warn("Incorrect query type for query '" + queryname + "'");
      // TODO: report nicely what is wrong: Query of wrong kind.
      throw new WebApplicationException(Status.BAD_REQUEST);
    }
    return "todo";
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public AnnotationStoreService getAnnotationStoreService() {
    if (annotationStoreService == null) {
      throw new ServiceException("AnnotationStoreService service not available");
    }
    return annotationStoreService;
  }

  public void setAnnotationStoreService(AnnotationStoreService service) {
    this.annotationStoreService = service;
  }

  public Serializer getSerializerService() {
    if (serializerService == null) {
      throw new ServiceException("Serializer service not available");
    }
    return serializerService;
  }

  public void setSerializerService(Serializer serializerService) {
    this.serializerService = serializerService;
  }

  public Parser getParserService() {
    if (parserService == null) {
      throw new ServiceException("Parser service not available");
    }
    return parserService;
  }

  public void setParserService(Parser parserService) {
    this.parserService = parserService;
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  protected Annotation readAnnotation(String rdfData) throws Exception {
    MGraph graph = new SimpleMGraph();
    getParserService().parse(graph, new ByteArrayInputStream(rdfData.getBytes("UTF-8")),
        SupportedFormat.RDF_XML);

    Annotation annotation = new Annotation();
    annotation.setGraph(graph);

    return annotation;
  }
}
