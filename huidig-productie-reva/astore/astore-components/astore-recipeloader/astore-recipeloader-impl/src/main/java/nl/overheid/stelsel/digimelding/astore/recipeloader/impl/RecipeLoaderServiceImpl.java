package nl.overheid.stelsel.digimelding.astore.recipeloader.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import nl.overheid.stelsel.digimelding.astore.recipeloader.AlreadyManagedRecipeException;
import nl.overheid.stelsel.digimelding.astore.recipeloader.MissingRecipeException;
import nl.overheid.stelsel.digimelding.astore.recipeloader.RecipeLoaderService;
import nl.overheid.stelsel.digimelding.astore.recipeloader.UnmanagedRecipeException;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.stanbol.rules.base.api.AlreadyExistingRecipeException;
import org.apache.stanbol.rules.base.api.NoSuchRecipeException;
import org.apache.stanbol.rules.base.api.Recipe;
import org.apache.stanbol.rules.base.api.RecipeConstructionException;
import org.apache.stanbol.rules.base.api.RecipeEliminationException;
import org.apache.stanbol.rules.base.api.RuleStore;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implemenation for the RecipeLoader service.
 * 
 */
public class RecipeLoaderServiceImpl implements RecipeLoaderService {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private Logger logger = LoggerFactory.getLogger( getClass() );

  // -------------------------------------------------------------------------
  // Members
  // -------------------------------------------------------------------------

  private Bundle bundle;
  private RuleStore ruleStore;

  private RecipeTracker tracker;
  
  private Map<Long, Collection<UriRef>> managedDefaults = new HashMap<>();
  private Map<UriRef, Long> xref = new HashMap<>();
  private Map<UriRef, String> resourcePaths = new HashMap<>();

  // -------------------------------------------------------------------------
  // OSGi Blueprint lifecycle
  // -------------------------------------------------------------------------

  public void init() {
    logger.trace( "RecipeLoaderServiceImpl:init" );

    // Start tracking bundles with recipes
    tracker = new RecipeTracker();
    bundle.getBundleContext().addBundleListener( tracker );
    tracker.initialize( this, bundle.getBundleContext() );
  }

  public void destroy() {
    logger.trace( "RecipeLoaderServiceImpl:destroy" );
    
    // Stop tracking bundles with recipes.
    bundle.getBundleContext().removeBundleListener( tracker );
    tracker = null;
  }

  // -------------------------------------------------------------------------
  // Implementing RecipeLoaderService
  // -------------------------------------------------------------------------

  @Override
  public boolean exists( UriRef recipeId ) {
    return ruleStore.listRecipeIDs().contains( recipeId );
  }

  @Override
  public boolean isDefault( UriRef recipeId ) {
    return xref.containsKey( recipeId );
  }

  @Override
  public long getOwner( UriRef recipeId ) throws UnmanagedRecipeException {
    Long bundleId = xref.get( recipeId );
    if( bundleId == null ) {
      throw new UnmanagedRecipeException( recipeId );
    }
    return bundleId;
  }

  @Override
  public boolean isModified( UriRef recipeId ) throws UnmanagedRecipeException {
    if( !isDefault( recipeId ) ) {
      throw new UnmanagedRecipeException( recipeId );
    }
    
    Recipe storedRecipe = null;
    try {
      storedRecipe = ruleStore.getRecipe( recipeId );
    } catch( NoSuchRecipeException | RecipeConstructionException e ) {
      // Assume not equal
      return false;
    }
    Recipe defaultRecipe = getRecipe( xref.get( recipeId ), resourcePaths.get( recipeId ) );

    return defaultRecipe.equals( storedRecipe );
  }

  @Override
  public synchronized boolean manageDefault( UriRef recipeId, Bundle bundle, String resourcePath )
      throws MissingRecipeException, AlreadyManagedRecipeException {
    long bundleId = bundle.getBundleId();
    if( isDefault( recipeId ) ) {
      throw new AlreadyManagedRecipeException( recipeId, getOwner( recipeId ), bundleId );
    }

    // Load the recipe into the rulestore if not yet present.
    boolean existsInRuleStore = exists( recipeId );
    if( !existsInRuleStore ) {
      loadRecipe( recipeId, bundle, resourcePath );
    }    

    // Update book keeping
    if( !managedDefaults.containsKey( bundleId ) ) {
      managedDefaults.put( bundleId, new ArrayList<UriRef>() );
    }
    managedDefaults.get( bundleId ).add( recipeId );
    xref.put( recipeId, bundleId );
    resourcePaths.put( recipeId, resourcePath );

    if( logger.isInfoEnabled() ) {
      String action = ( existsInRuleStore ? "managed" : "loaded" );
      logger.info( "[] bundle([]) owned recipe: [] located at: []", action, xref.get( recipeId ), recipeId, resourcePath );
    }
    return !existsInRuleStore;
  }

  @Override
  public void restoreDefault( UriRef recipeId ) throws UnmanagedRecipeException {
    if( isDefault( recipeId ) ) {
      loadRecipe( recipeId, xref.get( recipeId ), resourcePaths.get( recipeId ) );

      if( logger.isInfoEnabled() ) {
        logger.info( "restored bundle([]) owned recipe: []", xref.get( recipeId ), recipeId );
      }
    } else {
      throw new UnmanagedRecipeException( recipeId );
    }
  }
  
  @Override
  public void restoreDefault( long bundleId ) {
    Bundle owner = bundle.getBundleContext().getBundle( bundleId );
    restoreDefault( owner );
  }
  
  @Override
  public void restoreDefault( Bundle bundle ) {
    Collection<UriRef> recipeUris = listDefaults( bundle );
    for( UriRef recipeId : recipeUris ) {
      loadRecipe( recipeId, bundle, resourcePaths.get( recipeId) );
      if( logger.isInfoEnabled() ) {
        logger.info( "restored bundle([]) owned recipe: []", bundle.getBundleId(), recipeId );
      }
    }
  }

  @Override
  public Collection<UriRef> listDefaults() {
    List<UriRef> defaults = new ArrayList<>();
    for( Collection<UriRef> ownedDefaults : managedDefaults.values() ) {
      defaults.addAll( ownedDefaults );
    }

    return defaults;
  }

  @Override
  public Collection<UriRef> listDefaults( long bundleId ) {
    List<UriRef> ownedDefaults = new ArrayList<>();
    if( managedDefaults.containsKey( bundleId ) ) {
      ownedDefaults.addAll( managedDefaults.get( bundleId ) );
    }

    return ownedDefaults;
  }

  @Override
  public Collection<UriRef> listDefaults( Bundle bundle ) {
    return listDefaults( bundle.getBundleId() );
  }

  @Override
  public synchronized boolean releaseDefault( UriRef recipeId ) {
    boolean exists = xref.containsKey( recipeId );
    if( exists ) {
      // Remove from cross reference and resource path.
      Long bundleId = xref.remove( recipeId );
      resourcePaths.remove( recipeId );

      // Remove from managed defaults
      Collection<UriRef> ownedDefaults = managedDefaults.get( bundleId );
      ownedDefaults.remove( recipeId );

      if( logger.isInfoEnabled() ) {
        logger.info( "released bundle([]) owned recipe: []", bundleId, recipeId );
      }
    }

    return exists;
  }

  @Override
  public synchronized Collection<UriRef> releaseDefaults( long bundleId ) {
    List<UriRef> releasedDefaults = new ArrayList<>();
    if( managedDefaults.containsKey( bundleId ) ) {
      Collection<UriRef> ownedDefaults = managedDefaults.remove( bundleId );
      // Clean up cross references and resource paths
      for( UriRef uriRef : ownedDefaults ) {
        xref.remove( uriRef );
        resourcePaths.remove( uriRef );
      }

      releasedDefaults.addAll( ownedDefaults );
    }

    if( logger.isInfoEnabled() ) {
      logger.info( "released bundle([]) owned recipes: []", bundleId, releasedDefaults.toString() );
    }

    return releasedDefaults;
  }

  @Override
  public Collection<UriRef> releaseDefaults( Bundle bundle ) {
    return releaseDefaults( bundle.getBundleId() );
  }

  // -------------------------------------------------------------------------
  // Getter / Setters
  // -------------------------------------------------------------------------
  
  public Bundle getBundle() {
    return bundle;
  }
  
  public void setBundle( Bundle bundle ) {
    this.bundle = bundle;
  }
  
  public RuleStore getRuleStore() {
    return ruleStore;
  }
  
  public void setRuleStore( RuleStore ruleStore ) {
    this.ruleStore = ruleStore;
  }
  
  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  /**
   * Loads a recipe into the RuleStore. This recipe is identified by the given
   * recipe Id and read from the resource path of the bundle with the given Id.
   * 
   * @param recipeId
   * @param bundleId
   * @param resourcePath
   */
  private void loadRecipe( UriRef recipeId, long bundleId, String resourcePath ) {
    Bundle owner = bundle.getBundleContext().getBundle( bundleId );
    if( owner != null ) {
      loadRecipe( recipeId, owner, resourcePath );
    } else {
      throw new MissingRecipeException( bundleId );
    }
  }

  /**
   * Loads a recipe into the RuleStore. This recipe is identified by the given
   * recipe Id and read from the resource path of the given bundle.
   * 
   * @param recipeId
   * @param bundle
   * @param resourcePath
   */
  private void loadRecipe( UriRef recipeId, Bundle bundle, String resourcePath ) throws MissingRecipeException {
    if( exists( recipeId ) ) {
      try {
        ruleStore.removeRecipe( recipeId );
      } catch( RecipeEliminationException e ) {
        logger.warn( "Failed to remove old recipe: " + recipeId, e );
      }
    }

    URL url = bundle.getEntry( resourcePath );
    if ( url == null ) {
      throw new MissingRecipeException( bundle.getBundleId(), resourcePath );
    }
    
    InputStream istream = null;
    try {
      istream = url.openStream();
      Recipe recipe = ruleStore.createRecipe( recipeId, null );
      ruleStore.addRulesToRecipe( recipe, istream, null );
    } catch( IOException | AlreadyExistingRecipeException e ) {
      logger.warn( "Failed to add default recipe: " + recipeId, e );
      throw new RuntimeException( e );
    }
  }

  private Recipe getRecipe( long bundleId, String resourcePath ) {
    UriRef tempRecipeId = new UriRef( "tempRecipe/" + UUID.randomUUID().toString() );

    Recipe tempRecipe = null;
    try {
      // Since Stanbol has no public features to load recipes from files, we use
      // the RuleStore to temporarily load a recipe.
      loadRecipe( tempRecipeId, bundleId, resourcePath );
      tempRecipe = ruleStore.getRecipe( tempRecipeId );
      ruleStore.removeRecipe( tempRecipeId );
    } catch( NoSuchRecipeException | RecipeConstructionException | RecipeEliminationException e ) {
      logger.error( "Failed to retrieve recipe!", e );
    }

    return tempRecipe;
  }
}
