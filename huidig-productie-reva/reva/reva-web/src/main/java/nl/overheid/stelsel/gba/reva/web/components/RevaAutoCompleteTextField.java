package nl.overheid.stelsel.gba.reva.web.components;

import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.DefaultCssAutoCompleteTextField;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * Copied from DefaultCssAutoCompleteTextField and added support for
 * AutoCompleteSettings.
 * 
 *
 * @param <T>
 */
public abstract class RevaAutoCompleteTextField<T> extends AutoCompleteTextField<T> {
  private static final long serialVersionUID = 1L;

  /**
   * Constructor.
   * 
   * @param id
   *          the component id
   */
  public RevaAutoCompleteTextField( final String id ) {
    this( id, null, null );
  }

  public RevaAutoCompleteTextField( final String id, AutoCompleteSettings settings ) {
    this( id, null, settings );
  }

  /**
   * Construct.
   * 
   * @param id
   *          the component id
   * @param model
   *          the component model
   */
  public RevaAutoCompleteTextField( final String id, final IModel<T> model ) {
    this( id, model, null );
  }

  public RevaAutoCompleteTextField( final String id, final IModel<T> model, AutoCompleteSettings settings ) {
    super( id, model, settings );
  }

  @Override
  public void renderHead( final IHeaderResponse response ) {
    super.renderHead( response );

    response.render( CssHeaderItem.forReference( new CssResourceReference( DefaultCssAutoCompleteTextField.class,
            "DefaultCssAutoCompleteTextField.css" ) ) );
  }
}