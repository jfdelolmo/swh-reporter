package org.jfo.swaggerhub.swhreporter.service;

import static org.jfo.swaggerhub.swhreporter.client.SwhClientParams.OWNER_PARAM;
import static org.jfo.swaggerhub.swhreporter.client.SwhClientParams.PAGE_PARAM;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.API_AS_YAML;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.DOMAIN_AS_YAML;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.GET_PROJECTS_BY_OWNER;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.GET_PROJECT_MEMBERS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.jfo.swaggerhub.swhreporter.client.SwhClientParams;
import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJson;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectMember;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectMembersList;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectsJson;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Service responsible to do the required calls SwggerHub.
 * It will handle basic objects or the ones defined by SwaggerHub.
 * It will not use DB models -> Use the SwhMapper to convert SwhModels to DbModels.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SwaggerHubServiceImpl {

  private final AdminService adminService;
  private final SwhWebClient webClient;

  public Set<ApisJsonApi> getAllOwnerSpecs(String owner) {
    int page = 0;
    boolean pendingToDownload = true;

    Set<ApisJsonApi> allSpecs = new HashSet<>();

    MultiValueMap<String, String> queryParams = SwhClientParams.buildAllOwnerSpecsParams(owner, page);

    while (pendingToDownload) {
      
      Mono<ApisJson> result = webClient.executeCallMono(SwhWebClient.GET_SPECS_URL, null, queryParams, ApisJson.class);

      ApisJson apisJson = result.blockOptional().orElse(new ApisJson());
      allSpecs.addAll(apisJson.getApis());
      pendingToDownload = apisJson.getTotalCount() > allSpecs.size();
      queryParams.get(PAGE_PARAM).set(0, "" + ++page);
    }

    return allSpecs;
  }

  public String getApiNameFromUrl(String url, String owner) {
    String base = SwhWebClient.BASE_URL + "/apis/" + owner + "/";
    String removed = StringUtils.remove(url, base);
    return removed.substring(0, removed.indexOf("/"));
  }

  public String getSpecVersionByUrl(String url, boolean resolved) {
    MultiValueMap<String, String> queryParams = SwhClientParams.buildGetSpecVersionByUrlQueryParams(resolved);

    Mono<String> result = webClient.executeCallMono(
        url.contains("/apis/") ? url + API_AS_YAML : url + DOMAIN_AS_YAML,
        null,
        queryParams,
        String.class);
    return result.block();
  }

  public Collaboration getCollaboration(String url) {
    Map<String, String> uriParams = new HashMap<>();
    String owner = adminService.getUserOwner();
    uriParams.put(OWNER_PARAM, owner);
    uriParams.put("api", getApiNameFromUrl(url, owner));
    MultiValueMap<String, String> queryParams = SwhClientParams.buildGetCollaborationQueryParams();
    
    Mono<Collaboration> result = webClient.executeCallMono(SwhWebClient.GET_API_COLLABORATION_URL, uriParams, queryParams, Collaboration.class);
    return result.block();
  }

  public ProjectsJson getProjects(String owner) {
    
    boolean pendingToDownload = true;
    ProjectsJson accumulated = new ProjectsJson();
    accumulated.setProjects(new ArrayList<>());

    Map<String, String> uriParams = new HashMap<>();
    uriParams.put(OWNER_PARAM, owner);

    AtomicInteger page = new AtomicInteger(0);
  
    while (pendingToDownload) {
      Mono<ProjectsJson> result = webClient
          .executeCallMono(GET_PROJECTS_BY_OWNER, 
              uriParams, 
              SwhClientParams.buildGetProjectsQueryParams(page.getAndIncrement()),
              ProjectsJson.class);

      ProjectsJson temp = result.blockOptional().orElse(new ProjectsJson());
      accumulated.setOffset(temp.getOffset());
      accumulated.setTotalCount(temp.getTotalCount());
      accumulated.getProjects().addAll(temp.getProjects());

      pendingToDownload = accumulated.getTotalCount() > accumulated.getProjects().size();
    }

    return accumulated;
  }

  public Set<ProjectMember> getProjectMembers(String owner, String project) {
    Map<String, String> uriParams = new HashMap<>();
    uriParams.put(OWNER_PARAM, owner);
    uriParams.put("projectId", project);

    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
    Mono<ProjectMembersList> result = webClient.executeCallMono(GET_PROJECT_MEMBERS, uriParams, queryParams, ProjectMembersList.class);

    Optional<ProjectMembersList> optionalBlock = result.blockOptional();

    if (optionalBlock.isEmpty() || optionalBlock.get().getMembers().isEmpty()) {
      return new HashSet<>();
    } else {
      return new HashSet<>(optionalBlock.get().getMembers());
    }
  }

}
