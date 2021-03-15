package org.jfo.swaggerhub.swhreporter.service;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import lombok.extern.slf4j.Slf4j;
import org.jfo.swaggerhub.swhreporter.dto.ApiDto;
import org.jfo.swaggerhub.swhreporter.dto.ClxApiOauth2SecurityDefinitionDto;
import org.jfo.swaggerhub.swhreporter.dto.CollaborationDto;
import org.jfo.swaggerhub.swhreporter.dto.SpecsDto;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.mappers.SwhMapper;
import org.jfo.swaggerhub.swhreporter.model.db.NewCollaboration;
import org.jfo.swaggerhub.swhreporter.model.db.NewOpenApiDocument;
import org.jfo.swaggerhub.swhreporter.model.db.NewSpecification;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import org.jfo.swaggerhub.swhreporter.repository.NewSpecificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReporterService {

    private final InitializerService initializerService;
    private final NewSpecificationRepository specificationRepository;
    private final SwaggerHubService swaggerHubService;
    private final ModelMapper modelMapper;
    private final SwhMapper swhMapper;

    public ReporterService(NewSpecificationRepository specificationRepository,
                           InitializerService initializerService,
                           SwaggerHubService swaggerHubService,
                           ModelMapper modelMapper,
                           SwhMapper swhMapper) {
        this.specificationRepository = specificationRepository;
        this.swaggerHubService = swaggerHubService;
        this.modelMapper = modelMapper;
        this.swhMapper = swhMapper;
        this.initializerService = initializerService;
    }

    public SpecsDto getSpecs() {
        log.info("Entering service getApis method");

        Page<NewSpecification> result = specificationRepository.findAll(PageRequest.of(0, 20, Sort.by("name").ascending()));

        if (result.getTotalElements()==0){
            initializerService.retrieveAllSpecs();
        }

        SpecsDto specsDto = new SpecsDto();
        specsDto.setNumberOfSpec(result.getTotalElements());

        specsDto.setSpecs(
                result.getContent()
                        .stream()
                        .map(modelMapper::specModelToSpecPropertiesDto)
                        .collect(Collectors.toList())
        );

        return specsDto;
    }

    public ApiDto getApiDetails(Long id) throws Exception {
        log.info("Entering service getDefaultApi for {}", id);

        Optional<NewSpecification> optionalSpecification = specificationRepository.findById(id);

        if (optionalSpecification.isEmpty()) {
            throw new RuntimeException(String.format("The id %s is not valid", id));
        }
        NewSpecification specification = optionalSpecification.get();

        //Check if the specification stored in the database has already the api
        //if not call the swhService to retrieve the api
        if (specificationHasNoApi(specification)) {
            specification = retrieveApiAndStoreUpdatedSpecification(specification);
        }

        if (specificationHasNoCollaboration(specification)) {
            specification = retrieveCollaborationAndStoreUpdatedSpecification(specification);
        }

        return buildApiDto(specification);
    }

    private NewSpecification retrieveCollaborationAndStoreUpdatedSpecification(NewSpecification specification) {
        Collaboration collaboration = swaggerHubService.getCollaboration(specification.getProperties().getUrl());
        NewCollaboration collaborationModel = swhMapper.collaborationSwhToModel(collaboration);
        return specificationRepository.save(specification.setCollaboration(collaborationModel));
    }

    private boolean specificationHasNoCollaboration(NewSpecification specification) {
        return (null == specification.getCollaboration());
    }

    private NewSpecification retrieveApiAndStoreUpdatedSpecification(NewSpecification specification) throws Exception {
        String apiAsString = swaggerHubService.getApiVersionByUrl(specification.getProperties().getUrl());
        NewOpenApiDocument openApiDocument = swhMapper.specificationAsStringToOpenApiDocument(apiAsString);
        specification.setOpenApiDocument(openApiDocument);

        return specificationRepository.save(specification);
    }

    private boolean specificationHasNoApi(NewSpecification specification) {
        return Optional.ofNullable(specification.getOpenApiDocument())
                .map(NewOpenApiDocument::getDefinition)
                .orElse("").length() == 0;

//        Clob clob = Optional.of(specification)
//                .map(Specification::getOpenApiDocument)
//                .map(OpenApiDocument::getYaml)
//                .orElse(null);
//
//        try {
//            if (null != clob && clob.length() > 0) {
//                hasNoApi = false;
//            } else {
//                hasNoApi = true;
//            }
//        } catch (Exception e) {
//            hasNoApi = true;
//        }
    }

    private ApiDto buildApiDto(NewSpecification specification) throws Exception {
        ApiDto apiDto = new ApiDto();
        apiDto.setName(specification.getName());
        apiDto.setVersion(specification.getProperties().getVersion());

//        String document = modelMapper.getClobAsString(specification.getOpenApiDocument().getDefinition());
        String document = specification.getOpenApiDocument().getDefinition();
        apiDto.setDocument(document);

//        OpenAPI openAPI = modelMapper.getOpenApiObjectFromBlob(specification.getOpenApiDocument().getOpenapi());
        OpenAPI openAPI = swhMapper.parseOpenApi(document);
        apiDto.setSecurityDefinitions(getClxApiOauth2SecurityDefinition(openAPI).orElse(null));

        NewCollaboration collaborationModel = specification.getCollaboration();
        apiDto.setCollaboration(
                collaborationModel == null ?
                        new CollaborationDto() :
                        modelMapper.collaborationModelToCollaborationDto(collaborationModel)
        );

        return apiDto;
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
