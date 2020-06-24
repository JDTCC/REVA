package org.apache.stanbol.rules.rulestore.commands;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.stanbol.rules.base.api.Recipe;
import org.apache.stanbol.rules.base.api.Rule;
import org.apache.stanbol.rules.rulestore.commands.decorators.RulePrettyDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command( scope = "rulestore", name = "getRule", description = "Gets the specified rule of the given recipe in the RuleStore." )
public class RuleStoreGetRuleCommand extends RuleStoreCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private Logger logger = LoggerFactory.getLogger( getClass() );

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option( name = "-n", aliases = { "--name" }, description = "Use the rule's name instead of id.", required = false, multiValued = false )
  private boolean byName;

  @Option( name = "-p", aliases = { "--pretty" }, description = "Prettify the rule output.", required = false, multiValued = false )
  private boolean prettyOutput;

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument( index = 0, name = "recipeId", description = "The recipe identification.", required = true, multiValued = false )
  private String recipeIdStr;

  @Argument( index = 1, name = "ruleId", description = "The rule identification or name.", required = true, multiValued = false )
  private String ruleIdStr;

  // -------------------------------------------------------------------------
  // AbstractAction overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    Rule rule = null;
    UriRef recipeId = new UriRef( recipeIdStr );

    try {
      Recipe recipe = getRuleStore().getRecipe( recipeId );
      if ( byName ) {
        rule = getRuleStore().getRule( recipe, ruleIdStr );
      } else {
        UriRef ruleId = new UriRef( ruleIdStr );
        rule = getRuleStore().getRule( recipe, ruleId );
      }

      if ( rule != null && prettyOutput ) {
        // Use pretty output decorator to improve output.
        rule = new RulePrettyDecorator( rule );
      }
      
      // Show results on console.
      session.getConsole().println( rule.toString() );
    } catch( Exception ex ) {
      session.getConsole().println( ex.getMessage() );
      logger.warn( ex.getMessage(), ex );
      return null;
    }
          
    return null;
  }
}
