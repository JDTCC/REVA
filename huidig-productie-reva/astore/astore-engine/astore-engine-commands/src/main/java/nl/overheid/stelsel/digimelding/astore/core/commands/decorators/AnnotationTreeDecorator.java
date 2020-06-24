package nl.overheid.stelsel.digimelding.astore.core.commands.decorators;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import nl.overheid.stelsel.digimelding.astore.AnnotationTree;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.Triple;
import org.apache.clerezza.rdf.core.serializedform.Serializer;
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat;

/**
 * Decorator for AnnotationTree. This decorator adds a proper toString() to the original type.
 * 
 */
public class AnnotationTreeDecorator extends AnnotationTree implements Decorator<AnnotationTree> {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private final AnnotationTree decorated;
  private String format = SupportedFormat.RDF_XML;
  private Serializer service;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public AnnotationTreeDecorator(AnnotationTree decorated) {
    this.decorated = decorated;
  }

  // -------------------------------------------------------------------------
  // Overriding AnnotationTree
  // -------------------------------------------------------------------------

  @Override
  public MGraph getGraph() {
    return decorated.getGraph();
  }

  @Override
  public void setGraph(MGraph graph) {
    decorated.setGraph(graph);
  }

  @Override
  public UUID getStam() {
    return decorated.getStam();
  }

  @Override
  public void setStam(UUID stam) {
    decorated.setStam(stam);
  }

  // -------------------------------------------------------------------------
  // Implementing Decorator
  // -------------------------------------------------------------------------

  @Override
  public AnnotationTree decorate(AnnotationTree decorated) {
    return new AnnotationTreeDecorator(decorated);
  };

  @Override
  public AnnotationTree getDecorated() {
    return decorated;
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  public void format(String format) {
    this.format = format;
  }

  public void setSerializerService(Serializer service) {
    this.service = service;
  }

  // -------------------------------------------------------------------------
  // Object overrides
  // -------------------------------------------------------------------------

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    builder.append("Stam: ").append(getStam().toString());
    builder.append("\nGraph: \n");

    if (service != null) {
      ByteArrayOutputStream boas = new ByteArrayOutputStream();
      service.serialize(boas, getGraph(), format);
      try {
        builder.append(boas.toString("UTF-8"));
      } catch (UnsupportedEncodingException e) {
        builder.append(e.getMessage());
      }
    } else {
      for (Triple triple : getGraph()) {
        builder.append(triple.getSubject()).append(",");
        builder.append(triple.getPredicate()).append(",");
        builder.append(triple.getObject()).append("\n");
      }
    }

    return builder.toString();
  }
}
