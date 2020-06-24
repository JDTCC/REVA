package nl.xup.tableprinter;

/**
 * Table Printer column header configuration.
 * 
 */
public class ColumnHeader {

  // -------------------------------------------------------------------------
  // Enumerations
  // -------------------------------------------------------------------------

  public enum Alignment {
    LEFT, CENTER, RIGHT
  }

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private String name;
  private Alignment align;
  private int width;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /**
   * Constructor to create a new header with the given name.
   * 
   * @param name the header name.
   */
  ColumnHeader(String name) {
    this(name, Alignment.LEFT);
  }

  /**
   * Constructor to create a new header with the given name and alignment for the whole column.
   * 
   * @param name the header name.
   * @param align the column alignment
   */
  ColumnHeader(String name, Alignment align) {
    setName(name);
    setAlign(align);
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  /**
   * Gives the header name.
   * 
   * @return header name as a string.
   */
  public final String getName() {
    return name;
  }

  /**
   * Set the name for the header
   * 
   * @param name the name for the header.
   * @throws IllegalArgumentException when given name is null.
   */
  public final void setName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Name can't be null!");
    }
    this.name = name;
  }

  /**
   * Gives the alignment for the column.
   * 
   * @return the Alignment.
   */
  public final Alignment getAlign() {
    return align;
  }

  /**
   * Set the alignment for the column.
   * 
   * @param align the alignment for the column.
   * @throws IllegalArgumentException when given alignment is null.
   */
  public final void setAlign(Alignment align) {
    if (align == null) {
      throw new IllegalArgumentException("Alignment can't be null!");
    }
    this.align = align;
  }

  /**
   * Gives the maximum width for the column.
   * 
   * @return
   */
  public final int getWidth() {
    return width;
  }

  /**
   * Sets the maximum width for the column.
   * 
   * @param width the width for the column
   */
  public final void setWidth(int width) {
    this.width = width;
  }
}
