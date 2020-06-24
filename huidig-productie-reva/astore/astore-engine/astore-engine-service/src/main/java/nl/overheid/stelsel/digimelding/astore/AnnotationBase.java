package nl.overheid.stelsel.digimelding.astore;

import java.util.UUID;

import org.apache.clerezza.rdf.core.MGraph;

abstract class AnnotationBase {

  // ------------------------------------------------------------------------
  // Members
  // ------------------------------------------------------------------------

  private MGraph graph;
  private UUID stam;

  // ------------------------------------------------------------------------
  // Getters / Setters
  // ------------------------------------------------------------------------

  public MGraph getGraph() {
    return graph;
  }

  public void setGraph(MGraph graph) {
    this.graph = graph;
  }

  public UUID getStam() {
    return stam;
  }

  public void setStam(UUID stam) {
    this.stam = stam;
  }
}
