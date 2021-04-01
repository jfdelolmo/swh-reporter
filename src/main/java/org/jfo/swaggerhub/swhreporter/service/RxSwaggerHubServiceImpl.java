package org.jfo.swaggerhub.swhreporter.service;

import static org.jfo.swaggerhub.swhreporter.client.SwhClientParams.buildAllOwnerSpecsParams;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.API_AS_YAML;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.DOMAIN_AS_YAML;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.GET_PROJECTS_BY_OWNER;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.GET_PROJECT_MEMBERS;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.tuple.Pair;
import org.jfo.swaggerhub.swhreporter.client.SwhClientParams;
import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJson;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectMember;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectMembersList;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectsJson;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RxSwaggerHubServiceImpl implements RxSwaggerHubService {

  private final SwhWebClient swhWebClient;

  @Override
  public Flux<ApisJson> getAllOwnerSpecs(String owner) {
    AtomicInteger pageCounter = new AtomicInteger(0);
    return getAllOwnerSpecsPaged(owner, 0)
        .expand(r -> {
          if (r.getTotalCount() > r.getOffset() + r.getApis().size()) {
            return getAllOwnerSpecsPaged(owner, pageCounter.incrementAndGet());
          }
          return Flux.empty();
        });
  }

  private Flux<ApisJson> getAllOwnerSpecsPaged(String owner, int page) {
    MultiValueMap<String, String> queryParams = buildAllOwnerSpecsParams(owner, page);
    return swhWebClient.executeCallFlux(SwhWebClient.GET_SPECS_URL, null, queryParams, ApisJson.class);
  }

  @Override
  public Mono<Collaboration> getCollaboration(String owner, String url) {
    Map<String, String> uriParams = SwhClientParams.buildGetCollaborationUriParams(owner, url);
    MultiValueMap<String, String> queryParams = SwhClientParams.buildGetCollaborationQueryParams();

    return swhWebClient.executeCallMono(SwhWebClient.GET_API_COLLABORATION_URL, uriParams, queryParams, Collaboration.class);
  }

  @Override
  public Mono<String> getApiVersionByUrl(String url, boolean resolved) {
    MultiValueMap<String, String> queryParams = SwhClientParams.buildGetSpecVersionByUrlQueryParams(resolved);

    return swhWebClient.executeCallMono(
        url.contains("/apis/") ? url + API_AS_YAML : url + DOMAIN_AS_YAML,
        null,
        queryParams,
        String.class);
  }

  @Override
  public Flux<ProjectsJson> getProjects(String owner) {
    AtomicInteger pageCounter = new AtomicInteger(0);
    return getProjectsPaged(owner, 0)
        .expand(r -> {
          if (r.getTotalCount() > r.getOffset() + r.getProjects().size()) {
            return getProjectsPaged(owner, pageCounter.incrementAndGet());
          }
          return Flux.empty();
        });
  }

  private Flux<ProjectsJson> getProjectsPaged(String owner, int page) {
    return swhWebClient
        .executeCallFlux(GET_PROJECTS_BY_OWNER,
            SwhClientParams.buildGetProjectsUriParams(owner),
            SwhClientParams.buildGetProjectsQueryParams(page),
            ProjectsJson.class);
  }

  @Override
  public Flux<ProjectMember> getProjectMembers(String owner, String project) {
    Mono<ProjectMembersList> mono = swhWebClient.executeCallMono(
        GET_PROJECT_MEMBERS,
        SwhClientParams.buildGetProjectMembersUriParams(owner, project),
        null,
        ProjectMembersList.class);

    return mono.map(ProjectMembersList::getMembers).flatMapMany(Flux::fromIterable);
  }

  public Mono<Pair<String, String>> getResolvedUnresolvedSpec(String url){
    String resolvedApi;
    String unresolvedApi;
    try {
      resolvedApi = getApiVersionByUrl(url, true).block();
    } catch (Exception e) {
      resolvedApi = "Error on retrieving the resolved specification from SwaggerHub";
    }
    unresolvedApi = getApiVersionByUrl(url, false).block();

    return Mono.just(Pair.of(resolvedApi,unresolvedApi));
  }
  
  
}
