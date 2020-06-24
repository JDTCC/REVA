package nl.xup.tableprinter.spi;

import org.apache.commons.lang.StringEscapeUtils;

import nl.xup.tableprinter.PrinterSPI;
import nl.xup.tableprinter.Row;
import nl.xup.tableprinter.TablePrinter;

/**
 * Prints tables in xml.
 * 
 */
public class XmlPrinter implements PrinterSPI {

  // -------------------------------------------------------------------------
  // Object Attributes
  // -------------------------------------------------------------------------

  private String rootElementName = "Table";

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Constructor for a new Xml printer.
   */
  public XmlPrinter() {}

  /**
   * Constructor for a new Xml printer with a specified root element name.
   * 
   * @param rootElementName the name for the root element.
   */
  public XmlPrinter(String rootElementName) {
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

    printedTable.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    printedTable.append("<" + getRootElementName() + ">\n");

    if (table != null && table.getColumnHeaders().size() > 0) {
      for (Row row : table.getRows()) {
        printedTable.append(" <Row>\n");

        int index = 0;
        for (String value : row.getCells()) {
          if (value == null) {
            value = "";
          }
          String columnName = table.getColumnHeaders().get(index).getName();
          printedTable.append("  <" + columnName + ">");
          printedTable.append(StringEscapeUtils.escapeXml(value));
          printedTable.append("</" + columnName + ">\n");
          index++;
        }
        printedTable.append(" </Row>\n");
      }
    }

    printedTable.append("</" + rootElementName + ">\n");

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
}
