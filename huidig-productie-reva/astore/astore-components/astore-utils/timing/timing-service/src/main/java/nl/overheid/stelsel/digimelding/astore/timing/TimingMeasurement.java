package nl.overheid.stelsel.digimelding.astore.timing;

import java.util.Collection;

/**
 * Interface describing a complete timing session.
 * 
 */
public interface TimingMeasurement {

  /**
   * Gives the unique identification of the session.
   * 
   * @return String containing the session id.
   */
  String getId();

  /**
   * Gives the total duration of the measurement. If the measurement has not yet ended then it will
   * return the duration up until now.
   * 
   * @return a long value containing the duration in nanoseconds.
   */
  long getDuration();

  /**
   * Ends the measurement.
   * 
   * @return the total duration of the measurement in nanoseconds.
   */
  long endMeasurement();

  /**
   * Indicates whether the measurement is still pending or not. The measurement is pending until it
   * ends.
   * 
   * @return True is still pending, false otherwise.
   */
  boolean isPending();

  /**
   * Gives the parent measurement for child measurements. Top level measurements have no parent a
   * wil return null.
   * 
   * @return The parent measurement object. Null if no parent present.
   */
  TimingMeasurement getParent();

  /**
   * Starts a measurement with the given ID. If the previous measurement has not yet been ended the
   * new measurement will automatically be create as a child of that previous measurement.
   * 
   * @param id the identification for this measurement.
   */
  TimingMeasurement startChildMeasurement(String id);

  /**
   * Gives a collection of child measurements of the current measurement. Each of these measurements
   * in their turn give access to its own child measurements.
   * 
   * @return a possible empty collection of child measurements.
   */
  Collection<TimingMeasurement> getChildMeasurements();
}
