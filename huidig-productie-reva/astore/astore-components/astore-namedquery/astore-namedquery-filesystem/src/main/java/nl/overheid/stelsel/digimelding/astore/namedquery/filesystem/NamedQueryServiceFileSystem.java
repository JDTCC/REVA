package nl.overheid.stelsel.digimelding.astore.namedquery.filesystem;

import java.io.IOException;
import java.util.Map;

import nl.overheid.stelsel.digimelding.astore.namedquery.AbstractNamedQueryService;

import org.osgi.framework.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NamedQueryServiceFileSystem extends AbstractNamedQueryService {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 6940025479389623842L;
  private static Logger logger = LoggerFactory.getLogger(NamedQueryServiceFileSystem.class);

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private FileSystemRepository repository;
  private boolean persistenStorageEnabled = false;
  private boolean autoReload = false;

  // -------------------------------------------------------------------------
  // OSGi Blueprint lifecycle
  // -------------------------------------------------------------------------

  /** Blueprint lifecycle method */
  public void lifecycleInit() {
    logger.trace("NamedQueryServiceFileSystem:lifecycleInit");

    if (getRepository().isPersistent()) {
      Map<String, String> namedQueryMap = getRepository().getNamedQueries();
      putAll(namedQueryMap);
      persistenStorageEnabled = true;
    }
  }

  /** Blueprint lifecycle method */
  public void lifecycleDestroy() {
    logger.trace("NamedQueryServiceFileSystem:lifecycleDestroy");
  }

  // -------------------------------------------------------------------------
  // Implementing NamedQueryService
  // -------------------------------------------------------------------------

  /** {@inheritDoc} */
  @Override
  public String getNamedQuery(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Null is not a valid name!");
    }
    if (isAutoReload() && getRepository().isModified(name)) {
      try {
        String query = getRepository().loadQuery(name);
        if (query != null) {
          put(name, query);
        } else {
          remove(name);
        }
      } catch (IOException ioex) {
        logger.error("Loading query file failed, file will be ignored!", ioex);
      }
    }
    return get(name);
  }

  /** {@inheritDoc} */
  @Override
  public String putNamedQuery(String name, String query) {
    if (name == null) {
      throw new IllegalArgumentException("Null is not a valid name!");
    }
    if (query == null) {
      throw new IllegalArgumentException("Null is not a valid query!");
    }
    return put(name, query);
  }

  // -------------------------------------------------------------------------
  // Implementing Map
  // -------------------------------------------------------------------------

  /** {@inheritDoc} */
  @Override
  public String put(String key, String value) {
    String response = super.put(key, value);

    if (isPersistentRepository()) {
      repository.store(key, value);
    }

    return response;
  }

  /** {@inheritDoc} */
  @Override
  public void putAll(Map<? extends String, ? extends String> m) {
    if (isPersistentRepository()) {
      // We need to make sure the named queries are store in our
      // repository. Leave the hard work to the simple put method.
      for (Map.Entry<? extends String, ? extends String> entry : m.entrySet()) {
        put(entry.getKey(), entry.getValue());
      }
    } else {
      // No further special treatment, leave it to the super implementation.
      super.putAll(m);
    }
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  /**
   * Retrieve the filesystem repository being used.
   * 
   * @return
   */
  public FileSystemRepository getRepository() {
    if (repository == null) {
      persistenStorageEnabled = false;
      throw new ServiceException("Repository not set, check blueprint configuration! ");
    }
    return repository;
  }

  /**
   * Injector to tell which filesystem repository to use.
   * 
   * @param repository
   */
  public void setRepository(FileSystemRepository repository) {
    this.repository = repository;
  }

  /**
   * Indicates whether the named query table is persisted to filesystem.
   * 
   * @return True if persisted false otherwise.
   */
  public boolean isPersistentRepository() {
    return persistenStorageEnabled && getRepository().isPersistent();
  }

  /**
   * Allows to turn auto reloading of named queries on or off. Turning on auto reload is
   * particularly useful during development so queries can be modified on the normal OS filesystem.
   * Any changes made will automatically be reloaded.
   * 
   * @param autoReload indicates whether to auto reload (true) or not (false).
   */
  public void setAutoReload(boolean autoReload) {
    this.autoReload = autoReload;
  }

  /**
   * Incidates whether or not auto reloading is enabled.
   * 
   * @return True if auto reload is enabled, false otherwise.
   */
  public boolean isAutoReload() {
    return autoReload;
  }
}
