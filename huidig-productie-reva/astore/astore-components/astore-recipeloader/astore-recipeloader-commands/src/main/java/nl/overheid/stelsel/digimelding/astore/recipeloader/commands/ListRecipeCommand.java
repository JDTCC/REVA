package nl.overheid.stelsel.digimelding.astore.recipeloader.commands;

import java.util.Collection;

import nl.overheid.stelsel.digimelding.astore.recipeloader.model.DefaultRecipeDetails;
import nl.overheid.stelsel.digimelding.astore.recipeloader.model.DefaultRecipeDetailsList;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command( scope = "recipeloader", name = "list", description = "Lists recipes in various details." )
public class ListRecipeCommand extends RecipeLoaderCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private Logger logger = LoggerFactory.getLogger( getClass() );

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option( name = "-d", aliases = { "--details" }, description = "Indicates whether to show detailed output.", required = false, multiValued = false )
  private boolean detailed;

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument( index = 0, name = "bundleId", description = "Only list recipes owned by the bundle with the given bundle.", required = false, multiValued = false )
  private Long bundleId;

  // -------------------------------------------------------------------------
  // AbstractAction overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    Object response = null;
    Collection<UriRef> recipeIds;

    try {
      if ( bundleId == null ) {
        recipeIds = getRecipeLoaderService().listDefaults();
      } else {
        recipeIds = getRecipeLoaderService().listDefaults( bundleId );        
      }

      response = recipeIds;
      if ( detailed ) {
        response = buildDetailedResponse( recipeIds );
      }
      
      // Show results on console.
      session.getConsole().println( response.toString() );
    } catch( Exception ex ) {
      session.getConsole().println( ex.getMessage() );
      logger.warn( ex.getMessage(), ex );
      return null;
    }
        
    return null;
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------
  
  private Object buildDetailedResponse( Collection<UriRef> recipeIds ) {
    DefaultRecipeDetailsList list = new DefaultRecipeDetailsList();
    for( UriRef recipeId : recipeIds ) {
      boolean modified = getRecipeLoaderService().isModified( recipeId );
      long bundleId = getRecipeLoaderService().getOwner( recipeId );
      DefaultRecipeDetails details = new DefaultRecipeDetails( recipeId, bundleId, modified );
      list.add( details );
    }
    return list;
  }
}
