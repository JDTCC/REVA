package org.apache.stanbol.rules.rulestore.commands;

import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.stanbol.rules.base.api.util.RecipeList;
import org.apache.stanbol.rules.rulestore.commands.decorators.RecipeListDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command( scope = "rulestore", name = "listRecipes", description = "Lists recipes in various details." )
public class RuleStoreListRecipesCommand extends RuleStoreCommand {

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
  // NamedQueryCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    Object result;

    try {
      if ( detailed ) {
        RecipeList recipes = getRuleStore().listRecipes();
        result = new RecipeListDecorator( recipes );
      } else {
        result = getRuleStore().listRecipeIDs();
      }
      
      // Show results on console.
      session.getConsole().println( result.toString() );
    } catch( Exception ex ) {
      session.getConsole().println( ex.getMessage() );
      logger.warn( ex.getMessage(), ex );
      return null;
    }
    
    return null;
  }
}
