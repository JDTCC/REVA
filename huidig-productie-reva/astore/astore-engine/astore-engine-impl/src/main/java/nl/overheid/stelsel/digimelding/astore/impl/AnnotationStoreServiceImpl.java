package nl.overheid.stelsel.digimelding.astore.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.clerezza.rdf.core.UriRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationContext;
import nl.overheid.stelsel.digimelding.astore.AnnotationStoreException;
import nl.overheid.stelsel.digimelding.astore.AnnotationStoreService;
import nl.overheid.stelsel.digimelding.astore.AnnotationTree;
import nl.overheid.stelsel.digimelding.astore.authorisation.AuthorisationException;
import nl.overheid.stelsel.digimelding.astore.impl.managers.AuthorisationManager;
import nl.overheid.stelsel.digimelding.astore.impl.managers.FilterManager;
import nl.overheid.stelsel.digimelding.astore.impl.managers.NotificationManager;
import nl.overheid.stelsel.digimelding.astore.impl.managers.ProcessorManager;
import nl.overheid.stelsel.digimelding.astore.impl.managers.StorageManager;
import nl.overheid.stelsel.digimelding.astore.impl.managers.ValidatorManager;
import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;
import nl.overheid.stelsel.digimelding.astore.timing.TimingService;

public class AnnotationStoreServiceImpl implements AnnotationStoreService {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final String UNAUTHORISED_REQUEST = "Unauthorised request!";

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(AnnotationStoreServiceImpl.class);

  // -------------------------------------------------------------------------
  // Members
  // -------------------------------------------------------------------------

  private AuthorisationManager authorisationManager;
  private FilterManager filterManager;
  private ProcessorManager processorManager;
  private StorageManager storageManager;
  private ValidatorManager validatorManager;
  private NotificationManager notificationManager;
  private TimingService timingService;

  // -------------------------------------------------------------------------
  // OSGi Blueprint lifecycle
  // -------------------------------------------------------------------------

  public void init() {
    logger.trace("AnnotationStoreServiceImpl:init");
  }

  public void destroy() {
    logger.trace("AnnotationStoreServiceImpl:destroy");
  }

  // -------------------------------------------------------------------------
  // Implementing AnnotationStoreService
  // -------------------------------------------------------------------------

  @Override
  public AnnotationTree get(UUID id) throws Exception {
    return get(new AnnotationContext(), id);
  }

  @Override
  public AnnotationTree get(AnnotationContext context, UUID id) throws Exception {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("AnnotationStoreService:get:" + id);
    try {
      context.setRef(UUID2UriRefConverter.toUriRef(id));

      return storageManager.retrieve(context, id);
    } finally {
      measurement.endMeasurement();
    }
  }

  @Override
  public void put(Annotation annotation) throws Exception {
    put(new AnnotationContext(), annotation);
  }

  @Override
  public void put(AnnotationContext context, Annotation annotation) {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("AnnotationStoreService:put");
    try {
      context.setRef(UUID2UriRefConverter.toUriRef(annotation.getStam()));

      loadTreeInContext(context, annotation.getStam());
      if (authorisationManager.isStoreAuthorised(context, annotation)) {
        validatorManager.validate(context, annotation);
        processorManager.process(context, annotation);
        storageManager.store(context, annotation);
        notificationManager.notify(context, annotation);
      } else {
        throw new AuthorisationException(UNAUTHORISED_REQUEST);
      }
    } catch (AnnotationStoreException exception) {
      logger.warn(exception.getMessage());
      throw exception;
    } catch (Exception exception) {
      logger.error("failure", exception);
      throw exception;
    } finally {
      measurement.endMeasurement();
    }
  }

  @Override
  public Object query(String query) throws Exception {
    return query(new AnnotationContext(), query);
  }

  @Override
  public Object query(AnnotationContext context, String query) throws Exception {
    Object queryResult = null;
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("AnnotationStoreService:query");
    try {
      if (authorisationManager.isRetrieveAuthorised(context, query)) {
        queryResult = storageManager.query(context, query);
      } else {
        throw new AuthorisationException(UNAUTHORISED_REQUEST);
      }
    } finally {
      measurement.endMeasurement();
    }

    return queryResult;
  }

  @Override
  public Object namedQuery(String name, Map<String, String> parameters) throws Exception {
    return namedQuery(new AnnotationContext(), name, parameters);
  }

  @Override
  public Object namedQuery(AnnotationContext context, String name, Map<String, String> parameters)
      throws Exception {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("AnnotationStoreService:namedQuery");

    try {
      return internalNamedQuery(context, name, parameters);
    } finally {
      measurement.endMeasurement();
    }
  }

  @Override
  public void remove(UUID uuid) throws Exception {
    remove(new AnnotationContext(), uuid);
  }

  @Override
  public void remove(AnnotationContext context, UUID uuid) throws Exception {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("AnnotationStoreService:remove");

    try {
      UriRef uriRef = UUID2UriRefConverter.toUriRef(uuid);
      context.setRef(uriRef);

      loadTreeInContext(context, uuid);
      if (authorisationManager.isClearAuthorised(context)) {
        processorManager.processRemoval(context, uuid);
        storageManager.remove(context, uriRef);
      } else {
        throw new AuthorisationException(UNAUTHORISED_REQUEST);
      }
    } catch (AnnotationStoreException exception) {
      logger.warn(exception.getMessage());
      throw exception;
    } catch (Exception exception) {
      logger.error("failure", exception);
      throw exception;
    } finally {
      measurement.endMeasurement();
    }
  }

  @Override
  public void remove() throws Exception {
    remove(new AnnotationContext());
  }

  @Override
  public void remove(AnnotationContext context) throws Exception {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("AnnotationStoreService:remove:*");

    try {
      context.setRef(new UriRef("*"));

      if (authorisationManager.isClearAuthorised(context)) {
        storageManager.remove(context);
      } else {
        throw new AuthorisationException(UNAUTHORISED_REQUEST);
      }
    } catch (AnnotationStoreException exception) {
      logger.warn(exception.getMessage());
      throw exception;
    } catch (Exception exception) {
      logger.error("failure", exception);
      throw exception;
    } finally {
      measurement.endMeasurement();
    }
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public void setAuthorisationManager(AuthorisationManager authorisationManager) {
    this.authorisationManager = authorisationManager;
  }

  public void setFilterManager(FilterManager filterManager) {
    this.filterManager = filterManager;
  }

  public void setProcessorManager(ProcessorManager processorManager) {
    this.processorManager = processorManager;
  }

  public void setStorageManager(StorageManager storageManager) {
    this.storageManager = storageManager;
  }

  public void setValidatorManager(ValidatorManager validatorManager) {
    this.validatorManager = validatorManager;
  }

  public void setNotificationManager(NotificationManager notificationManager) {
    this.notificationManager = notificationManager;
  }

  public TimingService getTimingService() {
    return timingService;
  }

  public void setTimingService(TimingService timingService) {
    this.timingService = timingService;
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  /**
   * Loads the full annotation tree for the given stam and adds it to the annotation context. This
   * way individual pipeline components do not need to load the tree themselves.
   * 
   * @param ctx the context to load the tree into.
   * @param stam the stam of the tree to load.
   */
  private void loadTreeInContext(AnnotationContext ctx, UUID stam) {
    if (stam != null) {
      AnnotationTree tree = this.storageManager.retrieve(ctx, stam);
      if (tree != null) {
        ctx.setTree(tree.getGraph());
      }
    }
  }

  private Object internalNamedQuery(AnnotationContext context, String name,
      Map<String, String> parameters) throws Exception {
    Object queryResult = null;
    try {
      if (authorisationManager.isRetrieveAuthorised(context, name)) {
        queryResult = storageManager.namedQuery(context, name, parameters);
        Set<AnnotationTree> trees = new HashSet<AnnotationTree>();
        if (queryResult instanceof AnnotationTree) {
          trees.add(AnnotationTree.class.cast(queryResult));
        } else if (queryResult instanceof Set) {
          trees.addAll(cast(queryResult));
        }
        if (!trees.isEmpty()) {
          filterManager.filter(trees);
        }
      } else {
        throw new AuthorisationException(UNAUTHORISED_REQUEST);
      }
    } catch (Exception exception) {
      logger.error(String.format("Running named query failed!\n%s\nDue to: %s",
          queryInfo(name, parameters), exception.getMessage()));
      throw exception;
    }
    return queryResult;
  }

  @SuppressWarnings("unchecked")
  private Set<AnnotationTree> cast(Object object) {
    return Set.class.cast(object);
  }

  private String queryInfo(String queryName, Map<String, String> queryArgumets) {
    StringBuilder builder = new StringBuilder();

    builder.append("Query name: " + queryName + "\n");
    builder.append("Query arguments: \n");
    for (Map.Entry<String, String> entry : queryArgumets.entrySet()) {
      builder.append(String.format("%s = '%s' \n", entry.getKey(), entry.getValue()));
    }

    return builder.toString();
  }
}
