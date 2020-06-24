package nl.overheid.stelsel.digimelding.astore.remote.soap;

import java.util.Map;
import java.util.UUID;

import javax.jws.WebService;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import nl.overheid.stelsel.digimelding.astore.remote.soap.adapters.ParameterMapAdapter;

/**
 * The service interface for remote access to the annotation store.
 * 
 */
@WebService
public interface AnnotationService {

  /**
   * Retrieves an existing annotation tree from the repository.
   * 
   * @param stamId the root identifier of the tree.
   */
  String ophalen(UUID stamId);

  /**
   * Places a new annotation into the annotation store. This either create a new annotation tree or
   * adds the annotation to an existing annotation tree.
   * 
   * @param stamId the root identifier of the tree.
   * @param annotation the annotation to store
   */
  void toevoegen(UUID stamId, String rdfData);

  /**
   * Executes a named query with the given name using the parameters from the given UriInfo.
   * 
   * @param queryname name of the query to execute.
   * @param uriInfo to retrieve the query parameters from.
   */
  String bevragen(String queryname,
      @XmlJavaTypeAdapter(ParameterMapAdapter.class) Map<String, String> parameters);
}
