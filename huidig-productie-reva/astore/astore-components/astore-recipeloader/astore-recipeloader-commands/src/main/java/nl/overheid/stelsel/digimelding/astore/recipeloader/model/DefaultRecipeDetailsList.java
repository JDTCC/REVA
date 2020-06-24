package nl.overheid.stelsel.digimelding.astore.recipeloader.model;

import java.util.ArrayList;

import nl.xup.tableprinter.ColumnHeader.Alignment;
import nl.xup.tableprinter.Row;
import nl.xup.tableprinter.TablePrinter;

/**
 * Dedicated list for DefaultRecipeDetails with the ability to output proper
 * tables (toString).
 * 
 */
public class DefaultRecipeDetailsList extends ArrayList<DefaultRecipeDetails> {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 874606321146714997L;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public DefaultRecipeDetailsList() {
    super();
  }

  public DefaultRecipeDetailsList( int initialCapacity ) {
    super( initialCapacity );
  }

  public DefaultRecipeDetailsList( DefaultRecipeDetailsList list ) {
    super( list );
  }

  // -------------------------------------------------------------------------
  // Object overrides
  // -------------------------------------------------------------------------

  @Override
  public String toString() {
    TablePrinter table = new TablePrinter();
    table.createColumnHeader( "Recupe Id" );
    table.createColumnHeader( "Bundle Id" );
    table.createColumnHeader( "Modified", Alignment.CENTER );
    for( DefaultRecipeDetails details : this ) {
      Row row = table.createRow();
      row.addCell( details.getRecipeId().toString() );
      row.addCell( String.valueOf( details.getBundleId() ) );
      row.addCell( (details.isModified() ? "Y" : "N") );
    }
    return table.toString();
  }
}
