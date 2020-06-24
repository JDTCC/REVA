package nl.overheid.stelsel.digimelding.astore.timing.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import nl.overheid.stelsel.digimelding.astore.timing.ThreadInfo;
import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;
import nl.overheid.stelsel.digimelding.astore.timing.TimingSession;

/**
 * Simple abstract {@link TimingSession} implementation.
 * 
 */
public abstract class SimpleTimingSession implements TimingSession {

  // ------------------------------------------------------------------------
  // Object attributes
  // ------------------------------------------------------------------------

  private String id;
  private ThreadInfo threadInfo;
  private long sessionStart;
  private Long sessionDuration = null;
  private Collection<TimingMeasurement> measurements = new ArrayList<>();

  // ------------------------------------------------------------------------
  // Constructors
  // ------------------------------------------------------------------------

  /**
   * Create a new timing session.
   */
  public SimpleTimingSession() {
    this(UUID.randomUUID().toString());
  }

  /**
   * Create a new timing session.
   * 
   * @param id
   */
  public SimpleTimingSession(String id) {
    this.id = id;
    this.threadInfo = new SimpleThreadInfo(Thread.currentThread());
    this.sessionStart = System.nanoTime();
  }

  // ------------------------------------------------------------------------
  // Implementing TimingSession
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
  public ThreadInfo getThreadInfo() {
    return threadInfo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long getDuration() {
    if (sessionDuration == null) {
      return System.nanoTime() - sessionStart;
    }

    return sessionDuration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long endSession() {
    if (sessionDuration == null) {
      sessionDuration = getDuration();

      // Also end all pending measurements.
      for (TimingMeasurement measurement : measurements) {
        measurement.endMeasurement();
      }
    }
    return sessionDuration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPending() {
    return sessionDuration == null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TimingMeasurement startMeasurement(String id) {
    TimingMeasurement measurement = createMeasurement(id);
    // Only add top level measurements. Lower level measurements will be added
    // to their parent's child measurements.
    if (measurement.getParent() == null) {
      measurements.add(measurement);
    }
    return measurement;
  }

  @Override
  public Collection<TimingMeasurement> getMeasurements() {
    return measurements;
  }

  // ------------------------------------------------------------------------
  // Abstract interface
  // ------------------------------------------------------------------------

  protected abstract TimingMeasurement createMeasurement(String id);
}
