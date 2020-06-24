package nl.xup.tableprinter.spi;

import java.util.List;

import nl.xup.tableprinter.ColumnHeader;
import nl.xup.tableprinter.PrinterSPI;
import nl.xup.tableprinter.Row;
import nl.xup.tableprinter.TablePrinter;

/**
 * Prints tables in json format.
 * 
 */
public class JSONPrinter implements PrinterSPI {

  // -------------------------------------------------------------------------
  // Object Attributes
  // -------------------------------------------------------------------------

  private String rootElementName = "table";

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Constructor for a new JSON printer.
   */
  public JSONPrinter() {}

  /**
   * Constructor for a new JSON printer with a specified root element name.
   * 
   * @param rootElementName the name for the root element.
   */
  public JSONPrinter(String rootElementName) {
    this.rootElementName = rootElementName;
  }

  // -------------------------------------------------------------------------
  // Implementing Printer
  // -------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  @Override
  public String print(TablePrinter table) {
    StringBuffer printedTable = new StringBuffer();

    printedTable.append("{\n");
    printedTable.append("  \"" + getRootElementName() + "\" : {");

    if (table != null && table.getColumnHeaders().size() > 0) {
      printedTable.append("\n");
      printedTable.append(headerLine(table.getColumnHeaders()));
      printedTable.append("    \"rows\" : [");

      boolean firstRow = true;
      for (Row row : table.getRows()) {
        if (!firstRow) {
          printedTable.append(",");
        }
        printedTable.append("\n");
        printedTable.append(rowLine(table.getColumnHeaders(), row));
        firstRow = false;
      }
      if (!firstRow) {
        printedTable.append("\n    ");
      }
      printedTable.append("]\n");
      printedTable.append("  }\n");
    } else {
      printedTable.append("}\n");
    }

    printedTable.append("}");

    return printedTable.toString();
  }

  // -------------------------------------------------------------------------
  // Getter / Setters
  // -------------------------------------------------------------------------

  /**
   * Gives the root element name used by the printer.
   * 
   * @return String with the root element name.
   */
  public String getRootElementName() {
    return rootElementName;
  }

  /**
   * Sets the root element name to the given name.
   * 
   * @param rootElementName the new root element name.
   */
  public void setRootElementName(String rootElementName) {
    this.rootElementName = rootElementName;
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private String headerLine(List<ColumnHeader> headers) {
    StringBuilder builder = new StringBuilder();

    builder.append("    \"headers\" : [");
    String prefix = " ";
    for (ColumnHeader columnHeader : headers) {
      builder.append(prefix + "\"" + columnHeader.getName() + "\"");
      prefix = ", ";
    }

    return builder.append(" ],\n").toString();
  }

  private String rowLine(List<ColumnHeader> headers, Row row) {
    StringBuilder builder = new StringBuilder();
    builder.append("      {\n");

    int index = 0;
    for (String cell : row.getCells()) {
      if (index != 0) {
        builder.append(",\n");
      }
      String columnName = headers.get(index).getName();
      builder.append("        \"" + columnName + "\": ");
      builder.append("\"" + cell + "\"");
      index++;
    }
    builder.append("\n      }");
    return builder.toString();
  }
}
