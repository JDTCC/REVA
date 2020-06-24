package nl.xup.tableprinter.spi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import nl.xup.tableprinter.ColumnHeader;
import nl.xup.tableprinter.PrinterSPI;
import nl.xup.tableprinter.Row;
import nl.xup.tableprinter.TablePrinter;
import nl.xup.tableprinter.ColumnHeader.Alignment;

/**
 * Prints tables in xhtml.
 * 
 */
public class XHtmlPrinter implements PrinterSPI {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final String NEWLINE = "\n";
  private static final String HTML_START = "<html  xmlns=\"http://www.w3.org/1999/xhtml\">\n";
  private static final String HTML_END = "</html>\n";
  private static final String BODY_START = "<body>\n";
  private static final String BODY_END = "</body>\n";
  private static final String TABLE_START = "<table>\n";
  private static final String TABLE_END = "</table>\n";
  private static final String TR_START = "<tr>\n";
  private static final String TR_END = "</tr>\n";
  private static final String TH_START = "<th style=\"%s\">";
  private static final String TH_END = "</th>";

  // -------------------------------------------------------------------------
  // Class Attributes
  // -------------------------------------------------------------------------

  private static Map<Alignment, String> alignmentMap = new HashMap<Alignment, String>();

  static {
    alignmentMap.put(Alignment.LEFT, "left");
    alignmentMap.put(Alignment.CENTER, "center");
    alignmentMap.put(Alignment.RIGHT, "right");
  }

  // -------------------------------------------------------------------------
  // Object Attributes
  // -------------------------------------------------------------------------

  private int indent = 0;

  // -------------------------------------------------------------------------
  // Implementing Printer
  // -------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override
  public String print(TablePrinter table) {
    StringBuffer printedTable = new StringBuffer();

    printedTable
        .append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
    printedTable.append(HTML_START);
    indent++;
    printedTable.append(indentString(BODY_START));
    indent++;
    printedTable.append(indentString(TABLE_START));
    indent++;

    if (table != null && table.getColumnHeaders().size() > 0) {
      printedTable.append(headerLine(table.getColumnHeaders()));

      for (Row row : table.getRows()) {
        printedTable.append(rowLine(table.getColumnHeaders(), row));
      }
    } else {
      printedTable.append("(empty)\n");
    }

    indent--;
    printedTable.append(indentString(TABLE_END));
    indent--;
    printedTable.append(indentString(BODY_END));
    indent--;
    printedTable.append(HTML_END);

    return printedTable.toString();
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  /**
   * Serializes the given header in html table header row. The headers are also used for alignment.
   * 
   * @param headers the column headers
   * @return String containing a html formatted header row.
   */
  private String headerLine(List<ColumnHeader> headers) {
    StringBuilder builder = new StringBuilder();

    builder.append(indentString(TR_START));
    indent++;
    for (ColumnHeader columnHeader : headers) {
      builder.append(indentString(String.format(TH_START, getAligmentStyle(columnHeader))));
      builder.append(StringEscapeUtils.escapeHtml(columnHeader.getName()));
      builder.append(TH_END + NEWLINE);
    }

    indent--;
    builder.append(indentString(TR_END));
    return builder.toString();
  }

  /**
   * Serializes the given row in html table row. The headers are used for alignment.
   * 
   * @param headers the column headers
   * @param row the row
   * @return String containing a html formatted row.
   */
  private String rowLine(List<ColumnHeader> headers, Row row) {
    StringBuilder builder = new StringBuilder();

    builder.append(indentString(TR_START));
    indent++;
    int index = 0;
    for (String value : row.getCells()) {
      if (value == null) {
        value = "";
      }
      ColumnHeader header = headers.get(index);
      builder.append(indentString(String.format(TH_START, getAligmentStyle(header))));
      builder.append(StringEscapeUtils.escapeHtml(value));
      builder.append(TH_END + NEWLINE);
      index++;
    }

    indent--;
    builder.append(indentString(TR_END));
    return builder.toString();
  }

  /**
   * Determine the alignment style for the given header.
   * 
   * @param columnHeader the header
   * @return html string with the right alignment style
   */
  private String getAligmentStyle(ColumnHeader columnHeader) {
    return "text-align: " + alignmentMap.get(columnHeader.getAlign()) + ";";
  }

  /**
   * Indent the given string with the current indent level.
   * 
   * @param str to indent
   * @return the indented string.
   */
  private String indentString(String str) {
    return StringUtils.leftPad("", indent, ' ') + str;
  }
}
