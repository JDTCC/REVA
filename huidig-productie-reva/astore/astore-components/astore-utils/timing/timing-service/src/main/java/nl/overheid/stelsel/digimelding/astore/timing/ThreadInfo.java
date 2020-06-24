package nl.overheid.stelsel.digimelding.astore.timing;

/**
 * Simple interface to give access to thread information long after the thread has stopped or is
 * recycled for other purposes. This information object is mainly used to tell which thread created
 * session objects.
 * 
 */
public interface ThreadInfo {

  /**
   * Gives the Id of the thread that created a session object.
   * 
   * @return a long value containing the thread id.
   */
  long getId();

  /**
   * Gives the name of the thread that created a session object.
   * 
   * @return a string with the name of the thread.
   */
  String getName();
}
