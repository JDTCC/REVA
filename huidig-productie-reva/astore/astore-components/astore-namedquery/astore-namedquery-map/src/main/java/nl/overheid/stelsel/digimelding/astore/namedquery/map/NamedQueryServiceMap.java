package nl.overheid.stelsel.digimelding.astore.namedquery.map;

import nl.overheid.stelsel.digimelding.astore.namedquery.AbstractNamedQueryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NamedQueryServiceMap extends AbstractNamedQueryService {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 6531649177612182708L;
  private static Logger logger = LoggerFactory.getLogger(NamedQueryServiceMap.class);

  // -------------------------------------------------------------------------
  // OSGi Blueprint lifecycle
  // -------------------------------------------------------------------------

  /** Blueprint lifecycle method */
  public void lifecycleInit() {
    logger.trace("NamedQueryServiceImpl:lifecycleInit");
  }

  /** Blueprint lifecycle method */
  public void lifecycleDestroy() {
    logger.trace("NamedQueryServiceImpl:lifecycleDestroy");
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
}
