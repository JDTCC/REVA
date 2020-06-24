package nl.overheid.stelsel.digimelding.astore.recipeloader;

/**
 * This exception indicates that the recipe loader was not able to load a
 * particular recipe.
 * 
 */
public class MissingRecipeException extends RuntimeException {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 3089255381494517521L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Creates a new expection with the given message and cause.
   * 
   * @param message
   *          the message
   * @param cause
   *          the cause
   */
  public MissingRecipeException( String message, Throwable cause ) {
    super( message, cause );
  }

  /**
   * Creates a new exception to indicate that a recipe can not be loaded because
   * the bundle to load the recipe from does not exist.
   * 
   * @param bundleId
   *          the osgi bundle id
   */
  public MissingRecipeException( long bundleId ) {
    super( String.format( "Can't find bundle (%d) to load recipe from!", bundleId ) );
  }

  /**
   * Creates a new exception to indicate that a recipe can not be loaded because
   * it could not be found in the given bundle.
   * 
   * @param bundleId
   *          the osgi bundle id
   * @param resourcePath
   *          the resource path for the recipe.
   */
  public MissingRecipeException( long bundleId, String resourcePath ) {
    super( String.format( "Can't find recipe '%d' in bundle (%d)!", resourcePath, bundleId ) );
  }
}
