package org.apache.stanbol.rules.rulestore.commands;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.stanbol.rules.base.api.Recipe;
import org.apache.stanbol.rules.rulestore.commands.decorators.RecipePrettyDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command( scope = "rulestore", name = "export", description = "Exports a recipe from the RuleStore." )
public class RuleStoreExportCommand extends RuleStoreCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private Logger logger = LoggerFactory.getLogger( getClass() );

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------


  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument( index = 0, name = "recipeId", description = "The recipe identification.", required = true, multiValued = false )
  private String recipeIdStr;

  @Argument( index = 1, name = "description", description = "The recipe description.", required = false, multiValued = false )
  private String description;

  // -------------------------------------------------------------------------
  // AbstractAction overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    Recipe recipe = null;
    UriRef recipeId = new UriRef( recipeIdStr );

    try {
      // TODO
    } catch( Exception ex ) {
      session.getConsole().println( ex.getMessage() );
      logger.warn( ex.getMessage(), ex );
      return null;
    }

    return null;
  }
}
