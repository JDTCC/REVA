package nl.overheid.stelsel.digimelding.astore.namedquery.filesystem;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSystemRepository {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(FileSystemRepository.class);

  private static final String QUERY_FILE_EXTENSION = ".rq";
  private static final String QUERY_FILE_PATTERN = ".*\\" + QUERY_FILE_EXTENSION;

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private Map<String, Long> timestamps = new HashMap<String, Long>();
  private File repository;
  private boolean persistent = false;

  // -------------------------------------------------------------------------
  // OSGi Blueprint lifecycle
  // -------------------------------------------------------------------------

  /** Blueprint lifecycle method */
  public void init() {
    logger.trace("FileSystemRepository:lifecycleInit");
  }

  /** Blueprint lifecycle method */
  public void destroy() {
    logger.trace("FileSystemRepository:lifecycleDestroy");
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  /**
   * Store the given value under the given key. The key is used to create a file on the filesystem.
   * The value is used for that file's content.
   * 
   * @param key the name used to store the value under
   * @param value the value to be stored.
   */
  public void store(String key, String value) {
    if (isPersistent()) {
      File queryFile = new File(getRepository(), key + QUERY_FILE_EXTENSION);
      try {
        FileUtils.writeStringToFile(queryFile, value, "UTF-8");
        updateTimestamp(key);
      } catch (IOException e) {
        logger.error("Named query not stored due to: ", e);
      }
    }
  }

  /**
   * Retrieves a map from the filesystem with all stored values and the name / key under which they
   * were stored.
   * 
   * @return Map<key,value>
   */
  public Map<String, String> getNamedQueries() {
    Map<String, String> queriesMap = Collections.emptyMap();
    if (isPersistent()) {
      File[] queries = getRepository().listFiles(new FileFilter() {
        @Override
        public boolean accept(File pathname) {
          // Only files matching our pattern.
          return pathname.isFile() && pathname.getName().matches(QUERY_FILE_PATTERN);
        }
      });

      if ( queries != null ) {
        queriesMap = new HashMap<String, String>(queries.length);
        for (File query : queries) {
          try {
            String queryName =
                query.getName()
                    .substring(0, query.getName().length() - QUERY_FILE_EXTENSION.length());
            queriesMap.put(queryName, loadQuery(queryName));
          } catch (IOException ioex) {
            logger.error("Loading query file failed, file will be ignored!", ioex);
          }
        }
      }
    }

    return queriesMap;
  }

  /**
   * Loads a query from the filesystem
   * 
   * @param name of the query to load
   * @return String with the query contents.
   * @throws IOException
   */
  public String loadQuery(String name) throws IOException {
    String query = null;
    File namedQueryFile = new File(getRepository(), name + QUERY_FILE_EXTENSION);
    if (namedQueryFile.exists()) {
      query = getContent(namedQueryFile);
      updateTimestamp(name);
    }
    return query;
  }

  /**
   * Specifies whether the give named query has been modified since load time.
   * 
   * @param name of the query
   * @return true if the named query was modified, false otherwise.
   */
  public boolean isModified(String name) {
    boolean isModified = false;
    File namedQueryFile = new File(getRepository(), name + QUERY_FILE_EXTENSION);
    if (namedQueryFile.exists()) {
      if (timestamps.containsKey(name)) {
        isModified = namedQueryFile.lastModified() != timestamps.get(name);
      } else {
        // New query, assume modified to be able to auto load.
        isModified = true;
      }
    } else {
      // None existing, assume modified since the named query might
      // have been removed.
      isModified = timestamps.containsKey(name);
    }
    return isModified;
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  /**
   * Specifies if the file system based storage can be properly accessed for storing named queries.
   * 
   * @return True when the storage location can be accesssed, false otherwise.
   */
  public boolean isPersistent() {
    return persistent;
  }

  /**
   * Setter for the repository directory path.
   * 
   * @param repositoryPath
   */
  public void setRepositoryPath(String repositoryPath) {
    if (repositoryPath == null) {
      throw new IllegalArgumentException("Null not allowed for repository path!");
    }

    File tempRepository = new File(repositoryPath);
    if (!tempRepository.equals(repository)) {
      // Repository path is different from last time (existing) so
      // refresh the repository.
      repository = tempRepository;
      persistent = createFolderIfNotExists(repository);
      if (!persistent) {
        logger.warn("Reverting to in-memory repository only, due to previous problems!");
      }
    }
  }

  /**
   * Gets the directory (File object) used for storing named queries.
   * 
   * @return File object to the repository.
   */
  public File getRepository() {
    return repository;
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  /**
   * Create the given filesystem folder if it does not yet exist.
   * 
   * @param folder to create.
   * @return True if the folder exists or was created, false otherwise (creation failed)
   */
  private boolean createFolderIfNotExists(File folder) {
    if (folder.exists()) {
      if (!folder.isDirectory()) {
        logger.error("File already exists: " + folder.getAbsolutePath());
        return false;
      }
    } else {
      if (!folder.mkdirs()) {
        logger.error("Can't create folder: " + folder.getAbsolutePath());
        return false;
      }
    }

    return true;
  }

  /**
   * Read content from the file system
   * 
   * @param file the file to read
   * @return String containing the content.
   * @throws IOException
   */
  private String getContent(File file) throws IOException {
    return FileUtils.readFileToString(file, "UTF-8");
  }

  /**
   * Updates the timestamp for the given key
   * 
   * @param key to update the timestamp for.
   */
  private void updateTimestamp(String key) {
    File namedQueryFile = new File(getRepository(), key + QUERY_FILE_EXTENSION);
    timestamps.put(key, namedQueryFile.lastModified());
  }
}
