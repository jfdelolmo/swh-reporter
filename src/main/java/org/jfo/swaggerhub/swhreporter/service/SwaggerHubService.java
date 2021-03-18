package org.jfo.swaggerhub.swhreporter.service;

import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.API_AS_YAML;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.BASE_URL;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.DOMAIN_AS_YAML;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.GET_APIS_BY_OWNER;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.GET_API_COLLABORATION_URL;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.GET_API_VERSION_URL;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.GET_PROJECTS_BY_OWNER;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.GET_PROJECT_MEMBERS;
import static org.jfo.swaggerhub.swhreporter.client.SwhWebClient.GET_SPECS_URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.dto.SpecsDto;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJson;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectMember;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectMembersList;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectsJson;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import reactor.core.publisher.Mono;

/**
 * Service responsible to do the required calls SwggerHub.
 * It will handle basic objects or the ones defined by SwaggerHub.
 * It will not use DB models -> Use the SwhMapper to convert SwhModels to DbModels.
 */
@Service
public class SwaggerHubService {

    private static final String OWNER_PARAM = "owner";
    private static final String LIMIT_PARAM = "limit";
    private static final String ORDER_PARAM = "order";
    
    private final AdminService adminService;
    private final SwhWebClient webClient;
    private final ModelMapper mapper;
    
    public SwaggerHubService(AdminService adminService,
                             SwhWebClient webClient,
                             ModelMapper mapper) {
        this.adminService = adminService;
        this.webClient = webClient;
        this.mapper = mapper;
    }

    public List<ApisJsonApi> getAllOwnerApis(String owner) {
        int page = 0;
        boolean pendingToDownload = true;
        List<ApisJsonApi> allApis = new ArrayList<>();

        Map<String, String> uriParams = new HashMap<>();
        uriParams.put(OWNER_PARAM, owner);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("page", "0");
        queryParams.add(LIMIT_PARAM, "50");
        queryParams.add("sort", "NAME");
        queryParams.add(ORDER_PARAM, "ASC");

        while (pendingToDownload) {
            Mono<ApisJson> result = webClient.executeCall(GET_APIS_BY_OWNER, uriParams, queryParams, ApisJson.class);

            ApisJson apisJson = result.blockOptional().orElse(new ApisJson());
            allApis.addAll(apisJson.getApis());
            pendingToDownload = apisJson.getTotalCount() > allApis.size();
            queryParams.get("page").set(0, "" + ++page);
        }

        return allApis;
    }

    public List<ApisJsonApi> getAllOwnerSpecs(String owner) {
        int page = 0;
        boolean pendingToDownload = true;

        List<ApisJsonApi> allSpecs = new ArrayList<>();

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(OWNER_PARAM, owner);
        queryParams.add("specType", "ANY");
        queryParams.add("visibility", "ANY");
        queryParams.add("state", "ALL");
        queryParams.add("page", "" + page);
        queryParams.add(LIMIT_PARAM, "25");
        queryParams.add("sort", "NAME");
        queryParams.add(ORDER_PARAM, "ASC");

        while (pendingToDownload) {
            Mono<ApisJson> result = webClient.executeCall(GET_SPECS_URL, null, queryParams, ApisJson.class);

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
        queryParams.add(OWNER_PARAM, adminService.getUserOwner());
        queryParams.add("specType", "ANY");
        queryParams.add("visibility", "ANY");
        queryParams.add("state", "ALL");
        queryParams.add("page", "0");
        queryParams.add(LIMIT_PARAM, "25");
        queryParams.add("sort", "NAME");
        queryParams.add(ORDER_PARAM, "ASC");


        Mono<ApisJson> result = webClient.executeCall(GET_SPECS_URL, null, queryParams, ApisJson.class);
        ApisJson apisJson = result.blockOptional().orElse(new ApisJson());

        return mapper.apisJsonToSpecDto(apisJson);
    }

    public String getApiVersion(String apiName, String version) {
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put(OWNER_PARAM, adminService.getUserOwner());
        uriParams.put("apiName", apiName);
        uriParams.put("version", version);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("resolved", "true");
        queryParams.add("flatten", "true");

        Mono<String> result = webClient.executeCall(GET_API_VERSION_URL, uriParams, queryParams, String.class);

        return result.block();
    }

    public Collaboration getCollaboration(String url) {
        Map<String, String> uriParams = new HashMap<>();
        String owner = adminService.getUserOwner();
        uriParams.put(OWNER_PARAM, owner);
        uriParams.put("api", getApiNameFromUrl(url, owner));
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("expandTeams", "true");
        Mono<Collaboration> result = webClient.executeCall(GET_API_COLLABORATION_URL, uriParams, queryParams, Collaboration.class);
        return result.block();
    }

    public String getApiNameFromUrl(String url, String owner) {
        String base = BASE_URL + "/apis/" + owner + "/";
        String removed = StringUtils.remove(url, base);
        return removed.substring(0, removed.indexOf("/"));
    }

    public String getApiVersionByUrl(String url) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("resolved", "true");
        queryParams.add("flatten", "true");
        String specType = url.contains("/domains")? DOMAIN_AS_YAML : API_AS_YAML;
        Mono<String> result = webClient.executeCall(url + specType, null, queryParams, String.class);
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
            Mono<ProjectsJson> result = webClient.executeCall(GET_PROJECTS_BY_OWNER, uriParams, queryParams, ProjectsJson.class);

            ProjectsJson temp = result.blockOptional().orElse(new ProjectsJson());
            accumulated.setOffset(temp.getOffset());
            accumulated.setTotalCount(temp.getTotalCount());
            accumulated.getProjects().addAll(temp.getProjects());
            
            pendingToDownload = accumulated.getTotalCount() > accumulated.getProjects().size();
            queryParams.get("page").set(0, "" + ++page);
        }
        
        return accumulated;
    }
    
    public List<ProjectMember> getProjectMembers(String owner, String project){
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put(OWNER_PARAM, owner);
        uriParams.put("projectId", project);
        
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        Mono<ProjectMembersList> result = webClient.executeCall(GET_PROJECT_MEMBERS, uriParams, queryParams, ProjectMembersList.class);

        Optional<ProjectMembersList> optionalBlock = result.blockOptional();
       
        if (optionalBlock.isEmpty() || optionalBlock.get().getMembers().isEmpty()){
            return new ArrayList<>();
        }else{
            return optionalBlock.get().getMembers();
        }
    }
}

