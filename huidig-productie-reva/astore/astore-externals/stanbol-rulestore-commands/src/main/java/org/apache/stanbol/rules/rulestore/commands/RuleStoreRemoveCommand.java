package org.apache.stanbol.rules.rulestore.commands;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command( scope = "rulestore", name = "remove", description = "Remove the recipe from the RuleStore." )
public class RuleStoreRemoveCommand extends RuleStoreCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private Logger logger = LoggerFactory.getLogger( getClass() );

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
    boolean isRemoved = false;
    UriRef recipeId = new UriRef( recipeIdStr );

    try {
      isRemoved = getRuleStore().removeRecipe( recipeId );
      
      String message = "";
      if ( isRemoved ) {
        message = String.format( "Removed recipe: %s", recipeId.toString() );
      } else {
        message = String.format( "Recipe '%s' not removed", recipeId.toString() );
      }
      session.getConsole().println( message );
    } catch( Exception ex ) {
      session.getConsole().println( ex.getMessage() );
      logger.warn( ex.getMessage(), ex );
      return null;
    }
      
    return isRemoved;
  }
}
