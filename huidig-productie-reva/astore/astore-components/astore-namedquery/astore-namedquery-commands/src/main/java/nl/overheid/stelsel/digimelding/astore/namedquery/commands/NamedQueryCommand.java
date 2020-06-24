package nl.overheid.stelsel.digimelding.astore.namedquery.commands;

import nl.overheid.stelsel.digimelding.astore.namedquery.NamedQueryService;

import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.osgi.framework.ServiceException;

public abstract class NamedQueryCommand extends OsgiCommandSupport {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private NamedQueryService namedQueryService;

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public NamedQueryService getNamedQueryService() {
    if (namedQueryService == null) {
      throw new ServiceException("NamedQueryService service not available");
    }
    return namedQueryService;
  }

  public void setNamedQueryService(NamedQueryService service) {
    this.namedQueryService = service;
  }
}
