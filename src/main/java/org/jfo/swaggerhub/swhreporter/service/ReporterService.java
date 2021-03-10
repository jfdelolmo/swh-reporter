package org.jfo.swaggerhub.swhreporter.service;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jfo.swaggerhub.swhreporter.dto.ApiDto;
import org.jfo.swaggerhub.swhreporter.dto.ClxApiOauth2SecurityDefinitionDto;
import org.jfo.swaggerhub.swhreporter.dto.CollaborationDto;
import org.jfo.swaggerhub.swhreporter.dto.SpecsDto;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.jfo.swaggerhub.swhreporter.repository.SpecRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReporterService {

  private final SpecRepository specRepository;
  private final SwaggerHubService swaggerHubService;
  private final ModelMapper modelMapper;

  public ReporterService(SpecRepository specRepository, 
      SwaggerHubService swaggerHubService,
      ModelMapper modelMapper) {
    this.specRepository = specRepository;
    this.swaggerHubService = swaggerHubService;
    this.modelMapper = modelMapper;
  }

  public SpecsDto getSpecs() {
    log.info("Entering service getApis method");
    
    Page<Specification> result = specRepository.findAll(PageRequest.of(0, 20, Sort.by("name").ascending()));
    
    SpecsDto specsDto = new SpecsDto();
    specsDto.setNumberOfSpec(result.getTotalElements());
    
    specsDto.setSpecs(
        result.getContent()
            .stream()
            .map(modelMapper::specModelToSpecPropertiesDto)
            .collect(Collectors.toList())
    );
    
    return specsDto;
    //return swaggerHubService.getSpecs();
  }

  public ApiDto getApiDetails(String apiName, String version) throws Exception {
    log.info("Entering service getDefaultApi for {}", apiName);
    String document = swaggerHubService.getApiVersion(apiName, version);
    ApiDto apiDto = new ApiDto();
    apiDto.setName(apiName);
    apiDto.setVersion(version);
    apiDto.setDocument(document);
    OpenAPI openAPI = parseOpenApi(document);
    apiDto.setSecurityDefinitions(getClxApiOauth2SecurityDefinition(openAPI).orElse(null));
    CollaborationDto collaborationDto = swaggerHubService.getCollaboration(apiName);
    apiDto.setCollaboration(collaborationDto);

    return apiDto;
  }

  private OpenAPI parseOpenApi(String apiAsString) throws Exception {
    OpenAPIV3Parser openAPIV3Parser = new OpenAPIV3Parser();
    ParseOptions options = new ParseOptions();
    options.setResolveFully(true);
    SwaggerParseResult parseResults = openAPIV3Parser.readContents(apiAsString, null, options);
    if (!parseResults.getMessages().isEmpty()) {
      throw new Exception("Parse errors");
    }
    return parseResults.getOpenAPI();
  }

  private Optional<ClxApiOauth2SecurityDefinitionDto> getClxApiOauth2SecurityDefinition(OpenAPI api) {
    ClxApiOauth2SecurityDefinitionDto securities = new ClxApiOauth2SecurityDefinitionDto();
    securities.setScopes(new HashSet<>());
    securities.setRoles(new HashSet<>());
    securities.setAudiences(new HashSet<>());
    try {
      OAuthFlows clxApiOAuth2Flows = api.getComponents()
          .getSecuritySchemes()
          .get("ClxApiOAuth2")
          .getFlows();

      Optional.ofNullable(clxApiOAuth2Flows.getAuthorizationCode())
            .map(OAuthFlow::getScopes)
          .ifPresentOrElse(scopes ->
              securities.getScopes().addAll(scopes.entrySet()
                  .stream()
                  .map(e -> e.getKey() + " - " + e.getValue())
                  .collect(Collectors.toSet())),
              () -> securities.getScopes().add("Scopes not defined properly"));
            
      Optional.ofNullable(clxApiOAuth2Flows.getAuthorizationCode())
          .map(OAuthFlow::getExtensions)
          .ifPresentOrElse(extensions -> extensions.forEach((k, v) -> {
            if ("x-clx-roles".equals(k)) {
              securities.getRoles().addAll(
                  ((LinkedHashMap<String, String>) v).entrySet()
                      .stream()
                      .map(r -> r.getKey() + " - " + r.getValue())
                      .collect(Collectors.toSet())
              );
            } else if ("x-clx-audiences".equals(k)) {
              securities.getAudiences().addAll(
                  ((LinkedHashMap<String, String>) v).entrySet()
                      .stream()
                      .map(r -> r.getKey() + " - " + r.getValue())
                      .collect(Collectors.toSet())
              );
            }
          }),
              () -> {
              securities.getRoles().add("Roles not defined properly");
              securities.getAudiences().add("Audiences not defined properly");
              }    
          );

      return Optional.of(securities);
    } catch (Exception e) {
      log.error("{}", e.getMessage());
    }
    return Optional.empty();
  }


}
