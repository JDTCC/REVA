package nl.overheid.stelsel.digimelding.astore.timing.impl;

import nl.overheid.stelsel.digimelding.astore.timing.ThreadInfo;

/**
 * Simple {@link ThreadInfo} implementation.
 * 
 */
public class SimpleThreadInfo implements ThreadInfo {

  // ------------------------------------------------------------------------
  // Object attributes
  // ------------------------------------------------------------------------

  private long threadId;
  private String threadName;

  // ------------------------------------------------------------------------
  // Constructors
  // ------------------------------------------------------------------------

  /**
   * Create a new ThreadInfo object based on data from the given thread.
   * 
   * @param thread to extract the information from.
   */
  public SimpleThreadInfo(Thread thread) {
    threadId = thread.getId();
    threadName = thread.getName();
  }

  // ------------------------------------------------------------------------
  // Implementing ThreadInfo
  // ------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override
  public long getId() {
    return threadId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return threadName;
  }
}
