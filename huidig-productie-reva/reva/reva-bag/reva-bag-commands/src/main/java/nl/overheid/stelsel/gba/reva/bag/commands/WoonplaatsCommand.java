package nl.overheid.stelsel.gba.reva.bag.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.overheid.stelsel.gba.reva.bag.model.BCWoonplaats;

@Command( scope = "bag", name = "woonplaats", description = "Get/check woonplaats against bag service." )
public class WoonplaatsCommand extends OsgiCommandSupport {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger( WoonplaatsCommand.class );

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option( name = "-c", aliases = { "--check" }, description = "Check whether the given woonplaats exists.", required = false, multiValued = false )
  private boolean isCheck;

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument( index = 0, name = "woonplaats", description = "The whole or partial woonplaatsnaam to get/check.", required = true, multiValued = false )
  private String woonplaats;

  // -------------------------------------------------------------------------
  // AbstractAction overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    Object obj = null;
    try {
      if( isCheck ) {
        obj = BagServiceLocator.getBagService( getBundleContext() ).isWoonplaatsnaam( woonplaats );
      } else {

        Collection<BCWoonplaats> woonplaatsen = BagServiceLocator.getBagService( getBundleContext() ).getWoonplaatsen( woonplaats );
        Map<String,String> woonplaatsMap = new HashMap<>();
        for( BCWoonplaats bcWoonplaats : woonplaatsen ) {
          woonplaatsMap.put( bcWoonplaats.getIdentificatie(), bcWoonplaats.getWoonplaatsNaam() );
        }
        obj = woonplaatsMap;
      }
    } catch( Exception ex ) {
      session.getConsole().println( ex.getMessage() );
      logger.warn( ex.getMessage(), ex );
    }

    return obj;
  }
}
