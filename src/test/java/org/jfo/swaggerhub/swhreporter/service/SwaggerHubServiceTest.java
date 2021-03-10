package org.jfo.swaggerhub.swhreporter.service;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.dto.SpecsDto;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.junit.jupiter.api.Test;

class SwaggerHubServiceTest {

  private final ModelMapper mapper = new ModelMapper();
  private final SwhWebClient webClient = new SwhWebClient();
  private final SwaggerHubService swaggerHubService = new SwaggerHubService(webClient, mapper);

  private final String TEST_OWNER = "CREALOGIX";
  
  @Test
  void getAllOwnerApis(){
    List<ApisJsonApi> result =  swaggerHubService.getAllOwnerApis(TEST_OWNER);
    Assertions.assertThat(result).isNotEmpty();
  }
  
  @Test
  void getAllOwnerSpecs(){
    List<ApisJsonApi> result = swaggerHubService.getAllOwnerSpecs(TEST_OWNER);
    Assertions.assertThat(result).isNotEmpty();
  }
  
      @Test
    public void test_getSpecs() {
        SpecsDto result = swaggerHubService.getSpecs();
        Assertions.assertThat(result).isNotNull();
      }
//        MockWebServer mockWebServer = new MockWebServer();
//
//        //Schedule some response
//        mockWebServer.enqueue(new MockResponse().setBody("Hello world!"));
//
//        // Start the server
//        mockWebServer.start();
//
////        String baseUrl = SwaggerHubService.SPECS_URL;
//
//        // initialize a WebClient with the base url of the mock server
////        final WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
//        final SwhWebClient webClient = new SwhWebClient();
//        // initialize our service class
//        final SwaggerHubService restService = new SwaggerHubService(webClient);
//        // send the request
//        String response = restService.getMonoSpecs().block();
////        final String sendResponse = restService.getSpecs().block();
//
//        // ASSERTIONS
////        assertNotNull(sendResponse);
////        assertEquals("hello, world!", sendResponse);
//
//        // get the recorded request data
//        RecordedRequest request = mockWebServer.takeRequest();
//
////        assertEquals("testval", request.getBody().readUtf8());
//        Assertions.assertThat("GET").isEqualTo(request.getMethod());
//        Assertions.assertThat("/test").isEqualTo(request.getPath());
//
//        mockWebServer.shutdown();
//    }

}
