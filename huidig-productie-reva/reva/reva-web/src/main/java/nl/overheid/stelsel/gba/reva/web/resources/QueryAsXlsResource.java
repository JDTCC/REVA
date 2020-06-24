package nl.overheid.stelsel.gba.reva.web.resources;

import java.io.IOException;

import nl.overheid.stelsel.digimelding.astore.jaxrs.providers.ResultSetXlsMessageBodyWriter;

public class QueryAsXlsResource extends BaseQueryResource<ResultSetXlsMessageBodyWriter> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  public static final String QUERY_NAME_PARAM = "queryName";
  public static final String FILE_EXTENSION = ".xls";
  public static final String MIME_TYPE = "application/vnd.ms-excel";

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public QueryAsXlsResource() {
	super(MIME_TYPE, FILE_EXTENSION);
  }

  // -------------------------------------------------------------------------
  // BaseQueryResource overrides
  // -------------------------------------------------------------------------

  @Override
  protected ResultSetXlsMessageBodyWriter getWriter() throws IOException {
    return new ResultSetXlsMessageBodyWriter();
  }
}
