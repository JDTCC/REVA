package nl.overheid.stelsel.digimelding.astore.timing.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import nl.overheid.stelsel.digimelding.astore.timing.TimingService;
import nl.overheid.stelsel.digimelding.astore.timing.TimingSession;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

/**
 * Simple memory based implementation of a timeing service.
 * 
 */
@SuppressWarnings("unchecked")
public class TimingCollectorMemory implements TimingService {

  // ------------------------------------------------------------------------
  // Constants
  // ------------------------------------------------------------------------

  private static final int DEFAULT_BUFFER_SIZE = 100;

  // ------------------------------------------------------------------------
  // Object attributes
  // ------------------------------------------------------------------------

  private CircularFifoBuffer sessionBuffer = new CircularFifoBuffer(DEFAULT_BUFFER_SIZE);

  // ------------------------------------------------------------------------
  // Constructors
  // ------------------------------------------------------------------------

  public TimingCollectorMemory() {}

  // ------------------------------------------------------------------------
  // Implementing TimingService
  // ------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized TimingSession startSession() {
    TimingSession newSession = new TimingSessionMemory();
    sessionBuffer.add(newSession);
    return newSession;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TimingSession getSession() {
    TimingSessionMemory session = TimingSessionMemory.get();
    if (session == null) {
      session = TimingSessionMemory.class.cast(startSession());
      TimingSessionMemory.set(session);
    }
    return session;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TimingSession getLastSession() {
    if (sessionBuffer.isEmpty()) {
      return null;
    }
    Collection<TimingSession> sessions = getLastSessions(1);
    return (sessions.isEmpty() ? null : sessions.iterator().next());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<TimingSession> getLastSessions(int number) {
    CircularFifoBuffer requestedSessions = new CircularFifoBuffer(number);
    for (Iterator<TimingSession> iterator = sessionBuffer.iterator(); iterator.hasNext();) {
      TimingSession timingSession = iterator.next();
      if (!timingSession.isPending()) {
        requestedSessions.add(timingSession);
      }
    }
    return requestedSessions;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TimingSession findSession(String id) {
    for (Iterator<TimingSession> iterator = sessionBuffer.iterator(); iterator.hasNext();) {
      TimingSession timingSession = iterator.next();
      if (timingSession.getId().equals(id)) {
        return timingSession;
      }
    }

    // Not found.
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<TimingSession> getPendingSessions() {
    ArrayList<TimingSession> pendingSessions = new ArrayList<>();
    for (Iterator<TimingSession> iterator = sessionBuffer.iterator(); iterator.hasNext();) {
      TimingSession timingSession = iterator.next();
      if (timingSession.isPending()) {
        pendingSessions.add(timingSession);
      }
    }
    return pendingSessions;
  }

  // ------------------------------------------------------------------------
  // Getter / Setters
  // ------------------------------------------------------------------------

  /**
   * Retrieve the size of the session buffer. This size is the maximum number of sessions kept in
   * memory.
   * 
   * @return integer value with the maximum buffer size.
   */
  public int getBufferSize() {
    return sessionBuffer.maxSize();
  }

  /**
   * Allows to modify the maximum session buffer size. This size is the maximum number of sessions
   * kept in memory.
   * 
   * @param bufferSize the new Buffer size.
   */
  public void setBufferSize(int bufferSize) {
    CircularFifoBuffer newBuffer = new CircularFifoBuffer(bufferSize);
    newBuffer.addAll(sessionBuffer);
    sessionBuffer = newBuffer;
  }
}
