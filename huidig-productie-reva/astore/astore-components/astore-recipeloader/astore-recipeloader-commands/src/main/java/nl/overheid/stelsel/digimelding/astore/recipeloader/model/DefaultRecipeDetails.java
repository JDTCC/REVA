package nl.overheid.stelsel.digimelding.astore.recipeloader.model;

import org.apache.clerezza.rdf.core.UriRef;

/**
 * Container class for default recipe information.
 * 
 */
public class DefaultRecipeDetails {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------
  
  private UriRef recipeId;
  private long bundleId;
  private boolean modified;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public DefaultRecipeDetails( UriRef recipeId, long bundleId, boolean modified ) {
    this.recipeId = recipeId;
    this.bundleId = bundleId;
    this.modified = modified;
  }
  
  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------
  
  public UriRef getRecipeId() {
    return recipeId;
  }
  
  public long getBundleId() {
    return bundleId;
  }
  
  public boolean isModified() {
    return modified;
  }
  
  // -------------------------------------------------------------------------
  // Object Overrides
  // -------------------------------------------------------------------------
  
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append( String.format( "recipe id : %s\n", recipeId.toString() ) );
    buffer.append( String.format( "owner     : %d\n", bundleId ) );
    buffer.append( String.format( "modified? : %s\n", modified ) );
    return buffer.toString();
  }  
}
