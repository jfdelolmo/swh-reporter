package org.jfo.swaggerhub.swhreporter.mappers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SwhMapperTest {

  private final SwhMapper mapper = new SwhMapper();
  
  @Test
  void extractNameFromUrl_api(){
    String apiUrl = "https://api.swaggerhub.com/apis/CREALOGIX/aso-admin_api/2.0.6";
    String apiName = "aso-admin_api";
    
    String name = mapper.extractNameFromUrl(apiUrl);
    Assertions.assertThat(name).isEqualTo(apiName);
  }
  
  @Test
  void extractNameFromUrl_domain(){
    String apiUrl = "https://api.swaggerhub.com/domains/CREALOGIX/Calculation_domain_std/1";
    String apiName = "Calculation_domain_std";
    
    String name = mapper.extractNameFromUrl(apiUrl);
    Assertions.assertThat(name).isEqualTo(apiName);
  }
  
//  public NewOpenApiDocument specificationAsStringToOpenApiDocument(String specificationAsString) throws Exception {
//    NewOpenApiDocument openApiDocument = new NewOpenApiDocument();
//
////    openApiDocument.setDefinition(ClobProxy.generateProxy(specificationAsString));
//    openApiDocument.setDefinition(specificationAsString);
//    return openApiDocument;
//  }
//
//  private byte[] openApiToByteArray(OpenAPI openAPI) throws Exception {
//    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//    ObjectOutputStream oos = new ObjectOutputStream(baos);
//    oos.writeObject(openAPI);
//    oos.flush();
//    return baos.toByteArray();
//  }
//
//  private String apiStringYaml(String specificationAsString) {
//    return new Yaml().dump(specificationAsString);
//  }
}