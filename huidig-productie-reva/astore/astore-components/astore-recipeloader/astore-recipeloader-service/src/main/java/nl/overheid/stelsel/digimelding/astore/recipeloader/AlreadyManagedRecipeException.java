package nl.overheid.stelsel.digimelding.astore.recipeloader;

import org.apache.clerezza.rdf.core.UriRef;

/**
 * This exception indicate that the recipe to be managed by the recipe loader is
 * already managed for a differen owner.
 * 
 */
public class AlreadyManagedRecipeException extends RuntimeException {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 8692066378670683292L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Creates a new exception with the given recipeId and owners.
   * 
   * @param recipeId
   *          the recipeId not known to the recipe loader.
   * @param owner
   *          the bundle id of the current owner.
   * @param newOwner
   *          the bundle id of the new owner that want to have the recipe
   *          managed.
   */
  public AlreadyManagedRecipeException( UriRef recipeId, long owner, long newOwner ) {
    super( String.format( "Recipe (%s) from bundle '%d' already ownded by '%d'",
        recipeId.toString(), newOwner, owner ) );
  }
}
