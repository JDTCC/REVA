package nl.overheid.stelsel.gba.reva.web.resources;

import java.io.IOException;

import org.apache.clerezza.jaxrs.sparql.providers.ResultSetCsvMessageBodyWriter;

public class QueryAsCsvResource extends BaseQueryResource<ResultSetCsvMessageBodyWriter> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  public static final String FILE_EXTENSION = ".csv";
  public static final String MIME_TYPE = "text/csv";

  public static final String CSV_ENCODING = "Cp1252";

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public QueryAsCsvResource() {
	super(MIME_TYPE, FILE_EXTENSION);
  }

  // -------------------------------------------------------------------------
  // BaseQueryResource overrides
  // -------------------------------------------------------------------------

  @Override
  protected void initializeResourceResponse(ResourceResponse resourceResponse) {
    super.initializeResourceResponse(resourceResponse);
    resourceResponse.setTextEncoding( CSV_ENCODING );
  }  
  
  @Override
  protected ResultSetCsvMessageBodyWriter getWriter() throws IOException {
    ResultSetCsvMessageBodyWriter writer = new ResultSetCsvMessageBodyWriter();
    writer.setTextEncoding( CSV_ENCODING );
    return writer;
  }
}
