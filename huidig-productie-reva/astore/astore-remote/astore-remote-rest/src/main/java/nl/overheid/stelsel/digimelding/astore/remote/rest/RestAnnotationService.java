/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package nl.overheid.stelsel.digimelding.astore.remote.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationStoreException;
import nl.overheid.stelsel.digimelding.astore.AnnotationStoreService;
import nl.overheid.stelsel.digimelding.astore.AnnotationTree;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.TripleCollection;
import org.apache.clerezza.rdf.core.impl.SimpleMGraph;
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat;
import org.apache.clerezza.rdf.core.sparql.ResultSet;
import org.osgi.framework.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rest service to manipulate the AStore
 * 
 */
public class RestAnnotationService {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(RestAnnotationService.class);

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private AnnotationStoreService annotationStoreService;

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  /**
   * Retrieves an existing annotation tree from the repository.
   * 
   * @param stamId the root identifier of the tree.
   */
  @GET
  @Path("annotation/{uuid}")
  @Produces({SupportedFormat.N3, SupportedFormat.N_TRIPLE, SupportedFormat.RDF_XML,
      SupportedFormat.TURTLE, SupportedFormat.X_TURTLE, SupportedFormat.RDF_JSON,
      SupportedFormat.HTML, SupportedFormat.XHTML})
  public TripleCollection retrieve(@PathParam("uuid") UUID stamId) {
    AnnotationTree tree = null;
    try {
      tree = getAnnotationStoreService().get(stamId);
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex);
      throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
    }

    if (tree == null) {
      throw new WebApplicationException(Status.NOT_FOUND);
    }

    return tree.getGraph();
  }

  /**
   * Places a new annotation into the annotation store. This either create a new annotation tree or
   * adds the annotation to an existing annotation tree.
   * 
   * @param stamId the root identifier of the tree.
   * @param annotation the annotation to store
   */
  @POST
  @Path("annotation/{uuid}")
  @Consumes({SupportedFormat.N3, SupportedFormat.N_TRIPLE, SupportedFormat.RDF_XML,
      SupportedFormat.TURTLE, SupportedFormat.X_TURTLE, SupportedFormat.RDF_JSON})
  public void add(@PathParam("uuid") UUID stamId, TripleCollection graphData) {
    logger.trace("AStoreRemoteServiceRest:put");

    try {
      MGraph graph = new SimpleMGraph();
      graph.addAll(graphData);

      Annotation annotation = new Annotation();
      annotation.setGraph(graph);
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
  @GET
  @Path("query/{name}")
  @Produces({"application/sparql-results+xml", "application/sparql-results+json",
      MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML, "text/csv",
      "text/tab-separated-values", MediaType.APPLICATION_XHTML_XML, MediaType.TEXT_HTML,
      MediaType.TEXT_PLAIN})
  public ResultSet namedQuery(@PathParam("name") String queryname, @Context UriInfo uriInfo) {
    // Convert query parameters in a usable form.
    Map<String, String> parameters = new HashMap<>(uriInfo.getQueryParameters().size());
    for (Entry<String, List<String>> entry : uriInfo.getQueryParameters().entrySet()) {
      parameters.put(entry.getKey(), entry.getValue().get(0));
    }

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
      // TODO: report nicely what is wrong: Query of wrong kind.
      throw new WebApplicationException(Status.BAD_REQUEST);
    }
    return ResultSet.class.cast(results);
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
}
