package nl.overheid.stelsel.digimelding.astore.recipeloader.commands;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command( scope = "recipeloader", name = "getowner", description = "Gives the bundle id of the owner of the recipe with the given id." )
public class OwnerRecipeCommand extends RecipeLoaderCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private Logger logger = LoggerFactory.getLogger( getClass() );

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument( index = 0, name = "recipeId", description = "The recipe Id to find the owner for.", required = true, multiValued = false )
  private String recipeIdStr;

  // -------------------------------------------------------------------------
  // AbstractAction overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    Long result = null;
    UriRef recipeId = new UriRef( recipeIdStr );

    try {
      result = getRecipeLoaderService().getOwner( recipeId );
    } catch( Exception ex ) {
      session.getConsole().println( ex.getMessage() );
      logger.warn( ex.getMessage(), ex );
    }
    
    return result;
  }
}
