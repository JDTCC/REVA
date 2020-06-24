package nl.overheid.stelsel.gba.reva.bag.importers.gemeentewoonplaatsrelatie;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType( XmlAccessType.FIELD )
@XmlRootElement( name = "relaties" )
public class Relaties {

  @XmlElement( name = "relatie" )
  private List<Relatie> relatie;

  public List<Relatie> getRelatie() {
    if( relatie == null ) {
      relatie = new ArrayList<Relatie>();
    }
    return relatie;
  }

  @XmlAccessorType( XmlAccessType.FIELD )
  @XmlType( name = "", propOrder = { "woonplaats", "gemeente" } )
  public static class Relatie {
    @XmlElement( name = "gemeente" )
    private String gemeente;

    @XmlElement( name = "woonplaats" )
    private String woonplaats;

    public void setGemeente( String gemeente ) {
      this.gemeente = gemeente;
    }

    public String getGemeente() {
      return gemeente;
    }

    public void setWoonplaats( String woonplaats ) {
      this.woonplaats = woonplaats;
    }

    public String getWoonplaats() {
      return woonplaats;
    }
  }
}
