package nl.overheid.stelsel.digimelding.astore.remote.soap.adapters;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * JAXB supported class to serialize Soap service parameter maps.
 * 
 */
@XmlType(name = "ParameterMap")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParameterMap {
  List<Parameter> parameters = new ArrayList<Parameter>();

  public List<Parameter> getEntries() {
    return parameters;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "IdentifiedUser")
  static class Parameter {
    @XmlAttribute
    String name;

    @XmlValue
    String value;
  }
}
