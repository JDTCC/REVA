package org.apache.stanbol.rules.rulestore.commands.decorators;

import org.apache.clerezza.rdf.core.UriRef;
import org.apache.stanbol.rules.base.api.Recipe;
import org.apache.stanbol.rules.base.api.Rule;
import org.apache.stanbol.rules.base.api.util.AtomList;

/**
 * Decorator for Rule. This decorator adds a proper toString() to the original
 * type.
 * 
 */
public class RulePrettyDecorator implements Rule {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private final Rule rule;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public RulePrettyDecorator( Rule rule ) {
    this.rule = rule;
  }

  // -------------------------------------------------------------------------
  // Implementing Rule
  // -------------------------------------------------------------------------

  public String prettyPrint() {
    return rule.prettyPrint();
  }

  public UriRef getRuleID() {
    return rule.getRuleID();
  }

  public String getRuleName() {
    return rule.getRuleName();
  }

  public String getDescription() {
    return rule.getDescription();
  }

  public void setDescription( String description ) {
    rule.setDescription( description );
  }

  public void setRuleName( String ruleName ) {
    rule.setRuleName( ruleName );
  }

  public void setRule( String ruleString ) {
    rule.setRule( ruleString );
  }

  public AtomList getHead() {
    return rule.getHead();
  }

  public AtomList getBody() {
    return rule.getBody();
  }

  public Recipe getRecipe() {
    return rule.getRecipe();
  }

  // -------------------------------------------------------------------------
  // Object overrides
  // -------------------------------------------------------------------------

  @Override
  public String toString() {
    return rule.prettyPrint();
  }
}
