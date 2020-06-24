package nl.overheid.stelsel.digimelding.astore.remote.web;

import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.clerezza.utils.osgi.BundlePathNode;
import org.apache.clerezza.web.fileserver.FileServer;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wymiwyg.commons.util.dirbrowser.PathNode;

/**
 * Javascript resources for AStore rest interface.
 * 
 */
@Path("/staticweb")
public class StaticResources {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public static final String STATICWEB = "/staticweb";

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private Bundle bundle;
  private FileServer fileServer;

  // -------------------------------------------------------------------------
  // OSGi Blueprint lifecycle
  // -------------------------------------------------------------------------

  public void init() {
    logger.trace("StaticResources:init");

    URL resourceDir = getClass().getResource("staticweb");
    PathNode pathNode = new BundlePathNode(getBundle(), resourceDir.getPath());
    logger.debug("Initializing file server for {} ({})", resourceDir, resourceDir.getFile());
    fileServer = new FileServer(pathNode);
  }

  public void destroy() {
    logger.trace("StaticResources:destroy");
  }

  // -------------------------------------------------------------------------
  // Interface Methods (Rest)
  // -------------------------------------------------------------------------

  /**
   * Returns a PathNode of a static file from the staticweb folder.
   * 
   * @return {@link PathNode}
   */
  @GET
  @Path("{path:.+}")
  public PathNode getStaticFile(@PathParam("path") String path) {
    final PathNode node = fileServer.getNode(path);
    return node;
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public Bundle getBundle() {
    return bundle;
  }

  public void setBundle(Bundle bundle) {
    this.bundle = bundle;
  }
}
