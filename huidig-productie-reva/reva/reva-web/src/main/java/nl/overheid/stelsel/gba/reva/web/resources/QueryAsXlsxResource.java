package nl.overheid.stelsel.gba.reva.web.resources;

import java.io.IOException;

import nl.overheid.stelsel.digimelding.astore.jaxrs.providers.ResultSetXlsxMessageBodyWriter;

public class QueryAsXlsxResource extends BaseQueryResource<ResultSetXlsxMessageBodyWriter> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  public static final String QUERY_NAME_PARAM = "queryName";
  public static final String FILE_EXTENSION = ".xlsx";
  public static final String MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public QueryAsXlsxResource() {
	super(MIME_TYPE, FILE_EXTENSION);
  }
  
  // -------------------------------------------------------------------------
  // BaseQueryResource overrides
  // -------------------------------------------------------------------------

  @Override
  protected ResultSetXlsxMessageBodyWriter getWriter() throws IOException {
    return new ResultSetXlsxMessageBodyWriter();
  }
}
