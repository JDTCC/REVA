package org.apache.stanbol.rules.rulestore.commands;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.stanbol.rules.base.api.Recipe;
import org.apache.stanbol.rules.base.api.util.RuleList;
import org.apache.stanbol.rules.rulestore.commands.decorators.RuleListDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command( scope = "rulestore", name = "listRules", description = "Lists the rules in the specified recipe." )
public class RuleStoreListRulesCommand extends RuleStoreCommand {

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

  @Argument( index = 0, name = "recipeId", description = "The recipe identification.", required = true, multiValued = false )
  private String recipeIdStr;

  // -------------------------------------------------------------------------
  // NamedQueryCommand overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    Object result;

    try {
      Recipe recipe = getRuleStore().getRecipe( new UriRef( recipeIdStr ) );
      if ( detailed ) {
        RuleList rules = getRuleStore().listRules( recipe );
        result = new RuleListDecorator( rules );
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
