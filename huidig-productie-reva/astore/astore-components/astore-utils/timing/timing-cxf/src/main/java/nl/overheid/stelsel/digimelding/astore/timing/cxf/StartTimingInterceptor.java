package nl.overheid.stelsel.digimelding.astore.timing.cxf;

import nl.overheid.stelsel.digimelding.astore.timing.TimingService;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.osgi.framework.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extracts the SSL-Certificat-DN from inboud messages.
 * 
 */
public class StartTimingInterceptor extends AbstractPhaseInterceptor<Message> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(StartTimingInterceptor.class);

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private TimingService timingService;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /** Simple default constructor */
  public StartTimingInterceptor() {
    super(Phase.RECEIVE);
  }

  // -------------------------------------------------------------------------
  // OSGi Blueprint lifecycle
  // -------------------------------------------------------------------------

  /**
   * Blueprint lifecycle method.
   */
  public void init() {
    logger.trace("StartTimingInterceptor:init");
  }

  /**
   * Blueprint lifecycle method.
   */
  public void destroy() {
    logger.trace("StartTimingInterceptor:destroy");
  }

  // -------------------------------------------------------------------------
  // Implementing AbstractPhaseInterceptor
  // -------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleMessage(Message message) {
    // Getting a new session automatically starts it.
    getTimingService().getSession();
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  /**
   * Retrieves the currently configured timing service.
   * 
   * @return
   */
  public TimingService getTimingService() {
    if (timingService == null) {
      throw new ServiceException("Timing service not available");
    }
    return timingService;
  }

  /**
   * Configure the timing service to be used.
   * 
   * @param service
   */
  public void setTimingService(TimingService service) {
    this.timingService = service;
  }
}
