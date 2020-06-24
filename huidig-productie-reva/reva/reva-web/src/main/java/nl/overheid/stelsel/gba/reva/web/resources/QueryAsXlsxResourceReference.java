package nl.overheid.stelsel.gba.reva.web.resources;

import java.util.Locale;

public class QueryAsXlsxResourceReference extends BaseQueryResourceReference<QueryAsXlsxResource> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  private static final Key KEY = new Key( QueryAsXlsxResourceReference.class.getName(), "query", Locale.ENGLISH, null, null );

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public QueryAsXlsxResourceReference() {
    super( KEY );
  }

  // -------------------------------------------------------------------------
  // BaseQueryResourceReference overrides
  // -------------------------------------------------------------------------

  @Override
  protected QueryAsXlsxResource getQueryResource() {
    return new QueryAsXlsxResource();
  }
}
