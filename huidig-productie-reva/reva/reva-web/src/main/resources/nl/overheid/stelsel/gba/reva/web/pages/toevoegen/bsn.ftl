<!-- #!Freemarker -->
<#escape x as x?xml>

<!DOCTYPE rdf:RDF [
    <!ENTITY rdf "http://www.w3.org/1999/02/22-rdf-syntax-ns#" >
    <!ENTITY oa "http://www.w3.org/ns/oa#" >
    <!ENTITY reva "http://data.reva.nl/2013/07/reva#" >
    <!ENTITY prov "http://www.w3.org/ns/prov#" >
    <!ENTITY foaf "http://xmlns.com/foaf/0.1/" >
]>
<rdf:RDF
    xmlns:rdf="&rdf;"
    xmlns:oa="&oa;"
    xmlns:reva="&reva;" 
    xmlns:prov="&prov;" 
    xmlns:foaf="&foaf;" > 

  <!-- # Object 1: BSN Annotatie -->
  <rdf:Description rdf:about="urn:uuid:${uuidBsn}">
    <rdf:type rdf:resource="&reva;BSNAnnotatie"/>
    <oa:annotatedAt rdf:datatype="http://www.w3.org/2001/XMLSchema#dateTime">${timestamp}</oa:annotatedAt>    
    <oa:annotatedBy rdf:resource="urn:uuid:${uuidBsn}#agent"/>
    <oa:serializedBy rdf:resource="urn:uuid:${uuidBsn}#softwareAgent"/>
    <oa:hasTarget rdf:resource="urn:uuid:${uuidRoot}"/>
    <oa:hasBody>${reva.bsn}</oa:hasBody>
  </rdf:Description>

  <!-- # Object 2: agent -->
  <rdf:Description rdf:about="urn:uuid:${uuidBsn}#agent">
    <rdf:type rdf:resource="&prov;Agent"/>
    <#if gebruiker??>
      <foaf:nick>${gebruiker}</foaf:nick>
      <foaf:organisation>${gemeenteVanInschrijving}</foaf:organisation>
    <#else>
      <foaf:nick>Onbekend</foaf:nick>
    </#if>
  </rdf:Description>
  
  <!-- # Object 3: software agent -->
  <rdf:Description rdf:about="urn:uuid:${uuidBsn}#softwareAgent">
    <rdf:type rdf:resource="&prov;SoftwareAgent"/>
    <foaf:name>Reva</foaf:name>
  </rdf:Description>
</rdf:RDF>
</#escape>