package nl.overheid.stelsel.gba.reva.web.pages.confirmation;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import nl.overheid.stelsel.gba.reva.web.pages.confirmation.ConfirmationButton.ConfirmationAnswer;

@SuppressWarnings( "serial" )
public class ConfirmationPanel extends Panel {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final int MODAL_HEIGTH = 350;
  private static final int MODAL_WIDTH = 110;

  // -------------------------------------------------------------------------
  // Object members
  // -------------------------------------------------------------------------

  private String acceptCaption;
  private String rejectCaption;

  private ModalWindow modalWindow;
  private AjaxButton acceptButton;
  private AjaxButton rejectButton;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public ConfirmationPanel( String id, String message, final ModalWindow modalWindow, final ConfirmationAnswer answer ) {
    super( id );

    Form<?> confirmationForm = new Form<Object>( "confirmationForm" );

    MultiLineLabel messageLabel = new MultiLineLabel( "confirmationMessage", message );
    confirmationForm.add( messageLabel );
    modalWindow.setTitle( "Please confirm" );
    modalWindow.setInitialHeight( MODAL_WIDTH );
    modalWindow.setInitialWidth( MODAL_HEIGTH );
    this.modalWindow = modalWindow;

    acceptButton = new AjaxButton( "acceptButton", confirmationForm ) {
      @Override
      protected void onSubmit( AjaxRequestTarget target, Form<?> form ) {
        if( target != null ) {
          answer.setAnswer( true );
          modalWindow.close( target );
        }
      }
    };

    rejectButton = new AjaxButton( "rejectButton", confirmationForm ) {
      @Override
      protected void onSubmit( AjaxRequestTarget target, Form<?> form ) {
        if( target != null ) {
          answer.setAnswer( false );
          modalWindow.close( target );
        }
      }
    };

    confirmationForm.add( acceptButton );
    confirmationForm.add( rejectButton );

    add( confirmationForm );
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public String getPanelTitle() {
    return modalWindow.getTitle().getObject();
  }

  public void setPanelTitle( String panelTitle ) {
    modalWindow.setTitle( panelTitle );
  }

  public String getAcceptCaption() {
    return acceptCaption;
  }

  public void setAcceptCaption( String acceptCaption ) {
    this.acceptCaption = acceptCaption;
    acceptButton.setModel( new Model<String>( acceptCaption ) );
  }

  public String getRejectCaption() {
    return rejectCaption;
  }

  public void setRejectCaption( String rejectCaption ) {
    this.rejectCaption = rejectCaption;
    rejectButton.setModel( new Model<String>( rejectCaption ) );
  }
}