package nl.overheid.stelsel.gba.reva.web.resources;

import java.util.Locale;

public class QueryAsCsvResourceReference extends BaseQueryResourceReference<QueryAsCsvResource> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  private static final Key KEY = new Key( QueryAsCsvResourceReference.class.getName(), "query", Locale.ENGLISH, null, null );

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public QueryAsCsvResourceReference() {
    super( KEY );
  }

  // -------------------------------------------------------------------------
  // BaseQueryResourceReference overrides
  // -------------------------------------------------------------------------

  @Override
  protected QueryAsCsvResource getQueryResource() {
    return new QueryAsCsvResource();
  };
}
