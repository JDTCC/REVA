package nl.overheid.stelsel.digimelding.astore.notification.twitter;

import org.osgi.framework.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationContext;
import nl.overheid.stelsel.digimelding.astore.notification.NotificationProvider;
import twitter4j.Twitter;

/**
 * This notification provider uses twitter to send out notification.
 * 
 */
public class TwitterNotificationProvider implements NotificationProvider {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(TwitterNotificationProvider.class);

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private Twitter twitter = null;
  private int teller = 0;

  // -------------------------------------------------------------------------
  // OSGi Blueprint lifecycle
  // -------------------------------------------------------------------------

  public void init() {
    logger.trace("TwitterNotificationProvider:init");
  }

  public void destroy() {
    logger.trace("TwitterNotificationProvider:destroy");
  }

  // -------------------------------------------------------------------------
  // Implementing NotificationProvider
  // -------------------------------------------------------------------------

  @Override
  public void notify(AnnotationContext context, Annotation annotation) {

    // TODO: Build twitter message.

    try {
      getTwitter().updateStatus("Digimelding bla bla: " + ++teller);
    } catch (Exception e) {
      logger.error("Sending tweet failed!", e);
    }
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public Twitter getTwitter() {
    if (twitter == null) {
      throw new ServiceException("Twitter service not available");
    }
    return twitter;
  }

  public void setTwitter(Twitter twitter) {
    this.twitter = twitter;
  }
}
