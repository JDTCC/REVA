package nl.overheid.stelsel.digimelding.astore.timing;

import java.util.Collection;

/**
 * A service to collect timing measurements.
 * 
 * The basic idea is to gather timing information without being intrusive to the existing
 * application. For this reason the timing implementation should not throw any exceptions. And be as
 * foregiving for incorrect usage as possible.
 * 
 */
public interface TimingService {

  /**
   * Starts a timing session every time this method is called. During a session multiple timing
   * durations can be collected.
   * 
   * Please note that if the session is not ended it eventually becomes a zombie session that stick
   * around and never gets terminated (ended).
   * 
   * @return a newly created {@link TimingSession} object.
   */
  TimingSession startSession();

  /**
   * Gives the currently running session for the current thread. If the current thread has no
   * session yet a new one will be created.
   * 
   * Note: this allows for only a single running session per thread. If you need more sessions per
   * thread you have to do the bookkeeping yourself and use startSession multiple time.
   * 
   * @return the {@link TimingSession} object for the current thread.
   */
  TimingSession getSession();

  /**
   * Retrieves the last finished timing session.
   * 
   * @return TimingSession object with the timing information of the last session. If there was no
   *         last session null will be returned.
   */
  TimingSession getLastSession();

  /**
   * Looks for the timing session with the given id.
   * 
   * @param id of the session to look for.
   * @return If found the TimingSession object of the session, null otherwise.
   */
  TimingSession findSession(String id);

  /**
   * Retrieves the requested number of finished timing sessions.
   * 
   * @param number the requested number of finished sessions
   * @return a possible empty collection with at most the requested number of finished sessions.
   */
  Collection<TimingSession> getLastSessions(int number);

  /**
   * Retrieves the pending sessions. These are session that have not yet ended. So they either are
   * still running of they have become zombie session.
   * 
   * @return a possible empty collection with unfinished timing sessions.
   */
  Collection<TimingSession> getPendingSessions();
}
