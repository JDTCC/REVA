package org.apache.stanbol.rules.rulestore.commands.decorators;

import java.util.Collection;
import java.util.Iterator;

import nl.xup.tableprinter.ColumnHeader.Alignment;
import nl.xup.tableprinter.Row;
import nl.xup.tableprinter.TablePrinter;

import org.apache.stanbol.rules.base.api.Recipe;
import org.apache.stanbol.rules.base.api.util.RecipeList;

/**
 * Decorator for RecipeList. This decorator adds a proper toString() to the
 * original type.
 * 
 */
public class RecipeListDecorator extends RecipeList {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private final RecipeList list;
  private String printedTable = null; // Cache TablePrinter results.

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public RecipeListDecorator( RecipeList list ) {
    this.list = list;
  }

  // -------------------------------------------------------------------------
  // Overriding RecipeList
  // -------------------------------------------------------------------------
  
  @Override
  public boolean add( Recipe recipe ) {
    printedTable = null;
    return list.add( recipe );
  }

  @Override
  public boolean addAll( Collection<? extends Recipe> c ) {
    printedTable = null;
    return list.addAll( c );
  }

  @Override
  public int hashCode() {
    return list.hashCode();
  }

  @Override
  public void clear() {
    printedTable = null;
    list.clear();
  }

  @Override
  public boolean contains( Object o ) {
    return list.contains( o );
  }

  @Override
  public boolean containsAll( Collection<?> c ) {
    return list.containsAll( c );
  }

  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public Iterator<Recipe> iterator() {
    return list.iterator();
  }

  @Override
  public boolean remove( Object o ) {
    printedTable = null;
    return list.remove( o );
  }

  @Override
  public boolean equals( Object obj ) {
    return list.equals( obj );
  }

  @Override
  public boolean removeAll( Collection<?> c ) {
    printedTable = null;
    return list.removeAll( c );
  }

  @Override
  public boolean retainAll( Collection<?> c ) {
    printedTable = null;
    return list.retainAll( c );
  }

  @Override
  public int size() {
    return list.size();
  }

  @Override
  public Object[] toArray() {
    return list.toArray();
  }

  @Override
  public <T> T[] toArray( T[] a ) {
    return list.toArray( a );
  }

  // -------------------------------------------------------------------------
  // Object overrides
  // -------------------------------------------------------------------------

  @Override
  public String toString() {
    if ( printedTable == null ) {
      TablePrinter table = new TablePrinter();
      table.createColumnHeader( "Id" );
      table.createColumnHeader( "# Rules", Alignment.RIGHT );
      table.createColumnHeader( "Description" );
      for( Recipe recipe : this ) {
        Row row = table.createRow();
        row.addCell( recipe.getRecipeID().toString() );
        row.addCell( String.valueOf( recipe.getRuleList().size() ) );
        String description = recipe.getRecipeDescription();
        if ( description == null ) {
          description = "";
        }
        row.addCell( description );
      }
      printedTable = table.toString();
    }
    return printedTable;
  }

}
