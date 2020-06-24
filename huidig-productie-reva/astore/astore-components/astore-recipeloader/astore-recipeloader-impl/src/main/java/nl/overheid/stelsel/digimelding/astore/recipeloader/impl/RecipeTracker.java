package nl.overheid.stelsel.digimelding.astore.recipeloader.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;

import nl.overheid.stelsel.digimelding.astore.recipeloader.RecipeLoaderService;

import org.apache.clerezza.rdf.core.UriRef;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

/**
 * This bundle listener checks all bundles for the existence of
 * RuleStore-recipe-locations in the bundle manifest. If present this entry will
 * be used to manage all recipes on those locations. Manages the recipes is take
 * care of by the RecipeLoader.
 * 
 * Only Active bundles are managed. Once an Active bundle becomes
 * 
 */
public class RecipeTracker implements BundleListener {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final String RULESTORE_MANIFEST_KEY = "RuleStore-recipe-locations";
  private static final String PATH_SEPARATOR = "/";

  // -------------------------------------------------------------------------
  // Members
  // -------------------------------------------------------------------------

  RecipeLoaderService recipeLoader = null;

  // -------------------------------------------------------------------------
  // Implementing BundleListener
  // -------------------------------------------------------------------------

  public void initialize( RecipeLoaderService service, BundleContext context ) {
    recipeLoader = service;

    for( Bundle bundle : context.getBundles() ) {
      if( bundle.getState() == Bundle.ACTIVE ) {
        processBundle( bundle );
      }
    }
  }

  // -------------------------------------------------------------------------
  // Implementing BundleListener
  // -------------------------------------------------------------------------

  @Override
  public void bundleChanged( BundleEvent event ) {
    Bundle bundle = event.getBundle();
    switch( event.getType() ) {
      case BundleEvent.STARTED:
        processBundle( bundle );
        break;
      case BundleEvent.STOPPED:
        unprocessBundle( bundle );
        break;
      default:
        // ignore
    }
  }

  public void unprocessBundle( Bundle bundle ) {
    Dictionary<String, String> dictionary = bundle.getHeaders(); 
    String recipeLocationsStr = dictionary.get( RULESTORE_MANIFEST_KEY );
    if( recipeLocationsStr != null ) {
      recipeLoader.releaseDefaults( bundle );
    }
  }

  public void processBundle( Bundle bundle ) {
    Dictionary<String, String> dictionary = bundle.getHeaders(); 
    String recipeLocationsStr = dictionary.get( RULESTORE_MANIFEST_KEY );
    if( recipeLocationsStr != null ) {
      Collection<String> recipeLocations = Arrays.asList( recipeLocationsStr.split( "," ) );

      // Process each location one by one
      for( String recipeLocation : recipeLocations ) {
        processLocation( bundle, recipeLocation );
      }
    }
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private void processLocation( Bundle bundle, String location ) {
    Enumeration<String> enumeration = bundle.getEntryPaths( location );
    if ( enumeration != null ) {
      for( ; enumeration.hasMoreElements(); ) {
        String entry = enumeration.nextElement();
        if( PATH_SEPARATOR.equals( entry.substring( entry.length() - 1 ) ) ) {
          // This entry is a directory, process it.
          processLocation( bundle, entry );
        } else {
          recipeLoader.manageDefault( new UriRef( entry ), bundle, entry );
        }
      }
    }
  }
}
