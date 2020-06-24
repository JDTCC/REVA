package nl.overheid.stelsel.digimelding.astore.timing.map;

import java.util.Collection;

import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;
import nl.overheid.stelsel.digimelding.astore.timing.TimingSession;
import nl.overheid.stelsel.digimelding.astore.timing.impl.SimpleTimingSession;

/**
 * Memory based {@link TimingSession} implementation.
 * 
 */
public class TimingSessionMemory extends SimpleTimingSession {

  // ------------------------------------------------------------------------
  // Class attributes
  // ------------------------------------------------------------------------

  private static final ThreadLocal<TimingSessionMemory> CURRENT_SESSION = new ThreadLocal<>();

  // ------------------------------------------------------------------------
  // Constructors
  // ------------------------------------------------------------------------

  /**
   * Creates a new instance.
   */
  public TimingSessionMemory() {
    super();
  }

  // ------------------------------------------------------------------------
  // Public interface
  // ------------------------------------------------------------------------

  static final TimingSessionMemory get() {
    return CURRENT_SESSION.get();
  }

  static final void set(TimingSessionMemory session) {
    CURRENT_SESSION.set(session);
  }

  // ------------------------------------------------------------------------
  // Override Implementing TimingSession
  // ------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override
  public long endSession() {
    long duration = super.endSession();
    if (this == get()) {
      TimingSessionMemory.set(null);
    }
    return duration;
  }

  // ------------------------------------------------------------------------
  // Override SimpleTimingSession
  // ------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override
  protected TimingMeasurement createMeasurement(String id) {
    TimingMeasurement measurement = null;
    TimingSession activeSession = get();
    if (this == activeSession) {
      measurement = findDeepestPending(null);
    }

    if (measurement == null) {
      measurement = new TimingMeasurementMemory(id);
    } else {
      measurement = measurement.startChildMeasurement(id);
    }
    return measurement;
  };

  // ------------------------------------------------------------------------
  // Private methods
  // ------------------------------------------------------------------------

  private TimingMeasurement findDeepestPending(TimingMeasurement measurement) {
    TimingMeasurement currentPending = null;
    Collection<TimingMeasurement> measurements = getMeasurements();

    if (measurement != null) {
      if (measurement.isPending()) {
        currentPending = measurement;
        measurements = measurement.getChildMeasurements();
      } else {
        // This one is not pending so no pending child either.
        return null;
      }
    }

    // Check for even deeper ones.
    for (TimingMeasurement measurement2 : measurements) {
      TimingMeasurement deeperPending = findDeepestPending(measurement2);
      if (deeperPending != null) {
        return deeperPending;
      }
    }

    return currentPending;
  }
}
