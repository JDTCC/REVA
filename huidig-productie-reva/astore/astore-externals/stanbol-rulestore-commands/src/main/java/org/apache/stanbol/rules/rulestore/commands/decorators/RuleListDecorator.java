package org.apache.stanbol.rules.rulestore.commands.decorators;

import java.util.Collection;
import java.util.Iterator;

import nl.xup.tableprinter.Row;
import nl.xup.tableprinter.TablePrinter;

import org.apache.stanbol.rules.base.api.Rule;
import org.apache.stanbol.rules.base.api.util.RuleList;

/**
 * Decorator for RuleList. This decorator adds a proper toString() to the
 * original type.
 * 
 */
public class RuleListDecorator extends RuleList {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private final RuleList list;
  private String printedTable = null; // Cached TablePrinter results.

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public RuleListDecorator( RuleList list ) {
    this.list = list;
  }

  // -------------------------------------------------------------------------
  // Overriding RuleList
  // -------------------------------------------------------------------------
  
  @Override
  public boolean add( Rule semionRule ) {
    printedTable = null;
    return list.add( semionRule );
  }

  @Override
  public boolean addToHead( Rule semionRule ) {
    printedTable = null;
    return list.addToHead( semionRule );
  }

  @Override
  public int hashCode() {
    return list.hashCode();
  }

  @Override
  public boolean addAll( Collection<? extends Rule> c ) {
    printedTable = null;
    return list.addAll( c );
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
  public Iterator<Rule> iterator() {
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
      table.createColumnHeader( "Name" );
      table.createColumnHeader( "Description" );
      for( Rule rule : this ) {
        Row row = table.createRow();
        row.addCell( rule.getRuleID().toString() );
        row.addCell( rule.getRuleName() );
        row.addCell( rule.getDescription() );
      }
      printedTable = table.toString();
    }
    return printedTable;
  }
}
