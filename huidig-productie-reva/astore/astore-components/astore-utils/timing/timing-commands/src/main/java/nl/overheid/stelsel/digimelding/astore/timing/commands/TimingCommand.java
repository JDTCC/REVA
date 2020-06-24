package nl.overheid.stelsel.digimelding.astore.timing.commands;

import nl.overheid.stelsel.digimelding.astore.timing.TimingService;

import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.osgi.framework.ServiceException;

/**
 * Base class voor Timing gerelateerde commando's.
 * 
 */
public abstract class TimingCommand extends OsgiCommandSupport {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private TimingService timingService = null;

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  /**
   * Retrieves the currently configured timing service. If no service is configured an exception
   * will be thrown.
   * 
   * @return the currently configured timing service.
   */
  public TimingService getTimingService() {
    if (timingService == null) {
      throw new ServiceException("Timing service not available");
    }
    return timingService;
  }

  /**
   * Configures the timing service to be use.d
   * 
   * @param service
   */
  public void setTimingService(TimingService service) {
    this.timingService = service;
  }
}
