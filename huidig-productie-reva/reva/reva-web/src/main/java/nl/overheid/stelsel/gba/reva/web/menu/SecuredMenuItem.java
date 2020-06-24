package nl.overheid.stelsel.gba.reva.web.menu;

import java.util.Arrays;

import org.apache.shiro.subject.Subject;
import org.apache.wicket.Page;

import nl.overheid.stelsel.gba.reva.web.security.RevaPermissions;
import nl.overheid.stelsel.gba.reva.web.security.RevaRoles;

public class SecuredMenuItem extends SimpleMenuItem {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 6329100883175990687L;

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private RevaRoles[] roles = null;
  private RevaPermissions[] permissions = null;

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
  public <C extends Page> SecuredMenuItem( String id, final Class<C> pageClass, RevaRoles[] roles, RevaPermissions[] permissions ) {
    this( id, pageClass, null, roles, permissions );
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
  public <C extends Page> SecuredMenuItem( String id, final Class<C> pageClass, MenuItem parent, RevaRoles[] roles,
          RevaPermissions[] permissions ) {
    super( id, pageClass, parent );

    this.roles = (roles != null ? roles.clone() : null);
    this.permissions = (permissions != null ? permissions.clone() : null);
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  /**
   * Geeft aan of de opgegeven Subject permissie voor het menu item heeft.
   * 
   * @param subject
   *          het subject
   * @return True indien permissie, false in alle andere gevallen.
   */
  public boolean isPermitted( Subject subject ) {
    // Check rollen
    boolean heeftToegestaneRol = false;
    for( RevaRoles role : Arrays.asList( roles ) ) {
      if( subject.hasRole( role.getRole() ) ) {
        heeftToegestaneRol = true;
      }
    }

    // Check permissies
    boolean heeftToegestanePermissie = false;
    for( RevaPermissions permission : Arrays.asList( permissions ) ) {
      if( subject.isPermitted( permission.getStringPermission() ) ) {
        heeftToegestanePermissie = true;
      }
    }

    return heeftToegestaneRol || heeftToegestanePermissie;
  }

  // -------------------------------------------------------------------------
  // Getters
  // -------------------------------------------------------------------------

  /**
   * Geeft aan voor welke rollen het menuitem zichtbaar is.
   * 
   * @return Array met rollen, null indien geen rollen
   */
  public RevaRoles[] getRoles() {
    return roles;
  }

  /**
   * Geeft aan voor welke permissies het menuitem zichtbaar is.
   * 
   * @return Array met permissies, null indien geen permissies.
   */
  public RevaPermissions[] getPermissions() {
    return permissions;
  }
}
