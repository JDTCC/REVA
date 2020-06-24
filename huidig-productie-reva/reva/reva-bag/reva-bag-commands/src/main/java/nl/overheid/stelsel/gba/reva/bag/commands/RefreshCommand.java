package nl.overheid.stelsel.gba.reva.bag.commands;

import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Command( scope = "bag", name = "refresh", description = "Refreshes the bag service." )
public class RefreshCommand extends OsgiCommandSupport {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger( RefreshCommand.class );

  // -------------------------------------------------------------------------
  // AbstractAction overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    logger.info( "Refreshing BAG..." );
    try {
      BagServiceLocator.getBagService( getBundleContext() ).reset();
    } catch( Exception ex ) {
      session.getConsole().println( ex.getMessage() );
      logger.warn( ex.getMessage(), ex );
    }
    logger.info( "Done" );
    return null;
  }
}
