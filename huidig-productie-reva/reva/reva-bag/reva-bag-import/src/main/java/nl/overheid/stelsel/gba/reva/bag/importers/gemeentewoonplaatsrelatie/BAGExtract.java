package nl.overheid.stelsel.gba.reva.bag.importers.gemeentewoonplaatsrelatie;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlRootElement( name = "extract" )
public class BAGExtract {
  @XmlElement( name = "datum" )
  private String datum;

  public String getDatum() {
    return datum;
  }

  public void setDatum( String datum ) {
    this.datum = datum;
  }
}
