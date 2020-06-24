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
  
  <!-- # Object 1: Root Annotatie -->
  <rdf:Description rdf:about="urn:uuid:${uuidRoot}">
    <rdf:type rdf:resource="&reva;RootAnnotatie"/>
    <oa:annotatedAt rdf:datatype="http://www.w3.org/2001/XMLSchema#dateTime">${timestamp}</oa:annotatedAt>
    <reva:aanmaakDatumStr>${timestampDate}</reva:aanmaakDatumStr>
    <reva:aanmaakDatumInt rdf:datatype="http://www.w3.org/2001/XMLSchema#integer">${timestampDateInt}</reva:aanmaakDatumInt>
    <oa:annotatedBy rdf:resource="urn:uuid:${uuidRoot}#agent"/>
    <oa:serializedBy rdf:resource="urn:uuid:${uuidRoot}#softwareAgent"/>
  </rdf:Description>

  <!-- # Object 2: agent -->
  <rdf:Description rdf:about="urn:uuid:${uuidRoot}#agent">
    <rdf:type rdf:resource="&prov;Agent"/>
    <#if gebruiker??>
      <foaf:nick>${gebruiker}</foaf:nick>
      <foaf:organisation>${gemeenteVanInschrijving}</foaf:organisation>
    <#else>
      <foaf:nick>Onbekend</foaf:nick>
    </#if>
  </rdf:Description>
  
  <!-- # Object 3: software agent -->
  <rdf:Description rdf:about="urn:uuid:${uuidRoot}#softwareAgent">
    <rdf:type rdf:resource="&prov;SoftwareAgent"/>
    <foaf:name>Reva</foaf:name>
  </rdf:Description>

  <!-- # Object4: Adres Annotatie -->
  <rdf:Description rdf:about="urn:uuid:${uuidAdres}">
    <rdf:type rdf:resource="&reva;AdresAnnotatie"/>
    <oa:annotatedAt rdf:datatype="http://www.w3.org/2001/XMLSchema#dateTime">${timestamp}</oa:annotatedAt>
    <reva:aanmaakDatumStr>${timestampDate}</reva:aanmaakDatumStr>
    <reva:aanmaakDatumInt rdf:datatype="http://www.w3.org/2001/XMLSchema#integer">${timestampDateInt}</reva:aanmaakDatumInt>
    <oa:annotatedBy rdf:resource="urn:uuid:${uuidRoot}#agent"/>
    <oa:serializedBy rdf:resource="urn:uuid:${uuidRoot}#softwareAgent"/>
    <oa:hasTarget rdf:resource="urn:uuid:${uuidRoot}"/>
    <oa:hasBody rdf:resource="urn:uuid:${uuidAdres}#body"/>
  </rdf:Description>
    
  <!-- # Object 5: Adres body -->
  <rdf:Description rdf:about="urn:uuid:${uuidAdres}#body">
    <rdf:type rdf:resource="&reva;AdresBody"/>
    <#if reva.woonPlaats??>
      <reva:woonplaats>${reva.woonPlaats}</reva:woonplaats>
    </#if>
    <#if reva.openbareRuimte??>
      <reva:openbareruimte>${reva.openbareRuimte}</reva:openbareruimte>
    </#if>
    <#if reva.postCode??>
      <reva:postcode>${reva.postCode}</reva:postcode>
    </#if>

    <#if reva.huisNummer??>
      <reva:huisnummer>${reva.huisNummer}</reva:huisnummer>
    </#if>
    <#if reva.huisLetter??>
      <reva:huisletter>${reva.huisLetter}</reva:huisletter>
    </#if>
    <#if reva.huisNummerToevoeging??>
      <reva:huisnummerToevoeging>${reva.huisNummerToevoeging}</reva:huisnummerToevoeging>
    </#if>
    <#if reva.aanduidingHuisNummer??>
      <reva:aanduidingBijHuisnummer>${reva.aanduidingHuisNummer}</reva:aanduidingBijHuisnummer>
    </#if>
    <#if reva.adresType??>
      <reva:adresType>${reva.adresType}</reva:adresType>
    </#if>
    <#if reva.toelichting??>
      <reva:toelichting>${reva.toelichting}</reva:toelichting>
    </#if>
    
    <#if reva.idCodeObject??>
      <reva:bagIdCodePlaats>${reva.idCodeObject}</reva:bagIdCodePlaats>
    </#if>
    <#if reva.idCodeNummerAanduiding??>
      <reva:bagIdCodeNummerAanduiding>${reva.idCodeNummerAanduiding}</reva:bagIdCodeNummerAanduiding>
    </#if>
    <#if reva.bagGebruiksdoelen??>
      <reva:bagGebruiksdoelen>${reva.bagGebruiksdoelen}</reva:bagGebruiksdoelen>
    </#if>
  </rdf:Description>
      
  <!-- # Object 6: Dossiernummer Annotatie -->
  <#if reva.dossierNummer??>
    <rdf:Description rdf:about="urn:uuid:${uuidDossiernummer}">
      <rdf:type rdf:resource="&reva;DossierNummerAnnotatie"/>
      <oa:annotatedAt rdf:datatype="http://www.w3.org/2001/XMLSchema#dateTime">${timestamp}</oa:annotatedAt>
      <oa:annotatedBy rdf:resource="urn:uuid:${uuidRoot}#agent"/>
      <oa:serializedBy rdf:resource="urn:uuid:${uuidRoot}#softwareAgent"/>
      <oa:hasTarget rdf:resource="urn:uuid:${uuidRoot}"/>
      <oa:hasBody>${reva.dossierNummer}</oa:hasBody>
    </rdf:Description>
  </#if>

  <!-- # Object 7: BSN Annotatie -->
  <#if reva.bsn??>
    <rdf:Description rdf:about="urn:uuid:${uuidBsn}">
      <rdf:type rdf:resource="&reva;BSNAnnotatie"/>
      <oa:annotatedAt rdf:datatype="http://www.w3.org/2001/XMLSchema#dateTime">${timestamp}</oa:annotatedAt>
      <oa:annotatedBy rdf:resource="urn:uuid:${uuidRoot}#agent"/>
      <oa:serializedBy rdf:resource="urn:uuid:${uuidRoot}#softwareAgent"/>
      <oa:hasTarget rdf:resource="urn:uuid:${uuidRoot}"/>
      <oa:hasBody>${reva.bsn}</oa:hasBody>
    </rdf:Description>
  </#if>
</rdf:RDF>
</#escape>