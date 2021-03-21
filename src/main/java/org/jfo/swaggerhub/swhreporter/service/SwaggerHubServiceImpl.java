package org.jfo.swaggerhub.swhreporter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.dto.SpecsDto;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.model.swh.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.GET_PROJECTS_BY_OWNER;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.GET_PROJECT_MEMBERS;

/**
 * Service responsible to do the required calls SwggerHub.
 * It will handle basic objects or the ones defined by SwaggerHub.
 * It will not use DB models -> Use the SwhMapper to convert SwhModels to DbModels.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SwaggerHubServiceImpl {

    private static final String OWNER_PARAM = "owner";
    private static final String LIMIT_PARAM = "limit" ;
    private static final String ORDER_PARAM = "order";

    private final AdminService adminService;
    private final SwhWebClient webClient;
    private final ModelMapper mapper;

    public List<ApisJsonApi> getAllOwnerApis(String owner) {
        int page = 0;
        boolean pendingToDownload = true;
        List<ApisJsonApi> allApis = new ArrayList<>();

        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("owner", owner);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", "0");
        queryParams.add("limit", "50");
        queryParams.add("sort", "NAME");
        queryParams.add("order", "ASC");

        while (pendingToDownload) {
            Mono<ApisJson> result = webClient.executeCallMono(SwhWebClient.GET_APIS_BY_OWNER, uriParams, queryParams, ApisJson.class);

            ApisJson apisJson = result.blockOptional().orElse(new ApisJson());
            allApis.addAll(apisJson.getApis());
            pendingToDownload = apisJson.getTotalCount() > allApis.size();
            queryParams.get("page").set(0, "" + ++page);
        }

        return allApis;
    }

    public Set<ApisJsonApi> getAllOwnerSpecs(String owner) {
        int page = 0;
        boolean pendingToDownload = true;

        Set<ApisJsonApi> allSpecs = new HashSet<>();

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("owner", owner);
        queryParams.add("specType", "ANY");
        queryParams.add("visibility", "ANY");
        queryParams.add("state", "ALL");
        queryParams.add("page", "" + page);
        queryParams.add("limit", "25");
        queryParams.add("sort", "NAME");
        queryParams.add("order", "ASC");

        while (pendingToDownload) {
            Mono<ApisJson> result = webClient.executeCallMono(SwhWebClient.GET_SPECS_URL, null, queryParams, ApisJson.class);

            ApisJson apisJson = result.blockOptional().orElse(new ApisJson());
            allSpecs.addAll(apisJson.getApis());
            pendingToDownload = apisJson.getTotalCount() > allSpecs.size();
            queryParams.get("page").set(0, "" + ++page);
        }

        return allSpecs;
    }

    /*
      - type: Swagger
                url: 'https://api.swaggerhub.com/apis/username/petstore/1.1'
              - type: X-Version
                value: '1.1'
              - type: X-Created
                value: '2017-01-16T13:39:06Z'
              - type: X-Modified
                value: '2017-01-25T11:48:13Z'
              - type: X-Published
                value: 'true'
              - type: X-Versions
                value: '1.0,*1.1,1.1-oas3'
              - type: X-Private
                value: 'false'
              - type: X-OASVersion
                value: '3.0.0'
              - type: X-Notifications
                value: 'true'
              - type: X-CreatedBy
                value: 'username'
                X-Domain
     */
    public SpecsDto getSpecs() {

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("owner", "CREALOGIX");
        queryParams.add("specType", "ANY");
        queryParams.add("visibility", "ANY");
        queryParams.add("state", "ALL");
        queryParams.add("page", "0");
        queryParams.add("limit", "25");
        queryParams.add("sort", "NAME");
        queryParams.add("order", "ASC");


        Mono<ApisJson> result = webClient.executeCallMono(SwhWebClient.GET_SPECS_URL, null, queryParams, ApisJson.class);
        ApisJson apisJson = result.blockOptional().orElse(new ApisJson());

        return mapper.apisJsonToSpecDto(apisJson);
    }

    public String getApiVersion(String apiName, String version) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("owner", "CREALOGIX");
        uriParams.put("apiName", apiName);
        uriParams.put("version", version);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("resolved", "true");
        queryParams.add("flatten", "true");

        Mono<String> result = webClient.executeCallMono(SwhWebClient.GET_API_VERSION_URL, uriParams, queryParams, String.class);

        return result.block();
    }

    public Collaboration getCollaboration(String url) {
        Map<String, String> uriParams = new HashMap<>();
        String owner = adminService.getUserOwner();
        uriParams.put("owner", owner);
        uriParams.put("api", getApiNameFromUrl(url, owner));
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("expandTeams", "true");
        Mono<Collaboration> result = webClient.executeCallMono(SwhWebClient.GET_API_COLLABORATION_URL, uriParams, queryParams, Collaboration.class);
        return result.block();
    }

    public String getApiNameFromUrl(String url, String owner) {
        String base = SwhWebClient.BASE_URL + "/apis/" + owner + "/";
        String removed = StringUtils.remove(url, base);
        return removed.substring(0, removed.indexOf("/"));
    }

    public String getApiVersionByUrl(String url) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("resolved", "true");
        queryParams.add("flatten", "true");
        Mono<String> result = webClient.executeCallMono(url, null, queryParams, String.class);
        return result.block();
    }

    public ProjectsJson getProjects(String owner){
        int page = 0;
        boolean pendingToDownload = true;
        ProjectsJson accumulated = new ProjectsJson();
        accumulated.setProjects(new ArrayList<>());

        Map<String, String> uriParams = new HashMap<>();
        uriParams.put(OWNER_PARAM, owner);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("nameOnly", "false");
        queryParams.add("page", "0");
        queryParams.add(LIMIT_PARAM, "50");
        queryParams.add(ORDER_PARAM, "ASC");

        while (pendingToDownload) {
            Mono<ProjectsJson> result = webClient.executeCallMono(GET_PROJECTS_BY_OWNER, uriParams, queryParams, ProjectsJson.class);

            ProjectsJson temp = result.blockOptional().orElse(new ProjectsJson());
            accumulated.setOffset(temp.getOffset());
            accumulated.setTotalCount(temp.getTotalCount());
            accumulated.getProjects().addAll(temp.getProjects());

            pendingToDownload = accumulated.getTotalCount() > accumulated.getProjects().size();
            queryParams.get("page").set(0, "" + ++page);
        }

        return accumulated;
    }

    public Set<ProjectMember> getProjectMembers(String owner, String project){
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put(OWNER_PARAM, owner);
        uriParams.put("projectId", project);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        Mono<ProjectMembersList> result = webClient.executeCallMono(GET_PROJECT_MEMBERS, uriParams, queryParams, ProjectMembersList.class);

        Optional<ProjectMembersList> optionalBlock = result.blockOptional();

        if (optionalBlock.isEmpty() || optionalBlock.get().getMembers().isEmpty()){
            return new HashSet<>();
        }else{
            return new HashSet<>(optionalBlock.get().getMembers());
        }
    }

}
