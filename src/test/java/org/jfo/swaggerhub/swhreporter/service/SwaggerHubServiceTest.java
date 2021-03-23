package org.jfo.swaggerhub.swhreporter.service;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class SwaggerHubServiceTest {

  private final AdminService adminService = new AdminService();
  private final SwhWebClient webClient = new SwhWebClient();
  
  private final SwaggerHubServiceImpl swaggerHubServiceImpl = new SwaggerHubServiceImpl(adminService, webClient);

  private final String TEST_OWNER = "CREALOGIX";

  @Test
  @Disabled
  void getAllOwnerSpecs() {
    Set<ApisJsonApi> result = swaggerHubServiceImpl.getAllOwnerSpecs(TEST_OWNER);
    Assertions.assertThat(result).isNotEmpty();
  }

  @Test
  @Disabled
  void getApiNameFromUrl() {
    String url = "https://api.swaggerhub.com/apis/username/petstore/1.1";
    String name = swaggerHubServiceImpl.getApiNameFromUrl(url, "username");
    Assertions.assertThat(name).isEqualTo("petstore");
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
