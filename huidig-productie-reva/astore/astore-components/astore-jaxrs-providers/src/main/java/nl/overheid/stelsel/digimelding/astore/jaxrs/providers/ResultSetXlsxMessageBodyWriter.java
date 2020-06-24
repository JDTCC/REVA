package nl.overheid.stelsel.digimelding.astore.jaxrs.providers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.clerezza.rdf.core.Resource;
import org.apache.clerezza.rdf.core.sparql.ResultSet;
import org.apache.clerezza.rdf.core.sparql.SolutionMapping;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import nl.overheid.stelsel.digimelding.astore.utils.ResourceUtils;

/**
 * MessageBodyWirter for <code>ResultSet</code>. Resulting output is Microsoft Excel 2007 OOXML
 * (.xlsx)
 * 
 */
@Component
@Service(Object.class)
@Property(name = "javax.ws.rs", boolValue = true)
@Produces({"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"})
@Provider
public class ResultSetXlsxMessageBodyWriter implements MessageBodyWriter<ResultSet> {

  public ResultSetXlsxMessageBodyWriter() {}

  // --------------------------------------------------------------------------
  // Implementing MessageBodyWriter
  // --------------------------------------------------------------------------

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations,
      MediaType mediaType) {
    return ResultSet.class.isAssignableFrom(type);
  }

  @Override
  public long getSize(ResultSet t, Class<?> type, Type genericType, Annotation[] annotations,
      MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(ResultSet resultSet, Class<?> type, Type genericType,
      Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
      OutputStream entityStream) throws IOException, WebApplicationException {
    int rownum = 0;
    SXSSFWorkbook workbook = null;

    try {
      workbook = new SXSSFWorkbook(100);
      Sheet sheet = workbook.createSheet();

      Row row = sheet.createRow(rownum);
      writeHeader(row, resultSet.getResultVars());
      while (resultSet.hasNext()) {
        rownum++;
        row = sheet.createRow(rownum);
        writeLine(row, resultSet.getResultVars(), resultSet.next());
      }

      // Flush the constructed workbook.
      workbook.write(entityStream);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    } finally {
      // Disposing temporary files is mandatory (according to spec).
      if (workbook != null) {
        workbook.dispose();
      }
    }
  }

  // --------------------------------------------------------------------------
  // Private methods
  // --------------------------------------------------------------------------

  /**
   * Write resultset header to the given output stream.
   * 
   * @param row Spreadsheet row to write to.
   * @param headers the headers to write.
   */
  private void writeHeader(Row row, List<String> headers) {
    int column = 0;
    for (String header : headers) {
      Cell cell = row.createCell(column);
      cell.setCellValue(header);
      column++;
    }
  }

  /**
   * Write a single csv line using the given line data.
   * 
   * @param row spreadsheet row to write to.
   * @param headers the headers to write line data for.
   * @param lineData the line data to write.
   */
  private void writeLine(Row row, List<String> headers, SolutionMapping lineData) {
    int column = 0;
    for (String header : headers) {
      Resource resource = lineData.get(header);
      Cell cell = row.createCell(column);
      cell.setCellValue(ResourceUtils.getResourceValue(resource));
      column++;
    }
  }
}
