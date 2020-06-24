package nl.overheid.stelsel.gba.reva.web.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.ajax.WicketEventJQueryResourceReference;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * Menu panel voor het menu boven aan vrijwel elke web pagina.
 * 
 */
public class MenuPanel extends Panel {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = -6012210053885537876L;

  private static final ResourceReference JS = new JavaScriptResourceReference( MenuPanel.class, "MenuPanel.js" ) {

    /**
       */
    private static final long serialVersionUID = 1L;

    @Override
    public Iterable<? extends HeaderItem> getDependencies() {
      return Arrays.asList( JavaScriptHeaderItem.forReference( WicketEventJQueryResourceReference.get() ) );
    };
  };

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final String ID_MENUITEM = "menuitem";
  private static final String ID_MENUITEM_LINK = "menuitem-link";
  private static final String ID_SUB_MENUITEM = "sub-menuitem";
  private static final String ID_SUB_MENUITEM_LINK = "sub-menuitem-link";
  private static final String CURRENT_ATTRIBUTE = "current";

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public MenuPanel( String id ) {
    super( id );

    List<MenuItem> list = MenuBuilder.build();
    @SuppressWarnings( "serial" )
    ListView<MenuItem> menu = new ListView<MenuItem>( ID_MENUITEM, list ) {
      @Override
      protected void populateItem( ListItem<MenuItem> item ) {
        MenuItem menuItem = addMenuItem( item, ID_MENUITEM_LINK );

        List<MenuItem> subitems = new ArrayList<>( menuItem );
        ListView<MenuItem> submenu = new ListView<MenuItem>( ID_SUB_MENUITEM, subitems ) {
          @Override
          protected void populateItem( ListItem<MenuItem> item ) {
            addMenuItem( item, ID_SUB_MENUITEM_LINK );
          }
        };
        submenu.setVisible( !menuItem.isEmpty() );
        item.add( submenu );
      }
    };
    add( menu );
  }

  // -------------------------------------------------------------------------
  // Overrides
  // -------------------------------------------------------------------------

  @Override
  public void renderHead( IHeaderResponse response ) {
    super.renderHead( response );

    response.render( JavaScriptHeaderItem.forReference( JS ) );
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private MenuItem addMenuItem( final ListItem<MenuItem> item, final String id ) {
    MenuItem menuItem = item.getModelObject();
    Link<Object> link = new BookmarkablePageLink<Object>( id, menuItem.getPageClass() );
    link.setBody( new StringResourceModel( menuItem.getId(), null ) );
    item.add( link );

    if( menuItem.linksTo( getPage() ) ) {
      markCurrent( item );
    }

    // Security related
    if( menuItem instanceof SecuredMenuItem ) {
      SecuredMenuItem securedMenuItem = SecuredMenuItem.class.cast( menuItem );
      item.setVisible( securedMenuItem.isPermitted( SecurityUtils.getSubject() ) );
    }

    return menuItem;
  }

  @SuppressWarnings( "unchecked" )
  public void markCurrent( ListItem<MenuItem> item ) {
    MenuItem menuItem = item.getModelObject();

    if( menuItem.getParent() == null ) {
      // Markeer hoofdmenu item
      item.get( ID_MENUITEM_LINK ).add( new AttributeAppender( "class", new Model<String>( CURRENT_ATTRIBUTE ), " " ) );
    }

    if( menuItem.getParent() != null ) {
      // Markeer submenu item
      item.get( ID_SUB_MENUITEM_LINK ).add( new AttributeAppender( "class", new Model<String>( CURRENT_ATTRIBUTE ), " " ) );

      // Niet het hoogste menu item, dan 1 niveau hoger ook markeren.
      markCurrent( ListItem.class.cast( item.getParent().getParent() ) );
    }
  }
}