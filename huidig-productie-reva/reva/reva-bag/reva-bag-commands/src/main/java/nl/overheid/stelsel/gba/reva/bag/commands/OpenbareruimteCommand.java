package nl.overheid.stelsel.gba.reva.bag.commands;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.overheid.stelsel.gba.reva.bag.model.BCOpenbareRuimte;

@Command( scope = "bag", name = "openbareruimte", description = "Get/check openbare ruimte against bag service." )
public class OpenbareruimteCommand extends OsgiCommandSupport {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger( OpenbareruimteCommand.class );

  // -------------------------------------------------------------------------
  // Command Options
  // -------------------------------------------------------------------------

  @Option( name = "-c", aliases = { "--check" }, description = "Check whether the given openbare ruimte exists.", required = false, multiValued = false )
  private boolean isCheck;

  // -------------------------------------------------------------------------
  // Command Arguments
  // -------------------------------------------------------------------------

  @Argument( index = 0, name = "woonplaats", description = "The whole or partial woonplaatsnaam to get/check.", required = true, multiValued = false )
  private String woonplaats;

  @Argument( index = 1, name = "openbareruimte", description = "The whole or partial openbare ruimte to get/check.", required = true, multiValued = false )
  private String openbareruimte;

  // -------------------------------------------------------------------------
  // AbstractAction overrides
  // -------------------------------------------------------------------------

  @Override
  protected Object doExecute() throws Exception {
    Object obj = null;
    try {
      if( isCheck ) {
        obj = BagServiceLocator.getBagService( getBundleContext() ).isOpenbareruimtenaam( woonplaats, openbareruimte );
      } else {

        Collection<BCOpenbareRuimte> openbareruimtes = BagServiceLocator.getBagService( getBundleContext() ).getOpenbareruimtes(
                woonplaats, openbareruimte );
        Collection<String> openbareruimteTabel = new ArrayList<>();
        for( BCOpenbareRuimte bcOpenbareruimte : openbareruimtes ) {
          openbareruimteTabel.add( bcOpenbareruimte.getOpenbareRuimteNaam() );
        }
        obj = openbareruimteTabel;
      }
    } catch( Exception ex ) {
      session.getConsole().println( ex.getMessage() );
      logger.warn( ex.getMessage(), ex );
    }

    return obj;
  }
}
