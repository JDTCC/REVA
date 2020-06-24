package nl.overheid.stelsel.digimelding.astore.core.commands.decorators;

/**
 * Decorator interface. Classes implementing this interface are capable of decorating objects of
 * other types.
 * 
 *
 * @param <T> the type of the decorated objects.
 */
interface Decorator<T> {

  /**
   * Decorate the given object.
   * 
   * @param decorated the object to be decorated.
   * @return Decorator object.
   */
  T decorate(T decorated);

  /**
   * Get the decorated object.
   * 
   * @return the decorated object.
   */
  T getDecorated();
}
