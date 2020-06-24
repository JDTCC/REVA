package org.apache.stanbol.rules.rulestore.commands;

import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.apache.stanbol.rules.base.api.RuleStore;
import org.osgi.framework.ServiceException;

public abstract class RuleStoreCommand extends OsgiCommandSupport {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private RuleStore ruleStore;

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public RuleStore getRuleStore() {
    if( ruleStore == null ) {
      throw new ServiceException( "RuleStore not available" );
    }
    return ruleStore;
  }

  public void setRuleStore( RuleStore service ) {
    this.ruleStore = service;
  }
}
