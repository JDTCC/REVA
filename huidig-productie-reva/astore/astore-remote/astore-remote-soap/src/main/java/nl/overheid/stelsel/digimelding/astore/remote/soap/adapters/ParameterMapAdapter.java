package nl.overheid.stelsel.digimelding.astore.remote.soap.adapters;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import nl.overheid.stelsel.digimelding.astore.remote.soap.adapters.ParameterMap.Parameter;

/**
 * JAXB XML Adapter to convert from Map to ParameterMap.
 * 
 */
public class ParameterMapAdapter extends XmlAdapter<ParameterMap, Map<String, String>> {

  // -------------------------------------------------------------------------
  // Implementing XmlAdapter
  // -------------------------------------------------------------------------

  @Override
  public ParameterMap marshal(Map<String, String> parameters) throws Exception {
    ParameterMap parameterMap = new ParameterMap();
    for (Entry<String, String> entry : parameters.entrySet()) {
      Parameter parameter = new Parameter();
      parameter.name = entry.getKey();
      parameter.value = entry.getValue();
      parameterMap.parameters.add(parameter);
    }
    return parameterMap;
  }

  @Override
  public Map<String, String> unmarshal(ParameterMap parameterMap) throws Exception {
    Map<String, String> parameters = new HashMap<String, String>();
    for (Parameter parameter : parameterMap.parameters) {
      parameters.put(parameter.name, parameter.value);
    }
    return parameters;
  }
}
