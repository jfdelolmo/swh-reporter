package org.jfo.swaggerhub.swhreporter.service;

import org.jfo.swaggerhub.swhreporter.client.SwhWebClient;
import org.jfo.swaggerhub.swhreporter.dto.CollaborationDto;
import org.jfo.swaggerhub.swhreporter.dto.SpecsDto;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJson;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SwaggerHubService {

    private final SwhWebClient webClient;
    private final ModelMapper mapper;

    public SwaggerHubService(SwhWebClient webClient, ModelMapper mapper) {
        this.webClient = webClient;
        this.mapper = mapper;
    }
    
    //"CREALOGIX"
    public List<ApisJsonApi> getAllOwnerApis(String owner){
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
        
        while (pendingToDownload){
            Mono<ApisJson> result = webClient.executeCall(SwhWebClient.GET_APIS_BY_OWNER, uriParams, queryParams, ApisJson.class);
            
            ApisJson apisJson = result.blockOptional().orElse(new ApisJson());
            allApis.addAll(apisJson.getApis());
            pendingToDownload = apisJson.getTotalCount() > allApis.size();
            queryParams.get("page").set(0,"" + ++page);
        }

        return allApis;
    }
    
    public List<ApisJsonApi> getAllOwnerSpecs(String owner){
        int page = 0;
        boolean pendingToDownload = true;

        List<ApisJsonApi> allSpecs = new ArrayList<>();

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("owner", owner);
        queryParams.add("specType", "ANY");
        queryParams.add("visibility", "ANY");
        queryParams.add("state", "ALL");
        queryParams.add("page", "" + page);
        queryParams.add("limit", "25");
        queryParams.add("sort", "NAME");
        queryParams.add("order", "ASC");
        
        while(pendingToDownload){
            Mono<ApisJson> result = webClient.executeCall(SwhWebClient.GET_SPECS_URL, null, queryParams, ApisJson.class);

            ApisJson apisJson = result.blockOptional().orElse(new ApisJson());
            allSpecs.addAll(apisJson.getApis());
            pendingToDownload = apisJson.getTotalCount() > allSpecs.size();
            queryParams.get("page").set(0,"" + ++page);
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


        Mono<ApisJson> result = webClient.executeCall(SwhWebClient.GET_SPECS_URL, null, queryParams, ApisJson.class);
        ApisJson apisJson = result.blockOptional().orElse(new ApisJson());

        return mapper.apisJsonToSpecDto(apisJson);
    }

    public String getApiVersion(String apiName, String version){
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("owner", "CREALOGIX");
        uriParams.put("apiName", apiName);
        uriParams.put("version", version);

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("resolved", "true");
        queryParams.add("flatten", "true");

        Mono<String> result = webClient.executeCall(SwhWebClient.GET_API_VERSION_URL, uriParams, queryParams, String.class);
        
        return result.block();
    }

    public CollaborationDto getCollaboration(String apiName){
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("owner", "CREALOGIX");
        uriParams.put("api", apiName);
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("expandTeams", "true");
        Mono<Collaboration> result = webClient.executeCall(SwhWebClient.GET_API_COLLABORATION_URL, uriParams, queryParams, Collaboration.class);
        Collaboration collaboration = result.block();
        return mapper.collaborationToCollaborationDto(collaboration);
    }

}
