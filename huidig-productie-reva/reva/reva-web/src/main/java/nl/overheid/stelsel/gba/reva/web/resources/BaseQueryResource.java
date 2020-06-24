package nl.overheid.stelsel.gba.reva.web.resources;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyWriter;

import org.apache.clerezza.rdf.core.sparql.ResultSet;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.request.mapper.parameter.INamedParameters.NamedPair;
import org.apache.wicket.request.resource.AbstractResource;
import org.osgi.framework.ServiceException;

import nl.overheid.stelsel.digimelding.astore.AnnotationStoreService;
import nl.overheid.stelsel.digimelding.astore.authorisation.AuthorisationException;
import nl.overheid.stelsel.gba.reva.web.locators.AnnotationStoreServiceLocator;
import nl.overheid.stelsel.gba.reva.web.security.RevaPermissions;
import nl.overheid.stelsel.gba.reva.web.utils.ProfileUtils;

abstract class BaseQueryResource<T extends MessageBodyWriter<ResultSet>> extends AbstractResource {

  // -------------------------------------------------------------------------
  // Class attributes
  // -------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  public static final String QUERY_NAME_PARAM = "queryName";

  // -------------------------------------------------------------------------
  // Object attributes
  // -------------------------------------------------------------------------
  
  private String fileExtension;
  private String mimeType;
  
  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------
  
  public BaseQueryResource( final String mimeType, final String fileExtension ) {
	this.mimeType = mimeType;
	this.fileExtension = fileExtension;
  }
  
  // -------------------------------------------------------------------------
  // AbstractResource overrides
  // -------------------------------------------------------------------------

  @Override
  protected ResourceResponse newResourceResponse( Attributes attributes ) {
    ResourceResponse resourceResponse = getResourceResponse();
    resourceResponse.setFileName( getQueryName( attributes ) + fileExtension );

    try {
      final ResultSet results = executeQuery( attributes );

      resourceResponse.setWriteCallback( new WriteCallback() {
        @Override
        public void writeData( Attributes attributes ) throws IOException {
          OutputStream outputStream = attributes.getResponse().getOutputStream();
          T writer = getWriter();
          writer.writeTo( results, null, null, null, null, null, outputStream );
        }
      } );
    } catch( AuthorisationException aex ) {
      resourceResponse.setError( Response.Status.FORBIDDEN.getStatusCode(), aex.getMessage() );
    } catch( ServiceException sex ) {
      resourceResponse.setError( Response.Status.NOT_FOUND.getStatusCode(), sex.getMessage() );
    } catch( Exception ex ) {
      resourceResponse.setError( Response.Status.INTERNAL_SERVER_ERROR.getStatusCode() );
    }

    return resourceResponse;
  }

  // -------------------------------------------------------------------------
  // Interface
  // -------------------------------------------------------------------------

  protected abstract T getWriter() throws IOException;
  
  protected void initializeResourceResponse( ResourceResponse resourceResponse ) {
    resourceResponse.setContentType( mimeType );
  };
  
  // -------------------------------------------------------------------------
  // Private methods
  // -------------------------------------------------------------------------

  protected ResourceResponse getResourceResponse() {
    ResourceResponse resourceResponse = new ResourceResponse();
    initializeResourceResponse( resourceResponse );
    return resourceResponse;
  }
  
  private ResultSet executeQuery( Attributes attributes ) throws Exception {
    Subject subject = SecurityUtils.getSubject();
    // Determine the query to be executed
    String queryName = getQueryName( attributes );

    // Convert resource query parameters into actual query parameters
    Map<String, String> queryParameters = convertToQueryParameters( attributes );

    // Mag de gebruiker het BSN nummer wel zien?
    if( subject.isPermitted( RevaPermissions.RAADPLEGEN_BSN.getStringPermission() ) ) {
      queryParameters.put( "toonBSN", "true" );
    }

    if( !subject.isPermitted( RevaPermissions.ZOEKEN_ALLES.getStringPermission() ) ) {
      // Gebruiker mag niet allen zien, dus restrictie op basis van gemeentecode
      // toevoegen.
      String gemeentecode = ProfileUtils.getGemeentecode( SecurityUtils.getSubject() );
      queryParameters.put( "gemeenteCode", gemeentecode );
    }

    // Execute the query
    Object result = getAnnotationStoreService().namedQuery( queryName, queryParameters );

    if( result instanceof ResultSet ) {
      return ResultSet.class.cast( result );
    }

    throw new RuntimeException( "Invalid data retrieval query '" + queryName + "', check your query!" );
  }

  private String getQueryName( Attributes attributes ) {
    // Determine the query to be executed
    return attributes.getParameters().get( QUERY_NAME_PARAM ).toString();
  }

  private Map<String, String> convertToQueryParameters( Attributes attributes ) {
    Map<String, String> parameters = new HashMap<>();

    for( NamedPair pair : attributes.getParameters().getAllNamed() ) {
      if( !QUERY_NAME_PARAM.equals( pair.getKey() ) ) {
        parameters.put( pair.getKey(), pair.getValue() );
      }
    }

    return parameters;
  }

  protected AnnotationStoreService getAnnotationStoreService() {
    return AnnotationStoreServiceLocator.getService();
  }
}
