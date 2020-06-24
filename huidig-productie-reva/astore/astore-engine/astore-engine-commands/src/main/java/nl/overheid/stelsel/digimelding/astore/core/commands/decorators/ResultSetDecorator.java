package nl.overheid.stelsel.digimelding.astore.core.commands.decorators;

import java.util.List;

import nl.xup.tableprinter.Row;
import nl.xup.tableprinter.TablePrinter;

import org.apache.clerezza.rdf.core.BNode;
import org.apache.clerezza.rdf.core.PlainLiteral;
import org.apache.clerezza.rdf.core.Resource;
import org.apache.clerezza.rdf.core.TypedLiteral;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.sparql.ResultSet;
import org.apache.clerezza.rdf.core.sparql.SolutionMapping;

/**
 * Decorator for Clerezza ResultSet. This decorator add a proper toString() to the original type.
 * 
 */
public class ResultSetDecorator implements ResultSet, Decorator<ResultSet> {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private final ResultSet decorated;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public ResultSetDecorator(ResultSet decorated) {
    this.decorated = decorated;
  }

  // -------------------------------------------------------------------------
  // Implementing Decorator
  // -------------------------------------------------------------------------

  @Override
  public ResultSet decorate(ResultSet decorated) {
    return new ResultSetDecorator(decorated);
  };

  @Override
  public ResultSet getDecorated() {
    return decorated;
  }

  // -------------------------------------------------------------------------
  // Implementing ResultSet
  // -------------------------------------------------------------------------

  @Override
  public boolean hasNext() {
    return decorated.hasNext();
  }

  @Override
  public SolutionMapping next() {
    return decorated.next();
  }

  @Override
  public void remove() {
    decorated.remove();
  }

  @Override
  public List<String> getResultVars() {
    return decorated.getResultVars();
  }

  // -------------------------------------------------------------------------
  // Object overrides
  // -------------------------------------------------------------------------

  @Override
  public String toString() {
    // NOTE: toString() can only be done once. This is due to the inability to
    // reset the iterator (next() method). So on the next call to toString() the
    // underlying ResultSet is considered empty.
    // NOTE: Be careful when debugging. Showing the variable results consumes
    // the ResultSet. As a result the second view of the variable will show an
    // empty ResultSet.
    TablePrinter table = new TablePrinter();
    for (String variable : getResultVars()) {
      table.createColumnHeader(variable);
    }

    while (hasNext()) {
      SolutionMapping row = next();
      Row tableRow = table.createRow();
      for (String variable : getResultVars()) {
        Resource resource = row.get(variable);
        if (resource == null) {
          tableRow.addCell(null);
        } else {
          tableRow.addCell(getResourceValue(resource));
        }
      }
    }

    String response = table.toString();
    if (response.isEmpty()) {
      response = "(empty)";
    }
    return response;
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
