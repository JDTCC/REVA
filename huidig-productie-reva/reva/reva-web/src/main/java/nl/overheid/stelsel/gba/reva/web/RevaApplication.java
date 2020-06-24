package nl.overheid.stelsel.gba.reva.web;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.UUID;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.wicket.Application;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceException;
import org.wicketstuff.shiro.annotation.AnnotationsShiroAuthorizationStrategy;
import org.wicketstuff.shiro.authz.ShiroUnauthorizedComponentListener;
import org.wicketstuff.shiro.page.LogoutPage;

import nl.overheid.stelsel.digimelding.astore.AnnotationStoreService;
import nl.overheid.stelsel.gba.reva.web.converters.ISO8601DateConverter;
import nl.overheid.stelsel.gba.reva.web.converters.UUIDConverter;
import nl.overheid.stelsel.gba.reva.web.listeners.TimingListener;
import nl.overheid.stelsel.gba.reva.web.pages.HomePage;
import nl.overheid.stelsel.gba.reva.web.pages.LoginPage;
import nl.overheid.stelsel.gba.reva.web.pages.error.ErrorPage;
import nl.overheid.stelsel.gba.reva.web.resources.QueryAsXlsResourceReference;
import nl.overheid.stelsel.gba.reva.web.security.AccessDeniedPage;

/**
 * Reva application.
 * 
 */
public class RevaApplication extends WebApplication {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private BundleContext bundleContext;
  private AnnotationStoreService annotationStoreService;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public RevaApplication() {
  }

  // -------------------------------------------------------------------------
  // WebApplication Overrides
  // -------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<HomePage> getHomePage() {
    return HomePage.class;
  }

  /**
   * Covariant override for easy getting the current {@link RevaApplication}
   * without having to cast it.
   */
  public static RevaApplication get() {
    Application application = Application.get();

    if( !(application instanceof RevaApplication) ) {
      throw new WicketRuntimeException( "The application attached to the current thread is not a "
              + RevaApplication.class.getSimpleName() );
    }

    return RevaApplication.class.cast( application );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IConverterLocator newConverterLocator() {
    ConverterLocator defaultLocator = new ConverterLocator();
    defaultLocator.set( UUID.class, new UUIDConverter() );
    defaultLocator.set( Date.class, new ISO8601DateConverter() );
    return defaultLocator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void init() {
    super.init();

    getApplicationSettings().setInternalErrorPage( ErrorPage.class );

    // Enable Shiro security
    AnnotationsShiroAuthorizationStrategy authz = new AnnotationsShiroAuthorizationStrategy();
    getSecuritySettings().setAuthorizationStrategy( authz );
    getSecuritySettings().setUnauthorizedComponentInstantiationListener(
            new ShiroUnauthorizedComponentListener( LoginPage.class, AccessDeniedPage.class, authz ) );
    exceptionHandling();

    mountPage( "/logout", LogoutPage.class );
    mountResource( "/query/${queryName}", new QueryAsXlsResourceReference() );

    getMarkupSettings().setAutomaticLinking( true );
    getMarkupSettings().setStripWicketTags( true );

    getRequestCycleListeners().add( new TimingListener() );
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

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

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private void exceptionHandling() {
    // Handle Apache Shiro authorization exceptions.
    getRequestCycleListeners().add( new AbstractRequestCycleListener() {
      @Override
      public IRequestHandler onException( RequestCycle cycle, Exception ex ) {
        if( ex instanceof WicketRuntimeException ) {
          Throwable e = ex;
          e = ex.getCause();
          if( e instanceof InvocationTargetException ) {
            e = InvocationTargetException.class.cast( e ).getTargetException();
            if( e instanceof AuthorizationException ) {
              return new RenderPageRequestHandler( new PageProvider( AccessDeniedPage.class ) );
            }
          }
        }

        return super.onException( cycle, ex );
      }
    } );
  }
}
