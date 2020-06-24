package nl.overheid.stelsel.digimelding.astore.recipeloader;

import org.apache.clerezza.rdf.core.UriRef;

/**
 * This exception indicate that the recipe loader does not manage a certain
 * recipe.
 * 
 */
public class UnmanagedRecipeException extends RuntimeException {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 8692066378670683292L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Creates a new exception with the given recipeId.
   * 
   * @param recipeId
   *          the recipeId not managed to the recipe loader.
   */
  public UnmanagedRecipeException( UriRef recipeId ) {
    super( String.format( "Recipe '%s' not managed by recipe loader!", recipeId.toString() ) );
  }
}
