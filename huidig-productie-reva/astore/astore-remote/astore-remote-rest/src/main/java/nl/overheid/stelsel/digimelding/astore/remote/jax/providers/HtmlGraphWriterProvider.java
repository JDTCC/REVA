package nl.overheid.stelsel.digimelding.astore.remote.jax.providers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import nl.overheid.stelsel.digimelding.astore.remote.web.StaticResources;
import nl.xup.template.Bindings;
import nl.xup.template.SimpleBindings;
import nl.xup.template.Template;
import nl.xup.template.TemplateEngineService;

import org.apache.clerezza.rdf.core.TripleCollection;
import org.apache.clerezza.rdf.core.serializedform.Serializer;
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat;

/**
 * Jax-rs Provider for writing graphs to output streams.
 * 
 */
@Provider
@Produces({SupportedFormat.HTML, SupportedFormat.XHTML})
public class HtmlGraphWriterProvider implements MessageBodyWriter<TripleCollection> {

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------

  private Serializer serializer;
  private TemplateEngineService templating;

  private Template template = null;

  @Context
  private UriInfo ui;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  public HtmlGraphWriterProvider() {}

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
      OutputStream entityStream) throws IOException, WebApplicationException {
    // Serialize to turtle format.
    ByteArrayOutputStream boas = new ByteArrayOutputStream();
    serializer.serialize(boas, t, SupportedFormat.TURTLE);

    // Prepare bindings
    Bindings bindings = new SimpleBindings();
    bindings.put("graph", boas.toString("UTF-8"));
    bindings.put("base", ui.getBaseUri().toString());
    bindings.put("staticweb", StaticResources.STATICWEB);

    // construct response from template.
    try {
      template = getTemplate();
      entityStream.write(template.execute(bindings).getBytes("UTF-8"));
    } catch (Exception e) {
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    }
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

  public TemplateEngineService getTemplating() {
    return templating;
  }

  public void setTemplating(TemplateEngineService templating) {
    this.templating = templating;
  }

  public Template getTemplate() throws Exception {
    if (template == null) {
      Reader reader = new InputStreamReader(getClass().getResourceAsStream("index.html"));
      template = templating.getTemplate(reader);
    }
    return template;
  }
}
