package nl.overheid.stelsel.gba.reva.web.resources;

import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

abstract class BaseQueryResourceReference<T extends IResource> extends ResourceReference {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public BaseQueryResourceReference( Key key ) {
    super( key );
  }

  // -------------------------------------------------------------------------
  // ResourceReference overrides
  // -------------------------------------------------------------------------

  @Override
  public IResource getResource() {
    final Request request = RequestCycle.get().getRequest();
    // Wicket uses the same singleton instance also for detecting
    // if the given resource can be cached by the server - we don't need that,
    // and the only way to skip that test is to check the URL,
    // because the check is done by Wicket during forUrl() call,
    // within the context of a different request.
    if( !request.getUrl().getPath().startsWith( "query" ) ) {
      return null;
    }

    return getQueryResource();
  }

  // -------------------------------------------------------------------------
  // Abstract Interface
  // -------------------------------------------------------------------------
  
  protected abstract T getQueryResource();
}
