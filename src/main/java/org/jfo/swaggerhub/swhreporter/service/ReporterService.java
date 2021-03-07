package org.jfo.swaggerhub.swhreporter.service;

import lombok.extern.slf4j.Slf4j;
import org.jfo.swaggerhub.swhreporter.dto.ApiDto;
import org.jfo.swaggerhub.swhreporter.dto.CollaborationDto;
import org.jfo.swaggerhub.swhreporter.dto.SpecDto;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

@Slf4j
@Service
public class ReporterService {

    SwaggerHubService swaggerHubService;

    public ReporterService(SwaggerHubService swaggerHubService) {
        this.swaggerHubService = swaggerHubService;
    }

    public SpecDto getSpecs() {
        log.info("Entering service getApis method");

        SpecDto specs = swaggerHubService.getSpecs();

        return specs;
    }

    public ApiDto getApiDetails(String apiName, String version){
        log.info("Entering service getDefaultApi for {}", apiName);
        String document = swaggerHubService.getApiVersion(apiName, version);
        ApiDto apiDto = new ApiDto();
        apiDto.setName(apiName);
        apiDto.setVersion(version);
        apiDto.setDocument(document);
        CollaborationDto collaborationDto = swaggerHubService.getCollaboration(apiName);
        apiDto.setCollaboration(collaborationDto);
        return apiDto;
    }

}
