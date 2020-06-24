package nl.overheid.stelsel.digimelding.astore.utils.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Private contructors for utility classes are mostly not covered with
 * unittests. As a result they show up as uncovered in your code coverage tool
 * (Emma, Cobertura). Too eliminate this we use reflection to instantiate the
 * constructor and increase the coverage. This is purely a cosmetic solution to
 * increase coverage.
 * 
 * 
 * @param <T>
 *            the class under test.
 */
public class PrivateConstructorInstantiator<T> {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(PrivateConstructorInstantiator.class);

  private PrivateConstructorInstantiator() {
  }

  public PrivateConstructorInstantiator(Class<T> clazz) {
    this();

    Constructor<T> constructor = null;
    try {
      // Use reflection to get to the default constructor.
      constructor = clazz.getDeclaredConstructor();
    } catch (NoSuchMethodException e) {
      LOGGER.error("Missing default constructor for: " + clazz.getName());
    }

    // Only instantiate the class if a private default constructor exists.
    if (constructor != null
        && Modifier.isPrivate(constructor.getModifiers())) {
      constructor.setAccessible(true);
      try {
        constructor.newInstance();
      } catch (Exception e) {
        // Instantiating failed
        LOGGER.error("Instantiation failed due to: " + e.getMessage());
      }
    }
  }
}
