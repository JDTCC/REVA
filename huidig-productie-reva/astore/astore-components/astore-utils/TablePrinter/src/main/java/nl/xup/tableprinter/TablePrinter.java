package nl.xup.tableprinter;

import java.util.ArrayList;
import java.util.List;

import nl.xup.tableprinter.ColumnHeader.Alignment;
import nl.xup.tableprinter.spi.AsciiPrinter;

/**
 * Table Printer.
 * 
 */
public class TablePrinter {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private List<ColumnHeader> columnHeaders = new ArrayList<>();
  private List<Row> rows = new ArrayList<>();
  private PrinterSPI printer = new AsciiPrinter();

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Constructor for a new table printer.
   */
  public TablePrinter() {}

  // -------------------------------------------------------------------------
  // Public Interface
  // -------------------------------------------------------------------------

  /**
   * Gives the list of column headers for the table.
   * 
   * @return
   */
  public List<ColumnHeader> getColumnHeaders() {
    return columnHeaders;
  }

  /**
   * Creates a new ColumnHeader and adds it to the table.
   * 
   * @param name the name to be printed in the column header.
   * @return
   */
  public ColumnHeader createColumnHeader(String name) {
    ColumnHeader header = new ColumnHeader(name);
    columnHeaders.add(header);
    return header;
  }

  /**
   * Creates a new ColumnHeader and adds it to the table.
   * 
   * @param name the name to be printed in the column header.
   * @param align the alignment to be used for the column.
   * @return
   */
  public ColumnHeader createColumnHeader(String name, Alignment align) {
    ColumnHeader header = new ColumnHeader(name, align);
    columnHeaders.add(header);
    return header;
  }

  /**
   * Gives the list of all rows (except for the header) making up the table.
   * 
   * @return
   */
  public List<Row> getRows() {
    return rows;
  }

  /**
   * Creates a new row and adds it to the table.
   * 
   * @return
   */
  public Row createRow() {
    Row row = new Row();
    rows.add(row);
    return row;
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  /**
   * Gives the printer SPI to be used for actual printing.
   * 
   * @return PrinterSPI object.
   */
  public PrinterSPI getPrinter() {
    return printer;
  }

  /**
   * Sets the printer SPI to be used for actual printing.
   * 
   * @param printer the printe SPI to be used.
   */
  public void setPrinter(final PrinterSPI printer) {
    if (printer == null) {
      this.printer = new AsciiPrinter();
    } else {
      this.printer = printer;
    }
  }

  // -------------------------------------------------------------------------
  // Object overrides
  // -------------------------------------------------------------------------

  @Override
  public String toString() {
    return printer.print(this);
  }
}
