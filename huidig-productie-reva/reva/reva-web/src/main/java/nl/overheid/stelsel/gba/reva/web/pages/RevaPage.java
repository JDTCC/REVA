package nl.overheid.stelsel.gba.reva.web.pages;

import java.util.Arrays;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.WicketEventJQueryResourceReference;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.feedback.ErrorLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.wicketstuff.shiro.page.LogoutPage;

import nl.overheid.stelsel.gba.reva.web.borders.FeedbackBorder;
import nl.overheid.stelsel.gba.reva.web.configuration.VersionConfiguration;
import nl.overheid.stelsel.gba.reva.web.menu.MenuPanel;

/**
 * Basis Reva web pagina. Vrijwel alle Reva webpagina's zijn van een afgeleide
 * van deze abstracte pagina.
 * 
 */
public abstract class RevaPage extends WebPage {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = -8464467102312223465L;

  private static final ResourceReference JS = new JavaScriptResourceReference( RevaPage.class, "RevaPage.js" ) {

    private static final long serialVersionUID = 1L;

    @Override
    public Iterable<? extends HeaderItem> getDependencies() {
      return Arrays.asList( JavaScriptHeaderItem.forReference( WicketEventJQueryResourceReference.get() ),
              JavaScriptHeaderItem.forReference( new JavaScriptResourceReference( this.getClass(), "js/jquery-ui.min.js" ) ) );
    };
  };

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private Panel headerPanel;
  private Panel menuPanel;

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  public RevaPage() {
    this( null );
  }

  public RevaPage( PageParameters parameters ) {
    super( parameters );

    if( getApplication().getDebugSettings().isDevelopmentUtilitiesEnabled() ) {
      add( new DebugBar( "debug" ) );
    } else {
      add( new EmptyPanel( "debug" ).setVisible( false ) );
    }

    add( new Label( "versionLabel", VersionConfiguration.applicationVersion() ) );
    String contextValue = VersionConfiguration.applicationContext();
    add( new Label( "contextLabel", contextValue + "-omgeving" ).setVisible( !VersionConfiguration.CONTEXT_VALUE_PROD
            .equalsIgnoreCase( contextValue ) ) );

    headerPanel = new HeaderPanel( "headerPanel" );
    menuPanel = new MenuPanel( "menuPanel" );
    menuPanel.add( new AttributeAppender( "class", new Model<String>( VersionConfiguration.applicationContext().toLowerCase() ),
            " " ) );

    // User is logged in if there is a principal.
    boolean isLoggedIn = (SecurityUtils.getSubject().getPrincipal() != null);

    add( new BookmarkablePageLink<Object>( "login", LoginPage.class ).setVisible( !isLoggedIn ) );
    add( new BookmarkablePageLink<Object>( "logout", LogoutPage.class ).setVisible( isLoggedIn ) );
    add( new BookmarkablePageLink<Object>( "help", HelpPage.class ) );

    add( headerPanel );
    add( menuPanel );

    add( new FeedbackPanel( "successMessage", new ExactErrorLevelFilter( FeedbackMessage.SUCCESS ) ) );
    add( new FeedbackPanel( "infoMessage", new ExactErrorLevelFilter( FeedbackMessage.INFO ) ) );
    add( new FeedbackPanel( "errorMessage", new ErrorLevelFeedbackMessageFilter( FeedbackMessage.WARNING ) ) );
  }

  // -------------------------------------------------------------------------
  // Overrides
  // -------------------------------------------------------------------------

  @Override
  public void renderHead( IHeaderResponse response ) {
    super.renderHead( response );

    response.render( JavaScriptHeaderItem.forReference( JS ) );
  }

  @Override
  protected void onBeforeRender() {
    super.onBeforeRender();

    addOrReplace( new Label( "screenTitleLabel", new ResourceModel( getTitleLabelResourceName() ) ) );
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  /**
   * Gives access to the default menu panel.
   * 
   * @return Wicket Component
   */
  public Panel getMenuPanel() {
    return menuPanel;
  }

  /**
   * Gives access to the default header panel.
   * 
   * @return Wicket Component
   */
  public Panel getHeaderPanel() {
    return headerPanel;
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  /**
   * Gives the property resources name to be used for retrieving the label's
   * content.
   * 
   * @return String with the resource name to be used.
   */
  public String getTitleLabelResourceName() {
    return "screenTitleLabel";
  }

  private static class ExactErrorLevelFilter implements IFeedbackMessageFilter {
    private static final long serialVersionUID = 1L;
    private int errorLevel;

    public ExactErrorLevelFilter( int errorLevel ) {
      this.errorLevel = errorLevel;
    }

    public boolean accept( FeedbackMessage message ) {
      return message.getLevel() == errorLevel;
    }
  }

  // -------------------------------------------------------------------------
  // Protected methods
  // -------------------------------------------------------------------------

  /**
   * Add a border around the given component.
   * 
   * @param borderId
   *          the markup id of the border.
   * @param component
   *          the component to put inside the border.
   * @return
   */
  protected Component borderize( String borderId, Component component ) {
    FeedbackBorder border = new FeedbackBorder( borderId );
    border.add( component );
    return border;
  }
}
