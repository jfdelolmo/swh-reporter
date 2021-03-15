package org.jfo.swaggerhub.swhreporter.service;

import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.mappers.SwhMapper;
import org.jfo.swaggerhub.swhreporter.repository.NewSpecificationRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InitializerServiceTest {

    private final AdminService adminService = new AdminService();
    private final SwaggerHubService swaggerHubService = new SwaggerHubService(adminService, new SwhWebClient(), new ModelMapper());
    private final SwhMapper swhMapper = new SwhMapper();
    private final NewSpecificationRepository specificationRepository = Mockito.mock(NewSpecificationRepository.class);
    private final InitializerService initializerService = new InitializerService(adminService,
            swaggerHubService,
            swhMapper,
            specificationRepository);

//  @Test
//  @DisplayName("When we want to retrieve all the specifications")
//  void retrieveAllSpecs() {
//    int result = initializerService.retrieveAllSpecs();
//    Assertions.assertThat(result).isGreaterThan(0);
//  }

//    @Test
//    void test() throws Exception {
//        String url = "https://app.swaggerhub.com/apis/CREALOGIX/aso-admin_api/2.0.5";
//        String api = swaggerHubService.getApiVersionByUrl(url);
//        Specification entity = new Specification();
//        entity.setName("DummyName");
//        entity.setDescription("DummyDescription");
//        entity.setProperties(new Properties());
////    entity.getProperties().setSpecification(entity);
//        entity.getProperties().setCreated(OffsetDateTime.now());
//        entity.getProperties().setModified(OffsetDateTime.now());
//        entity.getProperties().setType("API");
//        entity.getProperties().setUrl(url);
//        entity.getProperties().setVersion("2.0.5");
////        entity.setOpenApiDocument(new OpenApiDocument());
////        entity.getOpenApiDocument().setSpecification(entity);
////        entity.setOpenApiDocument(swhMapper.specificationAsStringToOpenApiDocument(api));
////    entity.setCollaborationModel(null);
//
//        NewSpecification result = specificationRepository.save(entity);
//        Assertions.assertThat(result).isNotNull();
//    }

}