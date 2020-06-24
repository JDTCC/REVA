package nl.overheid.stelsel.gba.reva.web.components;

import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;

import nl.overheid.stelsel.gba.reva.bag.model.BCWoonplaats;
import nl.overheid.stelsel.gba.reva.web.locators.BagServiceLocator;
import nl.overheid.stelsel.gba.reva.web.validators.WoonplaatsValidator;

/**
 * A {@link TextField} for HTML5 &lt;input&gt;.
 * 
 * <p>
 * Automatically validates that the input is a woonplaats.
 * </p>
 * <p>
 * <strong>Note</strong>: This component does <strong>not</strong> support
 * multiple values!
 */
public class WoonplaatsTextField extends RevaAutoCompleteTextField<String> {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  private static final int DEFAULT_MAX_CHOICES = 25;

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private static Integer maxChoices = DEFAULT_MAX_CHOICES;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Construct.
   * 
   * @param id
   *          component id
   * @param woonplaatsInvoer
   *          the email input value
   */
  public WoonplaatsTextField( String id, final String woonplaatsInvoer ) {
    this( id, new Model<String>( woonplaatsInvoer ) );
  }

  /**
   * Construct.
   * 
   * @param id
   *          see Component
   * @param model
   *          the model
   */
  public WoonplaatsTextField( String id, IModel<String> model ) {
    this( id, model, WoonplaatsValidator.getInstance() );
  }

  /**
   * Construct.
   * 
   * @param id
   *          see Component
   */
  public WoonplaatsTextField( String id ) {
    this( id, null, WoonplaatsValidator.getInstance() );
  }

  /**
   * Construct.
   * 
   * @param id
   *          the component id
   * @param model
   *          the input value
   * @param woonplaatsValidator
   *          the validator that will check the correctness of the input value
   */
  public WoonplaatsTextField( String id, IModel<String> model, IValidator<String> woonplaatsValidator ) {
    super( id, model );

    add( woonplaatsValidator );

    add( new AjaxFormComponentUpdatingBehavior( "onblur" ) {
      private static final long serialVersionUID = 4493898120986691862L;

      @Override
      protected void onUpdate( AjaxRequestTarget target ) {
        // Nothing to do, we just want the model to be updated.
      }
    } );
  }

  // -------------------------------------------------------------------------
  // Implementing AutoCompleteTextField
  // -------------------------------------------------------------------------

  @Override
  protected Iterator<String> getChoices( String input ) {
    final Collection<BCWoonplaats> woonplaatsen = BagServiceLocator.getService().getWoonplaatsen( input );

    return new Iterator<String>() {
      private int counter = 0;
      private Iterator<BCWoonplaats> woonplaatIterator = woonplaatsen.iterator();

      @Override
      public boolean hasNext() {
        return counter < maxChoices ? woonplaatIterator.hasNext() : false;
      }

      @Override
      public String next() {
        counter++;
        return woonplaatIterator.next().getWoonplaatsNaam();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

}
