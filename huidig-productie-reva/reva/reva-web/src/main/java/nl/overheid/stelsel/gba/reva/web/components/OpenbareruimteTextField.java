package nl.overheid.stelsel.gba.reva.web.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.DefaultCssAutoCompleteTextField;

import nl.overheid.stelsel.gba.reva.bag.model.BCOpenbareRuimte;
import nl.overheid.stelsel.gba.reva.web.locators.BagServiceLocator;

/**
 * A {@link TextField} for HTML5 &lt;input&gt;.
 * 
 * <p>
 * Automatically validates that the input is an opebare ruimte.
 * </p>
 * <p>
 * <strong>Note</strong>: This component does <strong>not</strong> support
 * multiple values!
 */
public class OpenbareruimteTextField extends DefaultCssAutoCompleteTextField<String> {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  private static final int DEFAULT_MAX_CHOICES = 25;

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private static Integer maxChoices = DEFAULT_MAX_CHOICES;
  private String idWoonplaatsVeld;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Construct.
   * 
   * @param id
   *          see Component
   */
  public OpenbareruimteTextField( String id, String idWoonplaats ) {
    super( id );

    idWoonplaatsVeld = idWoonplaats;
  }

  // -------------------------------------------------------------------------
  // Implementing AutoCompleteTextField
  // -------------------------------------------------------------------------

  @Override
  protected Iterator<String> getChoices( String input ) {
    final Collection<BCOpenbareRuimte> woonplaatsen = new ArrayList<>();

    Component woonplaatsComponent = getForm().get( idWoonplaatsVeld );
    if( woonplaatsComponent != null ) {
      String woonplaats = woonplaatsComponent.getDefaultModelObjectAsString();
      woonplaatsen.addAll( BagServiceLocator.getService().getOpenbareruimtes( woonplaats, input ) );
    }

    return new Iterator<String>() {
      private int counter = 0;
      private Iterator<BCOpenbareRuimte> ruimteIterator = woonplaatsen.iterator();

      @Override
      public boolean hasNext() {
        return counter < maxChoices ? ruimteIterator.hasNext() : false;
      }

      @Override
      public String next() {
        counter++;
        return ruimteIterator.next().getOpenbareRuimteNaam();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

}
