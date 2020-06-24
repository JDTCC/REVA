package nl.overheid.stelsel.gba.reva.web.listeners;

import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;

import nl.overheid.stelsel.gba.reva.web.locators.TimingServiceLocator;

/**
 * Request cycle listener to time the request duration.
 * 
 */
public class TimingListener extends AbstractRequestCycleListener {

  @Override
  public void onBeginRequest( RequestCycle cycle ) {
    TimingServiceLocator.getService().startSession();
  }

  @Override
  public void onEndRequest( RequestCycle cycle ) {
    TimingServiceLocator.getService().getSession().endSession();
  }
}
