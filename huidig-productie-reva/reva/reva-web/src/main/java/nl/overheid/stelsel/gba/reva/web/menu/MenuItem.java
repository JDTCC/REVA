package nl.overheid.stelsel.gba.reva.web.menu;

import java.util.Collection;

import org.apache.wicket.Page;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * MenuItem iterface to represent a single menu item.
 * 
 */
public interface MenuItem extends Collection<MenuItem> {

  /**
   * Gives the id for the menu item.
   * 
   * @return
   */
  String getId();

  /**
   * Gives the parent menu item for this menu item.
   * 
   * @return
   */
  MenuItem getParent();

  /**
   * Indicates whether this menu item is the currently selected item.
   * 
   * @return True is it the currently selected menu item, false otherwise.
   */
  boolean isCurrent();

  /**
   * Get the page class registered with the link
   * 
   * @return Page class
   */
  Class<? extends Page> getPageClass();

  /**
   * @return page parameters
   */
  PageParameters getPageParameters();

  /**
   * Whether this link refers to the given page.
   * 
   * @param page
   *          the page
   */
  boolean linksTo( final Page page );
}
