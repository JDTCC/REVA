package nl.overheid.stelsel.digimelding.astore.recipeloader;

import java.util.Collection;

import org.apache.clerezza.rdf.core.UriRef;
import org.osgi.framework.Bundle;

/**
 * A service that is able to load pre packaged Stanbol RuleStore recipes.
 * 
 */
public interface RecipeLoaderService {

  /**
   * Checks whether the recipe with the given id is already present in the
   * RuleStore.
   * 
   * @param recipeId
   *          the recipe id for which to check existence.
   * @return true if the RuleStore already contains the recipe.
   */
  boolean exists( UriRef recipeId );

  /**
   * Checks whether the recipe with the give id is also a default recipe
   * controlled by the recipe loader.
   * 
   * @param recipeId
   *          the recipe id for which to check existence.
   * @return true if recipe is known to the recipe loader, false otherwise.
   */
  boolean isDefault( UriRef recipeId );

  /**
   * Gives the owner of the recipe with the given id.
   * 
   * @param recipeId
   *          the recipe id for which to retrieve the owner.
   * @return Osgi bundle id of the bundle that owns the recipe with the given
   *         id.
   * @throws UnmanagedRecipeException
   *           when the recipe with the given Id is not manage by the recipe
   *           loader.
   */
  long getOwner( UriRef recipeId ) throws UnmanagedRecipeException;

  /**
   * Checks whether the recipe with the give id is still the same as the default
   * or whether it has been modified.
   * 
   * @param recipeId
   *          the recipe id for which to check if modified.
   * @return true if the recipe has been modified, false if the recipe in the
   *         RuleStore is still the same as the default.
   * @throws UnmanagedRecipeException
   */
  boolean isModified( UriRef recipeId ) throws UnmanagedRecipeException;

  /**
   * Manages a default recipe by loading it into the RuleStore, but only if a
   * recipe with the given Id is not yet present in the RuleStore. If a recipe
   * with the given Id is already present in the RuleStore it will NOT we
   * overwritten.
   * 
   * The recipe will be loaded from the given bundle using the given
   * resourcePath.
   * 
   * Managed default recipes will not only be loaded it into the RuleStore, but
   * the recipe loader also allows these default recipes to be reloaded on
   * request.
   * 
   * @param recipeId
   *          the recipe id for the loaded recipe.
   * @param bundle
   *          the osgi bundle from which to load the recipe.
   * @param resourcePath
   *          the resource path from which to load the recipe.
   * @return true if the recipe was actually loaded into the RuleStore, false if
   *         a recipe with the given id was already present in the RuleStore.
   * @throws MissingRecipeException
   *           when the recipe can not be found at the given resource path.
   * @throws AlreadyManagedRecipeException
   *           when the recipe is already owned by a different bundle.
   */
  boolean manageDefault( UriRef recipeId, Bundle bundle, String resourcePath )
      throws MissingRecipeException, AlreadyManagedRecipeException;

  /**
   * Restores a recipe in the RuleStore to its default. In this case a (possibly
   * modified) recipe in the RuleStore will be placed by reloading the default
   * recipe.
   * 
   * @param recipeId
   *          the recipe id of the recipe to be restored.
   * @throws UnmanagedRecipeException
   */
  void restoreDefault( UriRef recipeId ) throws UnmanagedRecipeException;

  /**
   * Restores recipes in the RuleStore to their default. In this case (possibly
   * modified) recipes in the RuleStore will be replaced by reloading the default
   * recipes from the bundle with the specified id.
   * 
   * @param bundleId
   *          the id of the bundle who's default recipes need to be restored.
   */
  void restoreDefault( long bundleId );

  /**
   * Restores recipes in the RuleStore to their default. In this case (possibly
   * modified) recipes in the RuleStore will be replaced by reloading the default
   * recipes from the given bundle.
   * 
   * @param bundle
   *          the bundle who's default recipes need to be restored.
   */
  void restoreDefault( Bundle bundle );

  /**
   * Gives a collection of all recipe id's managed by the recipe loader.
   * 
   * @return
   */
  Collection<UriRef> listDefaults();

  /**
   * Gives a collection of all recipe id's managed by the recipe loader and on
   * behalf of the given bundle.
   * 
   * @param bundleId
   *          bundle id of the bundle for which to return a list of managed
   *          recipe id's.
   * @return A collection with recipe's id owned by a bundle with the given id.
   *         If that bundle is not known to the recipe loader an empty
   *         collection is returned.
   */
  Collection<UriRef> listDefaults( long bundleId );

  /**
   * Gives a collection of all recipe id's managed by the recipe loader and on
   * behalf of the given bundle.
   * 
   * @param bundle
   *          the bundle for which to return a list of managed recipe id's.
   * @return A collection with recipe's id owned by the given bundle. If that
   *         bundle is not known to the recipe loader an empty collection is
   *         returned.
   */
  Collection<UriRef> listDefaults( Bundle bundle );

  /**
   * Release control of the recipe with the given id. This recipe will NOT be
   * removed from the RuleStore but only from the recipe loader.
   * 
   * @param recipeId
   *          the id of the recipe to be released
   * @return true if the recipe was released, false if the recipe was not
   *         released from the recipe loader. In this last case the given recipe
   *         id is not known to the recipe loader.
   */
  boolean releaseDefault( UriRef recipeId );

  /**
   * Release control of all default recipes owned by the bundlewith the given
   * id. Recipes will NOT be removed from the RuleStore but only from the recipe
   * loader.
   * 
   * @param bundleId
   *          the id of the bundle who's default recipes need to be released.
   * @return A collection with released recipe id's, this colleciton if emptyif
   *         no recipes were released from the recipe loader. In this last case
   *         the bundle with the given id did not own any default recipes.
   */
  Collection<UriRef> releaseDefaults( long bundleId );

  /**
   * Release control of all default recipes owned by the given bundle. Recipes
   * will NOT be removed from the RuleStore but only from the recipe loader.
   * 
   * @param bundle
   *          the bundle who's default recipes need to be released.
   * @return A collection with released recipe id's, this colleciton if emptyif
   *         no recipes were released from the recipe loader. In this last case
   *         the given bundle did not own any default recipes.
   */
  Collection<UriRef> releaseDefaults( Bundle bundle );
}
