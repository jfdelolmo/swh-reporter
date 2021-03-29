package org.jfo.swaggerhub.swhreporter.service;

import org.assertj.core.api.Assertions;
import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.mappers.SwhMapper;
import org.jfo.swaggerhub.swhreporter.model.db.Admin;
import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.jfo.swaggerhub.swhreporter.repository.AdminReactiveRepository;
import org.jfo.swaggerhub.swhreporter.repository.SpecificationReactiveRepository;
import org.jfo.swaggerhub.swhreporter.service.reactive.RxAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
class InitializerServiceTest {

    private final AdminReactiveRepository adminReactiveRepository = mock(AdminReactiveRepository.class);
    private final SpecificationReactiveRepository specificationReactiveRepository = mock(SpecificationReactiveRepository.class);

    private final RxAdminService adminService = new RxAdminService(adminReactiveRepository);
    private final RxSwaggerHubService rxSwaggerHubService = new RxSwaggerHubServiceImpl(new SwhWebClient());
    private final SwhMapper swhMapper = new SwhMapper();


    private final InitializerService initializerService = new InitializerService(
            specificationReactiveRepository,
            rxSwaggerHubService,
            adminService,
            swhMapper
    );

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(InitializerService.class);
    }

    @Test
    @Disabled("Avoid call Swh")
    public void rxInitAllOwnedSpecs() {
        Flux<Specification> flux = Flux.just(new Specification());
        Mono<Long> mono = Mono.just(1L);
        Admin admin = new Admin();
        admin.setOwner("CREALOGIX");
        admin.setPendingToUpdate(true);
        Mono<Admin> adminDummy = Mono.just(admin);

        when(adminReactiveRepository.findByOwner(any())).thenReturn(adminDummy);
        doNothing().when(specificationReactiveRepository).saveAll(anyList());
        when(specificationReactiveRepository.count()).thenReturn(mono);

        Long response = initializerService.rxInitAllOwnedSpecs();

        Assertions.assertThat(response).isEqualTo(1L);
    }

//    private final SwhMapper swhMapper = new SwhMapper();
//    private final NewSpecificationRepository specificationRepository = Mockito.mock(NewSpecificationRepository.class);
//    private final ProjectRepository projectRepository = Mockito.mock(ProjectRepository.class);

//    private final InitializerService initializerService = new InitializerService(
//        rxSwaggerHubService,
//        adminService,
//            swaggerHubServiceImpl,
//            swhMapper,
//            specificationRepository,
//        projectRepository);

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