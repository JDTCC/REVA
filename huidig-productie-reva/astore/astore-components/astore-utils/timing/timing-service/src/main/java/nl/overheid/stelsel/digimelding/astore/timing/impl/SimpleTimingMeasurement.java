package nl.overheid.stelsel.digimelding.astore.timing.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;

/**
 * Simple abstract {@link TimingMeasurement} implementation.
 * 
 */
public abstract class SimpleTimingMeasurement implements TimingMeasurement {

  // ------------------------------------------------------------------------
  // Object attributes
  // ------------------------------------------------------------------------

  private String id;
  private long timingStart;
  private Long timingDuration = null;
  private Collection<TimingMeasurement> measurements = new ArrayList<>();
  private TimingMeasurement parent = null;

  // ------------------------------------------------------------------------
  // Constructors
  // ------------------------------------------------------------------------

  /**
   * Create a new timing session.
   */
  public SimpleTimingMeasurement() {
    this(UUID.randomUUID().toString());
  }

  /**
   * Create a new timing session.
   * 
   * @param id
   */
  public SimpleTimingMeasurement(String id) {
    this.id = id;
    this.timingStart = System.nanoTime();
  }

  /**
   * Create a new timing session.
   * 
   * @param parent
   */
  public SimpleTimingMeasurement(TimingMeasurement parent) {
    this(parent, UUID.randomUUID().toString());
  }

  /**
   * Create a new timing session.
   * 
   * @param parent
   * @param id
   */
  public SimpleTimingMeasurement(TimingMeasurement parent, String id) {
    this(id);
    this.parent = parent;
  }

  // ------------------------------------------------------------------------
  // Implementing TimingMeasurement
  // ------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId() {
    return id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getDuration() {
    if (timingDuration == null) {
      return System.nanoTime() - timingStart;
    }

    return timingDuration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long endMeasurement() {
    if (timingDuration == null) {
      timingDuration = System.nanoTime() - timingStart;

      // Also end all pending child measurements.
      for (TimingMeasurement measurement : measurements) {
        measurement.endMeasurement();
      }
    }
    return timingDuration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPending() {
    return timingDuration == null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TimingMeasurement getParent() {
    return parent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TimingMeasurement startChildMeasurement(String id) {
    TimingMeasurement measurement = createChildMeasurement(id);
    measurements.add(measurement);
    return measurement;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<TimingMeasurement> getChildMeasurements() {
    return measurements;
  }

  // ------------------------------------------------------------------------
  // Abstract interface
  // ------------------------------------------------------------------------

  protected abstract TimingMeasurement createChildMeasurement(String id);
}
