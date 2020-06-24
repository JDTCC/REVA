package nl.overheid.stelsel.digimelding.astore.remote.jax.providers;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.apache.clerezza.rdf.core.TripleCollection;
import org.apache.clerezza.rdf.core.serializedform.Parser;
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat;

/**
 * Jax-rs Provider for reading graphs from input streams.
 * 
 */
@Provider
@Consumes({SupportedFormat.N3, SupportedFormat.N_TRIPLE, SupportedFormat.RDF_XML,
    SupportedFormat.TURTLE, SupportedFormat.X_TURTLE, SupportedFormat.RDF_JSON})
public class GraphReaderProvider implements MessageBodyReader<TripleCollection> {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private Parser parser;

  // -------------------------------------------------------------------------
  // Implementing MessageBodyReader
  // -------------------------------------------------------------------------

  @Override
  public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations,
      MediaType mediaType) {
    return type.isAssignableFrom(TripleCollection.class);
  }

  @Override
  public TripleCollection readFrom(Class<TripleCollection> type, Type genericType,
      Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
      InputStream entityStream) throws IOException {
    return parser.parse(entityStream, mediaType.toString());
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public Parser getParser() {
    return parser;
  }

  public void setParser(Parser parser) {
    this.parser = parser;
  }
}
