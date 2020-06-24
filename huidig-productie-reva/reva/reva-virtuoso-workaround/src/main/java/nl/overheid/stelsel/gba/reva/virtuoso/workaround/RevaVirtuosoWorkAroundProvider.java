package nl.overheid.stelsel.gba.reva.virtuoso.workaround;

import java.util.Iterator;
import java.util.UUID;

import org.apache.clerezza.rdf.core.Literal;
import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.Triple;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.impl.PlainLiteralImpl;
import org.apache.clerezza.rdf.core.impl.TripleImpl;

import nl.overheid.stelsel.digimelding.astore.Annotation;
import nl.overheid.stelsel.digimelding.astore.AnnotationContext;
import nl.overheid.stelsel.digimelding.astore.notification.NotificationProvider;
import nl.overheid.stelsel.digimelding.astore.processor.AnnotationProcessor;

/**
 * AStore pre- and post-processor provider to circumvent virtuoso problems on 
 * max(datetime). This is done by adding a marker to the latest address. In case
 * of a new addresses this marker needs to be removed from the previous address.
 * 
 */
public class RevaVirtuosoWorkAroundProvider implements AnnotationProcessor, NotificationProvider {

  // -------------------------------------------------------------------------
  // Constants
  // -------------------------------------------------------------------------

  private static final String CONTEXT_KEY = "virtuoso.workaround.key.old.marker";
  private static final UriRef MARKER_PREDICATE = new UriRef("http://data.reva.nl/2013/07/reva#isLaatsteAdres");
  private static final Literal MARKER_OBJECT = new PlainLiteralImpl("marker");
  private static final UriRef ADRES_PREDICATE = new UriRef("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
  private static final UriRef ADRES_OBJECT = new UriRef("http://data.reva.nl/2013/07/reva#AdresAnnotatie");

  // -------------------------------------------------------------------------
  // Implementing AnnotationProcessor
  // -------------------------------------------------------------------------

  @Override
  public void process(AnnotationContext context, Annotation annotation) {
    // Check if new data already contains the marker (due to import)
    if (!hasMarker( annotation )) {
      // No marker yet, create one for the new data
      Triple marker = createMarker( annotation.getGraph() );
      if ( marker != null ) {
        // ... and add it.
        annotation.getGraph().add( marker );
    	
        // Find marker in existing (stored) data.
        Triple oldMarker = findOldMarker( context.getTree() );
    	
        // Add this marker to the context so it can be removed later on.
        context.setValue(CONTEXT_KEY, oldMarker);
      }
    }
  }
  
  @Override
  public void processRemoval(AnnotationContext context, UUID uuid) {
    // Nothing to do
  }

  // -------------------------------------------------------------------------
  // Implementing NotificationProvider
  // -------------------------------------------------------------------------

  @Override
  public void notify(AnnotationContext context, Annotation annotation) {
    // Get old marker from context and remove it.
    Triple oldMarker = context.getValue(CONTEXT_KEY, Triple.class);
    if ( oldMarker != null ) {
    	context.getTree().remove(oldMarker);
    }
  }

  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  private Triple createMarker( MGraph graph ) {
	Triple marker = null;
	Iterator<Triple> triples = graph.filter(null, ADRES_PREDICATE, ADRES_OBJECT);
	if ( triples.hasNext() ) {
	  marker = new TripleImpl(triples.next().getSubject(), MARKER_PREDICATE, MARKER_OBJECT);
    }
	return marker;
  }

  private Triple findOldMarker( MGraph graph ) {
    Triple oldMarker = null;
    if ( graph != null ) {
	  Iterator<Triple> triples = graph.filter(null, MARKER_PREDICATE, MARKER_OBJECT);
	  if ( triples.hasNext() ) {
	    oldMarker = triples.next();
	  }
    }
	return oldMarker;
  }
  
  private boolean hasMarker( Annotation annotation ) {
    // Has no marker until we're sure it has.  
    boolean hasMarker = false;
    
    if ( annotation.getGraph() != null ) {
      Iterator<Triple> triples = annotation.getGraph().filter(null, MARKER_PREDICATE, MARKER_OBJECT);
      hasMarker = triples.hasNext();
    }
    
    return hasMarker;
  }
}
