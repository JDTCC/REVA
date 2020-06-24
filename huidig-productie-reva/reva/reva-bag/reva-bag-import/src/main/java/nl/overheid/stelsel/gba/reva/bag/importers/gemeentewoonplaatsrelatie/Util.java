package nl.overheid.stelsel.gba.reva.bag.importers.gemeentewoonplaatsrelatie;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

import nl.overheid.stelsel.gba.reva.bag.importers.gemeentewoonplaatsrelatie.Relaties.Relatie;

public class Util {
  protected static final String XSLT_RELATIE = "gemeente-woonplaats-relatie.xslt";
  protected static final String XSLT_EXTRACT_DATUM = "gemeente-woonplaats-datum.xslt";

  public Util() {
  }

  public BAGExtract getExtractDatum( Document source ) throws TransformerException, JAXBException {
    // XML structuur versimpelen.
    Document extractDom = transformGemeenteWoonplaatsRelatieDocument( source, XSLT_EXTRACT_DATUM );

    // XML omzetten naar pojo.
    JAXBContext jaxb = JAXBContext.newInstance( BAGExtract.class );
    return (BAGExtract) jaxb.createUnmarshaller().unmarshal( extractDom );
  }

  public List<Relatie> getGemeenteWoonplaatsRelaties( Document source ) throws TransformerException, JAXBException {
    // XML structuur versimpelen.
    Document relatiesDom = transformGemeenteWoonplaatsRelatieDocument( source, XSLT_RELATIE );

    // XML omzetten naar pojo.
    JAXBContext jaxb = JAXBContext.newInstance( Relaties.class );
    Relaties relaties = (Relaties) jaxb.createUnmarshaller().unmarshal( relatiesDom );

    return relaties.getRelatie();
  }

  protected Document transformGemeenteWoonplaatsRelatieDocument( Document document, String xsltFilename )
          throws TransformerException {
    Source xslt = new StreamSource( Thread.currentThread().getContextClassLoader().getResourceAsStream( xsltFilename ) );
    DOMSource source = new DOMSource( document );
    DOMResult result = new DOMResult();

    TransformerFactory factory = TransformerFactory.newInstance();
    Transformer transformer = factory.newTransformer( xslt );
    transformer.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "4" );
    transformer.transform( source, result );

    return (Document) result.getNode();
  }
}
