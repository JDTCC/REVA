package nl.overheid.stelsel.gba.reva.web.borders;

import java.util.Iterator;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackCollector;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.Model;

/**
 * Special border to give additional feedback for form fields. Feedback like
 * special indicators for required fields or input errors.
 * 
 */
public class FeedbackBorder extends Border implements IFeedback {

  // ----------------------------------------------------------------------
  // Class attributes
  // ----------------------------------------------------------------------

  private static final long serialVersionUID = 7921111699978077078L;

  // ----------------------------------------------------------------------
  // Object attributes
  // ----------------------------------------------------------------------

  private Model<String> feedbackClassModel = new Model<String>( "" );

  // ----------------------------------------------------------------------
  // Constructors
  // ----------------------------------------------------------------------

  /**
   * Constructor.
   * 
   * @param id
   *          See Component
   */
  public FeedbackBorder( final String id ) {
    super( id );
    add( AttributeModifier.append( "class", feedbackClassModel ) );
  }

  // ----------------------------------------------------------------------
  // Overrides
  // ----------------------------------------------------------------------

  @Override
  protected void onBeforeRender() {
    super.onBeforeRender();

    // Clear
    feedbackClassModel.setObject( "" );

    // Check for required sub components.
    boolean containsRequiredFormComponent = false;
    for( Iterator<?> iter = getBodyContainer().iterator(); iter.hasNext(); ) {
      Object component = iter.next();
      // Is this component a form component?
      if( component instanceof FormComponent<?> ) {
        FormComponent<?> formComponent = ((FormComponent<?>) component);
        containsRequiredFormComponent |= (formComponent.isRequired() && formComponent.isEnabled());
      }
    }
    if( containsRequiredFormComponent ) {
      feedbackClassModel.setObject( "verplicht" );
    }

    // Are there any error messages for this component?
    boolean containsErrors = new FeedbackCollector( getPage() ).collect( getMessagesFilter() ).size() > 0;
    if( containsErrors ) {
      feedbackClassModel.setObject( "fout" );
    }
  }

  // ----------------------------------------------------------------------
  // Protected methods
  // ----------------------------------------------------------------------

  /**
   * @return Let subclass specify some other filter
   */
  protected IFeedbackMessageFilter getMessagesFilter() {
    return new ContainerFeedbackMessageFilter( this );
  }
}
