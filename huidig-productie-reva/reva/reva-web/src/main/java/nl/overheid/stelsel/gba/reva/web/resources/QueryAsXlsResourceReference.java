package nl.overheid.stelsel.gba.reva.web.resources;

import java.util.Locale;

public class QueryAsXlsResourceReference extends BaseQueryResourceReference<QueryAsXlsResource> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  private static final Key KEY = new Key( QueryAsXlsResourceReference.class.getName(), "query", Locale.ENGLISH, null, null );

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public QueryAsXlsResourceReference() {
    super( KEY );
  }

  // -------------------------------------------------------------------------
  // BaseQueryResourceReference overrides
  // -------------------------------------------------------------------------

  @Override
  protected QueryAsXlsResource getQueryResource() {
    return new QueryAsXlsResource();
  }
}
