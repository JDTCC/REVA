package nl.overheid.stelsel.digimelding.astore.notification;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationContext;

/**
 * Plugin interface for sending notifications.
 * 
 */
public interface NotificationProvider {

  /**
   * The actual notification method for the provider to implement. The astore engine will call this
   * method to allow the provider to send out the actual notification.
   * 
   * @param context the context
   * @param annotation the annotation to send a notification for.
   */
  void notify(AnnotationContext context, Annotation annotation);

}
