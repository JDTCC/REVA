package nl.overheid.stelsel.digimelding.astore.remote.jax.providers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import nl.xup.tableprinter.PrinterSPI;
import nl.xup.tableprinter.Row;
import nl.xup.tableprinter.TablePrinter;
import nl.xup.tableprinter.spi.AsciiPrinter;
import nl.xup.tableprinter.spi.XHtmlPrinter;

import org.apache.clerezza.rdf.core.BNode;
import org.apache.clerezza.rdf.core.PlainLiteral;
import org.apache.clerezza.rdf.core.Resource;
import org.apache.clerezza.rdf.core.TypedLiteral;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.sparql.ResultSet;
import org.apache.clerezza.rdf.core.sparql.SolutionMapping;

/**
 * Jax-rs Provider for writing graphs to output streams.
 * 
 */
@Provider
@Produces({MediaType.APPLICATION_XHTML_XML, MediaType.TEXT_HTML, MediaType.TEXT_PLAIN})
public class ResultSetTextMessageBodyWriter implements MessageBodyWriter<ResultSet> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static Map<String, PrinterSPI> printers;

  static {
    PrinterSPI htmlPrinter = new XHtmlPrinter();

    printers = new HashMap<>();
    printers.put(MediaType.APPLICATION_XHTML_XML, htmlPrinter);
    printers.put(MediaType.TEXT_HTML, htmlPrinter);
    printers.put(MediaType.TEXT_PLAIN, new AsciiPrinter());
  }

  // -------------------------------------------------------------------------
  // Implementing MessageBodyWriter
  // -------------------------------------------------------------------------

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations,
      MediaType mediaType) {
    return ResultSet.class.isAssignableFrom(type) && printers.containsKey(mediaType.toString());
  }

  @Override
  public long getSize(ResultSet t, Class<?> type, Type genericType, Annotation[] annotations,
      MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(ResultSet t, Class<?> type, Type genericType, Annotation[] annotations,
      MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
      throws IOException {
    TablePrinter printer = getTablePrinter(t);
    printer.setPrinter(printers.get(mediaType.toString()));

    entityStream.write(printer.toString().getBytes("UTF-8"));
  }

  // -------------------------------------------------------------------------
  // Private Methods
  // -------------------------------------------------------------------------

  public TablePrinter getTablePrinter(ResultSet resultSet) {
    TablePrinter table = new TablePrinter();

    // Start with header
    List<String> headers = resultSet.getResultVars();
    for (String variable : headers) {
      table.createColumnHeader(variable);
    }

    while (resultSet.hasNext()) {
      SolutionMapping row = resultSet.next();

      Row tableRow = table.createRow();
      for (String variable : headers) {
        Resource resource = row.get(variable);
        if (resource == null) {
          tableRow.addCell(null);
        } else {
          tableRow.addCell(getResourceValue(resource));
        }
      }
    }

    return table;
  }

  /**
   * Helper to get the proper string representation for the given Resource.
   */
  private String getResourceValue(Resource resource) {
    StringBuilder value = new StringBuilder();
    if (resource instanceof UriRef) {
      value.append(((UriRef) resource).getUnicodeString());
    } else if (resource instanceof TypedLiteral) {
      value.append(((TypedLiteral) resource).getLexicalForm());
    } else if (resource instanceof PlainLiteral) {
      value.append(((PlainLiteral) resource).getLexicalForm());
    } else if (resource instanceof BNode) {
      value.append("/");
    } else {
      value.append(resource.toString());
    }
    return value.toString();
  }
}
