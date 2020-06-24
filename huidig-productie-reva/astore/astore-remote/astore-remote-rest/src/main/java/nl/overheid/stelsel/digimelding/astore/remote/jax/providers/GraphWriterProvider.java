package nl.overheid.stelsel.digimelding.astore.remote.jax.providers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.clerezza.rdf.core.TripleCollection;
import org.apache.clerezza.rdf.core.serializedform.Serializer;
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat;

/**
 * Jax-rs Provider for writing graphs to output streams.
 * 
 */
@Provider
@Produces({SupportedFormat.N3, SupportedFormat.N_TRIPLE, SupportedFormat.RDF_XML,
    SupportedFormat.TURTLE, SupportedFormat.X_TURTLE, SupportedFormat.RDF_JSON})
public class GraphWriterProvider implements MessageBodyWriter<TripleCollection> {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private Serializer serializer;

  // -------------------------------------------------------------------------
  // Implementing MessageBodyWriter
  // -------------------------------------------------------------------------

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations,
      MediaType mediaType) {
    return TripleCollection.class.isAssignableFrom(type);
  }

  @Override
  public long getSize(TripleCollection t, Class<?> type, Type genericType,
      Annotation[] annotations, MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(TripleCollection t, Class<?> type, Type genericType,
      Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
      OutputStream entityStream) throws IOException {
    serializer.serialize(entityStream, t, mediaType.toString());
  }

  // -------------------------------------------------------------------------
  // Getters / Setters
  // -------------------------------------------------------------------------

  public Serializer getSerializer() {
    return serializer;
  }

  public void setSerializer(Serializer serializer) {
    this.serializer = serializer;
  }
}
