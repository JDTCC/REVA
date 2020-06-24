package nl.overheid.stelsel.digimelding.astore.timing;

import java.util.Collection;

/**
 * Interface describing a complete timing session.
 * 
 */
public interface TimingSession {

  /**
   * Gives the unique identification of the session.
   * 
   * @return String containing the session id.
   */
  String getId();

  /**
   * Gives the identification of the thread that created the session.
   * 
   * @return String containing the thread id.
   */
  ThreadInfo getThreadInfo();

  /**
   * Gives the total duration of the session. If the session has not yet ended then it will return
   * the duration up until now.
   * 
   * @return a long value containing the duration in nanoseconds.
   */
  long getDuration();

  /**
   * Ends a timing session. Any pending measurements will be ended implicitly.
   * 
   * @return the total duration of the session in nanoseconds.
   */
  long endSession();

  /**
   * Indicates whether the session is still pending or not. The session is pending until it ends.
   * 
   * @return True is still pending, false otherwise.
   */
  boolean isPending();

  /**
   * Starts a measurement with the given ID. If the previous measurement has not yet been ended the
   * new measurement will automatically be create as a child of that previous measurement.
   * 
   * @param id the identification for this measurement.
   */
  TimingMeasurement startMeasurement(String id);

  /**
   * Gives a collection of all parent measurements of the session. Each of these measurements gives
   * access to its child measurements.
   * 
   * @return a possible empty Collection with all root/parent measurement of the session.
   */
  Collection<TimingMeasurement> getMeasurements();
}
