package nl.overheid.stelsel.gba.reva.web.pages.confirmation;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * Special button die een confirmation popup tot gevolg heeft.
 * 
 * 
 *         REMARK: Op basis van
 *         http://tomaszdziurko.pl/2010/02/wicket-ajax-modal
 *         -are-you-sure-window/
 */
public abstract class ConfirmationButton extends Panel {

  private static final long serialVersionUID = -2885309640432484445L;

  // -------------------------------------------------------------------------
  // Object memebers
  // -------------------------------------------------------------------------

  private ModalWindow confirmModal;
  private ConfirmationPanel confirmationPanel;
  private ConfirmationAnswer answer;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public ConfirmationButton( String id, String buttonName, String modalMessageText ) {
    super( id );
    answer = new ConfirmationAnswer( false );
    addElements( id, buttonName, modalMessageText );
  }

  // -------------------------------------------------------------------------
  // Abstract methods
  // -------------------------------------------------------------------------

  protected abstract void onAccept( AjaxRequestTarget target );

  protected abstract void onReject( AjaxRequestTarget target );

  // -------------------------------------------------------------------------
  // Protected interface
  // -------------------------------------------------------------------------

  protected void addElements( String id, String buttonName, String modalMessageText ) {

    confirmModal = createConfirmModal( id, modalMessageText );

    Form form = new Form( "confirmForm" );
    add( form );

    AjaxButton confirmButton = new AjaxButton( "confirmButton", new Model( buttonName ) ) {
      @Override
      protected void onSubmit( AjaxRequestTarget target, Form form ) {
        confirmModal.show( target );
      }
    };

    form.add( confirmButton );
    form.add( confirmModal );

  }

  protected ModalWindow createConfirmModal( String id, String modalMessageText ) {

    ModalWindow modalWindow = new ModalWindow( "modal" );
    modalWindow.setCookieName( id );
    confirmationPanel = new ConfirmationPanel( modalWindow.getContentId(), modalMessageText, modalWindow, answer );
    modalWindow.setContent( confirmationPanel );
    modalWindow.setWindowClosedCallback( new ModalWindow.WindowClosedCallback() {
      @Override
      public void onClose( AjaxRequestTarget target ) {
        if( answer.isAnswer() ) {
          onAccept( target );
        } else {
          onReject( target );
        }
      }
    } );

    return modalWindow;
  }

  // -------------------------------------------------------------------------
  // Public interface
  // -------------------------------------------------------------------------

  public ConfirmationPanel getConfirmationPanel() {
    return confirmationPanel;
  }

  @SuppressWarnings("serial")
  public static class ConfirmationAnswer implements Serializable {

    private boolean answer;

    public ConfirmationAnswer( boolean answer ) {
      this.answer = answer;
    }

    public boolean isAnswer() {
      return answer;
    }

    public void setAnswer( boolean answer ) {
      this.answer = answer;
    }
  }

}