package org.jfo.swaggerhub.swhreporter.service;

import static org.jfo.swaggerhub.swhreporter.client.SwhClientParams.*;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.API_AS_YAML;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.DOMAIN_AS_YAML;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.GET_PROJECTS_BY_OWNER;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.GET_PROJECT_MEMBERS;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.USER_MANAGEMENT_MEMBERS_URL;
import static org.jfo.swaggerhub.swhreporter.model.CommonConcepts.ERROR_RETRIEVE_FROM_SWAGGER_HUB;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.tuple.Pair;
import org.jfo.swaggerhub.swhreporter.client.SwhClientParams;
import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.dto.MyAdminDto;
import org.jfo.swaggerhub.swhreporter.exception.MyAdminApiKeyNotFound;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJson;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectMember;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectMembersList;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectsJson;
import org.jfo.swaggerhub.swhreporter.model.swh.users.GetOrganizationMembersResult;
import org.jfo.swaggerhub.swhreporter.model.swh.users.OrganizationMemberResult;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SwaggerHubServiceImpl implements SwaggerHubService {

  private final AdminService adminService;
  private final SwhWebClient swhWebClient;

  private void configSwhWebClientApiKey(){
    String adminApiKey = Optional.ofNullable(adminService.getMyAdmin())
        .map(MyAdminDto::getApikey)
        .orElseThrow(MyAdminApiKeyNotFound::new);
    
    if (null==swhWebClient.getApiKey()) {
      swhWebClient.setApiKey(adminApiKey);
    }
  }
  
  @Override
  public Flux<ApisJson> getAllOwnerSpecs(String owner) {
    AtomicInteger pageCounter = new AtomicInteger(0);
    configSwhWebClientApiKey();
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
    Map<String, String> uriParams = buildGetCollaborationUriParams(owner, url);
    MultiValueMap<String, String> queryParams = buildGetCollaborationQueryParams();

    configSwhWebClientApiKey();
    return swhWebClient.executeCallMono(SwhWebClient.GET_API_COLLABORATION_URL, uriParams, queryParams, Collaboration.class);
  }

  @Override
  public Mono<String> getApiVersionByUrl(String url, boolean resolved) {
    MultiValueMap<String, String> queryParams = SwhClientParams.buildGetSpecVersionByUrlQueryParams(resolved);
    configSwhWebClientApiKey();
    return swhWebClient.executeCallMono(
        url.contains("/apis/") ? url + API_AS_YAML : url + DOMAIN_AS_YAML,
        null,
        queryParams,
        String.class);
  }

  @Override
  public Flux<ProjectsJson> getProjects(String owner) {
    AtomicInteger pageCounter = new AtomicInteger(0);
    configSwhWebClientApiKey();
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
            buildGetProjectsUriParams(owner),
            buildGetProjectsQueryParams(page),
            ProjectsJson.class);
  }

  @Override
  public Flux<ProjectMember> getProjectMembers(String owner, String project) {
    configSwhWebClientApiKey();
    Mono<ProjectMembersList> mono = swhWebClient.executeCallMono(
        GET_PROJECT_MEMBERS,
        buildGetProjectMembersUriParams(owner, project),
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
      log.error("Error on resolving api from {} :: {}", url, e.getMessage());
      resolvedApi = ERROR_RETRIEVE_FROM_SWAGGER_HUB;
    }
    unresolvedApi = getApiVersionByUrl(url, false).block();

    return Mono.just(Pair.of(resolvedApi,unresolvedApi));
  }

  @Override
  public Flux<GetOrganizationMembersResult> getAllOwnerMembers(String owner) {
    AtomicInteger pageCounter = new AtomicInteger(0);
    configSwhWebClientApiKey();

    return getAllOwnerMembersPaged(owner, 0)
            .expand(r -> {
              if (r.getTotalCount() > (r.getPage()+1)*PAGE_SIZE_NUM) {
                return getAllOwnerMembersPaged(owner, pageCounter.incrementAndGet());
              }
              return Flux.empty();
            });
  }

  private Flux<GetOrganizationMembersResult> getAllOwnerMembersPaged(String owner, int page) {
    Map<String, String> uriParams = buildGetAllOwnerMembersUriParams(owner);
    MultiValueMap<String, String> queryParams = buildAllOwnerMembersParams(page);
    return swhWebClient.executeCallFlux(USER_MANAGEMENT_MEMBERS_URL, uriParams, queryParams, GetOrganizationMembersResult.class);
  }

  public Flux<OrganizationMemberResult> deleteMember(String owner, String... userEmail) {
    Map<String, String> uriParams = buildDeleteMemberUriParams(owner);
    MultiValueMap<String, String> queryParams = buildDeleteMemberParams(userEmail);
    configSwhWebClientApiKey();
    return swhWebClient.executeCallFluxDelete(USER_MANAGEMENT_MEMBERS_URL, uriParams, queryParams, OrganizationMemberResult.class);
  }

}
