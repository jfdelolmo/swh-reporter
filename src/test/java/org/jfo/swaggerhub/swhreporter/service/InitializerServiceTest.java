package org.jfo.swaggerhub.swhreporter.service;

import org.assertj.core.api.Assertions;
import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.mappers.OASExtractor;
import org.jfo.swaggerhub.swhreporter.mappers.SwhMapper;
import org.jfo.swaggerhub.swhreporter.model.db.Admin;
import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.jfo.swaggerhub.swhreporter.repository.AdminRepository;
import org.jfo.swaggerhub.swhreporter.repository.ProjectRepository;
import org.jfo.swaggerhub.swhreporter.repository.SpecificationRepository;
import org.jfo.swaggerhub.swhreporter.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("When InitializerService is used")
class InitializerServiceTest {

    private final AdminRepository adminRepository = mock(AdminRepository.class);
    private final SpecificationRepository specificationReactiveRepository = mock(SpecificationRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ProjectRepository projectRepository = mock(ProjectRepository.class);

    private final ModelMapper modelMapper = new ModelMapper(new OASExtractor());
    private final AdminService adminService = new AdminService(adminRepository, modelMapper);
    private final SwaggerHubService swaggerHubService = new SwaggerHubServiceImpl(adminService, new SwhWebClient());
    private final SwhMapper swhMapper = new SwhMapper();

    private final InitializerService initializerService = new InitializerService(
            specificationReactiveRepository,
            userRepository,
            projectRepository,
            swaggerHubService,
            adminService,
            swhMapper
    );

    @BeforeEach
    public void setup() {
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

        when(adminRepository.findByUser(any())).thenReturn(adminDummy);
        doNothing().when(specificationReactiveRepository).saveAll(anyList());
        when(specificationReactiveRepository.count()).thenReturn(mono);

        Long response = initializerService.initAllOwnedSpecs();

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