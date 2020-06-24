package nl.overheid.stelsel.digimelding.astore.recipeloader.commands;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command( scope = "recipeloader", name = "restore", description = "Restores managed recipes in the rule store to their default values." )
public class RestoreRecipeCommand extends RecipeLoaderCommand {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private Logger logger = LoggerFactory.getLogger( getClass() );

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option( name = "-b", aliases = { "bundle" }, description = "Indicates whether to restore all default recipes for the given bundle id.", required = false, multiValued = false )
  private boolean useBundle;

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument( index = 0, name = "Id", description = "The identification for either the recipe or the bundle owning recipes.", required = true, multiValued = false )
  private String identification;

  // -------------------------------------------------------------------------
  // AbstractAction overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    UriRef recipeId = ( useBundle ? null : new UriRef( identification ) );

    try {
      if ( useBundle ) {
        long bundleId = Long.parseLong( identification );
        getRecipeLoaderService().restoreDefault( bundleId );
      } else {
        getRecipeLoaderService().restoreDefault( recipeId );
      }
    } catch( Exception ex ) {
      session.getConsole().println( ex.getMessage() );
      logger.warn( ex.getMessage(), ex );
    }
    
    return null;
  }
}
