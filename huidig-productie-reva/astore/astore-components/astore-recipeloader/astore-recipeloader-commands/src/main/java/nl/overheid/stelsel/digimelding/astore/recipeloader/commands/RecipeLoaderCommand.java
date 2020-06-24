package nl.overheid.stelsel.digimelding.astore.recipeloader.commands;

import nl.overheid.stelsel.digimelding.astore.recipeloader.RecipeLoaderService;

import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.osgi.framework.ServiceException;

public abstract class RecipeLoaderCommand extends OsgiCommandSupport {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private RecipeLoaderService recipeLoaderService;

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public RecipeLoaderService getRecipeLoaderService() {
    if( recipeLoaderService == null ) {
    throw new ServiceException( "RecipeLoader service not available" );
    }
    return recipeLoaderService;
  }

  public void setRecipeLoaderService( RecipeLoaderService service ) {
    this.recipeLoaderService = service;
  }
}
