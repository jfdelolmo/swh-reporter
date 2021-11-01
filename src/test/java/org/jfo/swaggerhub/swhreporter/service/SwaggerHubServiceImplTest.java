package org.jfo.swaggerhub.swhreporter.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.mappers.OASExtractor;
import org.jfo.swaggerhub.swhreporter.model.db.Admin;
import org.jfo.swaggerhub.swhreporter.model.swh.*;
import org.jfo.swaggerhub.swhreporter.model.swh.users.GetOrganizationMembersResult;
import org.jfo.swaggerhub.swhreporter.model.swh.users.OrganizationMember;
import org.jfo.swaggerhub.swhreporter.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class SwaggerHubServiceImplTest {

    private static final String COLLABORATION_URL = "https://api.swaggerhub.com/apis/CREALOGIX/Test111/0.0.1";
    private static final String PROJECT_NAME = "Monitoring";
    public static final String OWNER = "CREALOGIX";
    public static final String APIKEY = "APIKEY";

    private final AdminRepository adminRepository = mock(AdminRepository.class);
    private final ModelMapper modelMapper = new ModelMapper(new OASExtractor());
    private final AdminService adminService = new AdminService(adminRepository, modelMapper);

    private final SwhWebClient swhWebClient = new SwhWebClient();
    private final SwaggerHubService service = new SwaggerHubServiceImpl(adminService, swhWebClient);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(SwaggerHubServiceImplTest.class);
    }

    @Test
    @Disabled("Avoid call to SwaggerHub")
    void getAllOwnerSpecs() {
        Flux<ApisJson> flux = service.getAllOwnerSpecs(OWNER);
        List<ApisJsonApi> all = new ArrayList<>();
        flux.map(ApisJson::getApis).all(all::addAll).block();

//    flux.map(apisJson -> all.addAll(apisJson.getApis()))
//        .collectList().block();

        Assertions.assertThat(all).isNotEmpty();
        Assertions.assertThat(all.size()).isEqualTo(148);
    }

    @Test
    @Disabled("Avoid call to SwaggerHub")
    void getSpecVersionByUrl() {
        Mono<String> mono = service.getApiVersionByUrl(COLLABORATION_URL, true);
        Assertions.assertThat(mono).isNotNull();
        Assertions.assertThat(mono.block()).isNotNull().isNotEmpty();
    }


    @Test
    @Disabled("Avoid call to SwaggerHub")
    void getCollaboration() {
//    Mono<Collaboration> mono = rxService.getCollaboration(OWNER, COLLABORATION_URL);
        Mono<Collaboration> mono = service.getCollaboration(OWNER, "https://api.swaggerhub.com/apis/CREALOGIX/aso-admin_api/2.0.7");
        Collaboration collaboration = mono.block();

        Assertions.assertThat(mono).isNotNull();
        Assertions.assertThat(collaboration).isNotNull();
//    Assertions.assertThat(collaboration.getMembers()).isEmpty();
//    Assertions.assertThat(collaboration.getTeams()).hasSize(1);
    }

    @Test
    @Disabled("Avoid calls to SwaggerHub")
    void getProjects() {
        List<Project> projects = new ArrayList<>();
        Flux<ProjectsJson> flux = service.getProjects(OWNER);
        flux.map(p -> projects.addAll(p.getProjects()))
                .collectList()
                .block();

        Assertions.assertThat(projects).isNotEmpty();
        Assertions.assertThat(projects.size()).isEqualTo(24);
    }

    @Test
    @Disabled("Avoid calls to SwaggerHub")
    void getProjectMembers() {
        Flux<ProjectMember> flux = service.getProjectMembers(OWNER, PROJECT_NAME);

        List<ProjectMember> all = flux.collectList().block();

        Assertions.assertThat(all).isNotNull().isNotEmpty();
        Assertions.assertThat(all.size()).isEqualTo(2);
    }

    @Test
    void fluxTest() {
        Flux<String> flux = Flux.just("red", "white", "blue");
        Logger logger = LoggerFactory.getLogger("fluxTest");
        Disposable a = flux.log()
                .map(String::toUpperCase)
                .subscribeOn(Schedulers.newParallel("sub"))
                .publishOn(Schedulers.newParallel("pub"), 2)
                .subscribe(value -> logger.info("Consumed: " + value));

        Assertions.assertThat(a).isNotNull();
//    Assertions.assertThat(a.isDisposed()).isTrue();
    }

    @Test
    @Disabled("Avoid call to SwaggerHub")
    void getAllOwnerMembers() {
        Mono<Admin> monoMe = me();
        when(adminRepository.findByUser(any())).thenReturn(monoMe);

        Flux<GetOrganizationMembersResult> flux = service.getAllOwnerMembers(OWNER);
        List<OrganizationMember> all = new ArrayList<>();
        flux.map(GetOrganizationMembersResult::getItems).all(all::addAll).block();

        Assertions.assertThat(all).isNotEmpty();
    }

    private Mono<Admin> me() {
        Admin me = new Admin();
        me.setOwner(OWNER);
        me.setApikey(APIKEY);
        return Mono.just(me);
    }

}