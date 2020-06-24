package nl.overheid.stelsel.digimelding.astore.recipeloader.commands;

import nl.overheid.stelsel.digimelding.astore.recipeloader.model.DefaultRecipeDetails;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command( scope = "recipeloader", name = "show", description = "Show details for the recipe with the given id." )
public class ShowRecipeCommand extends RecipeLoaderCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private Logger logger = LoggerFactory.getLogger( getClass() );

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument( index = 0, name = "recipeId", description = "The recipe Id give details for.", required = true, multiValued = false )
  private String recipeIdStr;

  // -------------------------------------------------------------------------
  // NamedQueryCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    DefaultRecipeDetails details = null;
    UriRef recipeId = new UriRef( recipeIdStr );
    
    try {
      boolean modified = getRecipeLoaderService().isModified( recipeId );
      long bundleId = getRecipeLoaderService().getOwner( recipeId );
      
      details = new DefaultRecipeDetails( recipeId, bundleId, modified );

      // Show results on console.
      session.getConsole().println( details.toString() );
    } catch( Exception ex ) {
      session.getConsole().println( ex.getMessage() );
      logger.warn( ex.getMessage(), ex );
      return null;
    }
    
    return null;
  }
}
