package nl.overheid.stelsel.gba.reva.web;

import org.apache.wicket.RuntimeConfigurationType;
import org.ops4j.pax.wicket.api.WebApplicationFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceException;

import nl.overheid.stelsel.digimelding.astore.AnnotationStoreService;

/**
 * Simple PaxWicket web application factory to create the RevaApplication
 * instance and inject the annotation store in the newly created application.
 * 
 * 
 * @param <T>
 */
public class RevaWebApplicationFactory<T extends RevaApplication> implements WebApplicationFactory<T> {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private Class<T> wicketApplication;
  private BundleContext bundleContext;
  private AnnotationStoreService annotationStoreService;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public RevaWebApplicationFactory() {
  }

  public RevaWebApplicationFactory( Class<T> wicketApplication ) {
    this.wicketApplication = wicketApplication;
  }

  // -------------------------------------------------------------------------
  // Implementing WebApplicationFactory
  // -------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  public Class<T> getWebApplicationClass() {
    return wicketApplication;
  }

  /**
   * {@inheritDoc}
   */
  public void onInstantiation( RevaApplication application ) {
    // Make sure the application has access to the annotation store as well.
    application.setBundleContext( getBundleContext() );
    application.setAnnotationStoreService( getAnnotationStoreService() );

    // By default the app is in deployment mode. Development mode can be
    // configured in version.cfg.
    application.setConfigurationType( RuntimeConfigurationType.DEPLOYMENT );
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public void setWebApplicationClass( Class<T> wicketApplication ) {
    this.wicketApplication = wicketApplication;
  }

  public BundleContext getBundleContext() {
    if( bundleContext == null ) {
      throw new IllegalStateException( "BundleContext not yet initialized" );
    }
    return bundleContext;
  }

  public void setBundleContext( BundleContext bundleContext ) {
    this.bundleContext = bundleContext;
  }

  public AnnotationStoreService getAnnotationStoreService() {
    if( annotationStoreService == null ) {
      throw new ServiceException( "AnnotationStoreService service not available" );
    }
    return annotationStoreService;
  }

  public void setAnnotationStoreService( AnnotationStoreService service ) {
    this.annotationStoreService = service;
  }
}
