package org.jfo.swaggerhub.swhreporter.service;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJson;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import org.jfo.swaggerhub.swhreporter.model.swh.Project;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectMember;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectsJson;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
class RxSwaggerHubServiceImplTest {

  private static final String COLLABORATION_URL = "https://api.swaggerhub.com/apis/CREALOGIX/Test111/0.0.1";
  private static final String PROJECT_NAME = "Monitoring";
  public static final String OWNER = "CREALOGIX";

  private final SwhWebClient swhWebClient = new SwhWebClient();
  private final RxSwaggerHubService rxService = new RxSwaggerHubServiceImpl(swhWebClient);

  @Test
  @Disabled("Avoid call to SwaggerHub")
  void getAllOwnerSpecs() {
    Flux<ApisJson> flux = rxService.getAllOwnerSpecs(OWNER);
    List<ApisJsonApi> all = new ArrayList<>();
    flux.map(ApisJson::getApis).all(all::addAll).block();

//    flux.map(apisJson -> all.addAll(apisJson.getApis()))
//        .collectList().block();

    Assertions.assertThat(all).isNotEmpty();
    Assertions.assertThat(all.size()).isEqualTo(148);
  }

  @Test
  @Disabled("Avoid call to SwaggerHub")
  void getSpecVersionByUrl(){
    Mono<String> mono = rxService.getApiVersionByUrl(COLLABORATION_URL, true);
    Assertions.assertThat(mono).isNotNull();
    Assertions.assertThat(mono.block()).isNotNull().isNotEmpty();
  }
  

  @Test
  @Disabled("Avoid call to SwaggerHub")
  void getCollaboration() {
    Mono<Collaboration> mono = rxService.getCollaboration(OWNER, COLLABORATION_URL);
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
    Flux<ProjectsJson> flux = rxService.getProjects(OWNER);
    flux.map(p -> projects.addAll(p.getProjects()))
        .collectList()
        .block();

    Assertions.assertThat(projects).isNotEmpty();
    Assertions.assertThat(projects.size()).isEqualTo(24);
  }

  @Test
  @Disabled("Avoid calls to SwaggerHub")
  void getProjectMembers() {
    Flux<ProjectMember> flux = rxService.getProjectMembers(OWNER, PROJECT_NAME);

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
}