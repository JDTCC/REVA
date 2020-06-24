package nl.overheid.stelsel.digimelding.astore.timing.map;

import java.util.UUID;

import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;
import nl.overheid.stelsel.digimelding.astore.timing.impl.SimpleTimingMeasurement;

/**
 * Memory based {@link TimingMeasurement} implementation.
 * 
 */
public class TimingMeasurementMemory extends SimpleTimingMeasurement {

  // ------------------------------------------------------------------------
  // Object attributes
  // ------------------------------------------------------------------------

  // ------------------------------------------------------------------------
  // Constructors
  // ------------------------------------------------------------------------

  /**
   * Creates a new instance.
   */
  public TimingMeasurementMemory() {
    this(UUID.randomUUID().toString());
  }

  /**
   * Creates a new instance.
   * 
   * @param id
   */
  public TimingMeasurementMemory(String id) {
    super(id);
  }

  /**
   * Creates a new instance.
   * 
   * @param parent
   */
  public TimingMeasurementMemory(TimingMeasurement parent) {
    super(parent);
  }

  /**
   * Creates a new instance.
   * 
   * @param parent
   * @param id
   */
  public TimingMeasurementMemory(TimingMeasurement parent, String id) {
    super(parent, id);
  }

  // ------------------------------------------------------------------------
  // Override SimpleTimingMeasurement
  // ------------------------------------------------------------------------

  @Override
  protected TimingMeasurement createChildMeasurement(String id) {
    return new TimingMeasurementMemory(this, id);
  }
}
