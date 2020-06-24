package nl.overheid.stelsel.digimelding.astore.impl.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.clerezza.rdf.core.UriRef;
import org.osgi.framework.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationContext;
import nl.overheid.stelsel.digimelding.astore.AnnotationTree;
import nl.overheid.stelsel.digimelding.astore.namedquery.NamedQueryService;
import nl.overheid.stelsel.digimelding.astore.storage.StorageProvider;
import nl.overheid.stelsel.digimelding.astore.timing.TimingMeasurement;
import nl.overheid.stelsel.digimelding.astore.timing.TimingService;

public class StorageManager extends AbstractManager<StorageProvider> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Logger logger = LoggerFactory.getLogger(StorageManager.class);

  // -------------------------------------------------------------------------
  // Members
  // -------------------------------------------------------------------------

  private NamedQueryService namedQueryService;

  // -------------------------------------------------------------------------
  // OSGi Blueprint reference binding
  // -------------------------------------------------------------------------

  @Override
  public void bind(StorageProvider provider, Map<?, ?> properties) {
    super.bind(provider, properties);
  }

  @Override
  public void unbind(StorageProvider provider) {
    super.unbind(provider);
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  /**
   * Takes care of storing an annotation in the annotation store.
   * 
   * @param context the context.
   * @param annotation the annotation to store.
   */
  public void store(AnnotationContext context, Annotation annotation) {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("StorageManager:store");
    try {

      boolean stored = false;
      for (StorageProvider provider : getProviders()) {
        TimingMeasurement childMeasurement =
            measurement.startChildMeasurement(provider.getClass().getSimpleName());
        provider.store(context, annotation);
        childMeasurement.endMeasurement();
        stored = true;
      }

      if (!stored) {
        throw new ServiceException("Missing StorageProvider services, annotation not stored!");
      }
    } finally {
      measurement.endMeasurement();
    }
  }

  /**
   * Retrieve the requested AnnotationTree from storage providers. The first storage provider
   * returning data will be used.
   * 
   * @param context the context.
   * @param stam the root id of the requested annotation tree.
   * @return The AnnotationTree if present null otherwise.
   */
  public AnnotationTree retrieve(AnnotationContext context, UUID stam) {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("StorageManager:retrieve");
    try {

      AnnotationTree tree = null;
      for (StorageProvider provider : getProviders()) {
        TimingMeasurement childMeasurement =
            measurement.startChildMeasurement(provider.getClass().getSimpleName());
        tree = provider.retrieve(context, stam);
        childMeasurement.endMeasurement();
        if (tree != null) {
          return tree;
        }
      }

      return tree;
    } finally {
      measurement.endMeasurement();
    }
  }

  public Object namedQuery(AnnotationContext context, String namedQuery,
      Map<String, String> parameters) throws Exception {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("StorageManager:namedQuery");
    Map<String,String> parameterMap = parameters;
    try {

      if (parameterMap == null) {
        // No parameters yet, so create an empty map.
        parameterMap = new HashMap<String, String>();
      }

      // Add provider parameters to the list of given parameters.
      for (StorageProvider provider : getProviders()) {
        Map<String, String> providerParameters = provider.getQueryParameters();
        for (Map.Entry<String, String> entry : providerParameters.entrySet()) {
          // Only add if not already existing. This prevents overriding
          // user given values.
          if (!parameterMap.containsKey(entry.getKey())) {
            parameterMap.put(entry.getKey(), entry.getValue());
          }
        }
      }

      String query = getNamedQueryService().getQuery(namedQuery, parameterMap);
      if (query == null) {
        throw new ServiceException("Unknown query name: " + namedQuery);
      }

      return query(context, query);
    } finally {
      measurement.endMeasurement();
    }
  }

  public Object query(AnnotationContext context, String query) throws Exception {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("StorageManager:query");
    try {

      Object queryResult = null;
      for (StorageProvider provider : getProviders()) {
        try {
          TimingMeasurement childMeasurement =
              measurement.startChildMeasurement(provider.getClass().getSimpleName());
          queryResult = provider.query(context, query);
          childMeasurement.endMeasurement();
        } catch (Exception exception) {
          logger.error(String.format("Running query failed!\nQuery:\n%s\nDue to: %s", query,
              exception.getMessage()));
          throw exception;
        }
      }

      return queryResult;
    } finally {
      measurement.endMeasurement();
    }
  }

  /**
   * Removes the annotation tree with the given reference from the annotation store.
   * 
   * @param uriRef of the annotation tree to remove.
   * @param context the context.
   */
  public void remove(AnnotationContext context, UriRef uriRef) {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("StorageManager:remove");
    try {

      for (StorageProvider provider : getProviders()) {
        TimingMeasurement childMeasurement =
            measurement.startChildMeasurement(provider.getClass().getSimpleName());
        provider.remove(context, uriRef);
        childMeasurement.endMeasurement();
      }
    } finally {
      measurement.endMeasurement();
    }
  }

  /**
   * Removes all annotation trees from the annotation store.
   * 
   * @param context the context.
   */
  public void remove(AnnotationContext context) {
    TimingMeasurement measurement =
        getTimingService().getSession().startMeasurement("StorageManager:remove:*");
    try {

      for (StorageProvider provider : getProviders()) {
        TimingMeasurement childMeasurement =
            measurement.startChildMeasurement(provider.getClass().getSimpleName());
        provider.remove(context);
        childMeasurement.endMeasurement();
      }
    } finally {
      measurement.endMeasurement();
    }
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public NamedQueryService getNamedQueryService() {
    if (namedQueryService == null) {
      throw new ServiceException("NamedQueryService service not available");
    }
    return namedQueryService;
  }

  public void setNamedQueryService(NamedQueryService namedQueryService) {
    this.namedQueryService = namedQueryService;
  }

  /**
   * Noodzakelijk omdat blueprint de setter uit de super class niet pakt.
   */
  @Override
  public void setTimingService(TimingService timingService) {
    super.setTimingService(timingService);
  }
}
