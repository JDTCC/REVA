package org.apache.stanbol.rules.rulestore.commands.decorators;

import java.util.List;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.stanbol.rules.base.api.NoSuchRuleInRecipeException;
import org.apache.stanbol.rules.base.api.Recipe;
import org.apache.stanbol.rules.base.api.Rule;
import org.apache.stanbol.rules.base.api.util.RuleList;

/**
 * Decorator for Recipe. This decorator adds a proper toString() to the
 * original type.
 * 
 */
public class RecipePrettyDecorator implements Recipe {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private final Recipe recipe;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public RecipePrettyDecorator( Recipe recipe ) {
    this.recipe = recipe;
  }

  // -------------------------------------------------------------------------
  // Implementing Recipe
  // -------------------------------------------------------------------------

  public String prettyPrint() {
    return recipe.prettyPrint();
  }

  public Rule getRule( String ruleName ) throws NoSuchRuleInRecipeException {
    return recipe.getRule( ruleName );
  }

  public Rule getRule( UriRef ruleID ) throws NoSuchRuleInRecipeException {
    return recipe.getRule( ruleID );
  }

  public RuleList getRuleList() {
    return recipe.getRuleList();
  }

  public List<UriRef> listRuleIDs() {
    return recipe.listRuleIDs();
  }

  public List<String> listRuleNames() {
    return recipe.listRuleNames();
  }

  public UriRef getRecipeID() {
    return recipe.getRecipeID();
  }

  public String getRecipeDescription() {
    return recipe.getRecipeDescription();
  }

  public void addRule( Rule rule ) {    
    recipe.addRule( rule );
  }

  public void removeRule( Rule rule ) {
    recipe.removeRule( rule );
  }

  // -------------------------------------------------------------------------
  // Object overrides
  // -------------------------------------------------------------------------

  @Override
  public String toString() {
    return recipe.prettyPrint();
  }
}
