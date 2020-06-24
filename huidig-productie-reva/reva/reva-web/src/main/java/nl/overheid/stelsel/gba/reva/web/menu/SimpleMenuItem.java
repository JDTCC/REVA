package nl.overheid.stelsel.gba.reva.web.menu;

import java.util.ArrayList;

import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.Strings;

/**
 * 
 */
public class SimpleMenuItem extends ArrayList<MenuItem> implements MenuItem {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = -5353324752910182399L;

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  /** Identification for this menu item */
  private String id;

  /** The parent menu item for this menu item. */
  private MenuItem parent = null;

  /** The page class that this menu item links to. */
  private final Class<? extends Page> pageClass;

  /** The parameters to pass to the class constructor when instantiated. */
  private PageParameters parameters;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Constructor.
   * 
   * @param <C>
   * 
   * @param id
   *          The non-null id of this menu item
   * @param pageClass
   *          The class of page to link to
   */
  public <C extends Page> SimpleMenuItem( String id, final Class<C> pageClass ) {
    this( id, pageClass, null, null );
  }

  /**
   * Constructor.
   * 
   * @param <C>
   * 
   * @param id
   *          The non-null id of this menu item
   * @param pageClass
   *          The class of page to link to
   * @param parameters
   *          The parameters to pass to the new page when the link is clicked
   */
  public <C extends Page> SimpleMenuItem( String id, final Class<C> pageClass, final PageParameters parameters ) {
    this( id, pageClass, parameters, null );
  }

  /**
   * Constructor.
   * 
   * @param <C>
   * 
   * @param id
   *          The non-null id of this menu item
   * @param pageClass
   *          The class of page to link to
   * @param parent
   *          The parent menu item.
   */
  public <C extends Page> SimpleMenuItem( String id, final Class<C> pageClass, MenuItem parent ) {
    this( id, pageClass, null, parent );
  }

  /**
   * Constructor.
   * 
   * @param <C>
   * 
   * @param id
   *          The non-null id of this menu item
   * @param pageClass
   *          The class of page to link to
   * @param parameters
   *          The parameters to pass to the new page when the link is clicked
   * @param parent
   *          The parent menu item.
   */
  public <C extends Page> SimpleMenuItem( final String id, final Class<C> pageClass, final PageParameters parameters,
          final MenuItem parent ) {
    if( Strings.isEmpty( id ) ) {
      throw new WicketRuntimeException( "Null or empty menu item ID's are not allowed." );
    }
    this.id = id;

    this.parameters = parameters;

    if( pageClass == null ) {
      throw new IllegalArgumentException( "Page class for menu item cannot be null" );
    } else if( !Page.class.isAssignableFrom( pageClass ) ) {
      throw new IllegalArgumentException( "Page class must be derived from " + Page.class.getName() );
    }
    this.pageClass = pageClass;

    this.parent = parent;
    if( parent != null ) {
      parent.add( this );
    }
  }

  // -------------------------------------------------------------------------
  // Implementing MenuItem
  // -------------------------------------------------------------------------

  @Override
  public String getId() {
    return id;
  }

  @Override
  public MenuItem getParent() {
    return parent;
  }

  @Override
  public boolean isCurrent() {
    getRequestCycle();

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public final Class<? extends Page> getPageClass() {
    return pageClass;
  }

  public PageParameters getPageParameters() {
    if( parameters == null ) {
      parameters = new PageParameters();
    }
    return parameters;
  }

  @Override
  public boolean linksTo( final Page page ) {
    return page.getClass() == getPageClass();
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  /**
   * Gets the active request cycle for this component
   * 
   * @return The request cycle
   */
  public final RequestCycle getRequestCycle() {
    return RequestCycle.get();
  }
}
