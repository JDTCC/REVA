package org.apache.stanbol.rules.rulestore.commands;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.stanbol.rules.base.api.Recipe;
import org.apache.stanbol.rules.rulestore.commands.decorators.RecipePrettyDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command( scope = "rulestore", name = "getRecipe", description = "Gets the specified recipe from the RuleStore." )
public class RuleStoreGetRecipeCommand extends RuleStoreCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private Logger logger = LoggerFactory.getLogger( getClass() );

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option( name = "-p", aliases = { "--pretty" }, description = "Prettify the recipe output.", required = false, multiValued = false )
  private boolean prettyOutput;

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument( index = 0, name = "recipeId", description = "The recipe identification.", required = true, multiValued = false )
  private String recipeIdStr;

  // -------------------------------------------------------------------------
  // AbstractAction overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    Recipe recipe = null;
    UriRef recipeId = new UriRef( recipeIdStr );

    try {
      recipe = getRuleStore().getRecipe( recipeId );

      if ( recipe != null && prettyOutput ) {
        // Use pretty output decorator to improve output.
        recipe = new RecipePrettyDecorator( recipe );
      }
      
      // Show results on console.
      session.getConsole().println( recipe.toString() );
    } catch( Exception ex ) {
      session.getConsole().println( ex.getMessage() );
      logger.warn( ex.getMessage(), ex );
      return null;
    }
      
    return null;
  }
}
