package org.jfo.swaggerhub.swhreporter.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.jfo.swaggerhub.swhreporter.dto.ApiDto;
import org.jfo.swaggerhub.swhreporter.dto.ClxApiOauth2SecurityDefinitionDto;
import org.jfo.swaggerhub.swhreporter.dto.CollaborationDto;
import org.jfo.swaggerhub.swhreporter.dto.ParticipantReportDto;
import org.jfo.swaggerhub.swhreporter.dto.ProjectParticipantsReportDto;
import org.jfo.swaggerhub.swhreporter.dto.ProjectsReportDto;
import org.jfo.swaggerhub.swhreporter.dto.SpecInfoDto;
import org.jfo.swaggerhub.swhreporter.dto.SpecsDto;
import org.jfo.swaggerhub.swhreporter.dto.WrongReferenceReportDto;
import org.jfo.swaggerhub.swhreporter.dto.WrongReferenceSpecDto;
import org.jfo.swaggerhub.swhreporter.exception.SwaggerParseResultException;
import org.jfo.swaggerhub.swhreporter.mappers.ModelMapper;
import org.jfo.swaggerhub.swhreporter.mappers.SwhMapper;
import org.jfo.swaggerhub.swhreporter.model.db.NewCollaboration;
import org.jfo.swaggerhub.swhreporter.model.db.NewOpenApiDocument;
import org.jfo.swaggerhub.swhreporter.model.db.NewSpecification;
import org.jfo.swaggerhub.swhreporter.model.db.Project;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import org.jfo.swaggerhub.swhreporter.repository.NewSpecificationRepository;
import org.jfo.swaggerhub.swhreporter.repository.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReporterService {

  private final InitializerService initializerService;
  private final NewSpecificationRepository specificationRepository;
  private final ProjectRepository projectRepository;
  private final SwaggerHubServiceImpl swaggerHubServiceImpl;
  private final ModelMapper modelMapper;
  private final SwhMapper swhMapper;
  private final SpecValidator specValidator;

  private static final int DEFAULT_PAGE_SIZE = 200;

  public SpecsDto getSpecs() {
    log.info("Entering service getApis method");

    Page<NewSpecification> result = specificationRepository.findAll(PageRequest.of(0, DEFAULT_PAGE_SIZE, Sort.by("name").ascending()));

    if (result.getTotalElements() == 0) {
      initializerService.retrieveAllOwnedSpecs();
      result = specificationRepository.findAll(PageRequest.of(0, DEFAULT_PAGE_SIZE, Sort.by("name").ascending()));
    }

    SpecsDto specsDto = new SpecsDto();
    specsDto.setNumberOfSpec(result.getTotalElements());

    List<SpecInfoDto> specList = new ArrayList<>();
    result.getContent()
        .stream()
        .forEachOrdered(c -> specList.add(modelMapper.specModelToSpecPropertiesDto(c)));

    specsDto.setSpecs(specList);

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
    if (!specification.hasApi()) {
      specification = retrieveApiAndStoreUpdatedSpecification(specification);
    }

    if (specificationHasNoCollaboration(specification)) {
      specification = retrieveCollaborationAndStoreUpdatedSpecification(specification);
    }

    return buildApiDto(specification);
  }

  private NewSpecification retrieveCollaborationAndStoreUpdatedSpecification(NewSpecification specification) {
    Collaboration collaboration = swaggerHubServiceImpl.getCollaboration(specification.getProperties().getUrl());
    NewCollaboration collaborationModel = swhMapper.collaborationSwhToModel(collaboration);
    return specificationRepository.save(specification.setCollaboration(collaborationModel));
  }

  private boolean specificationHasNoCollaboration(NewSpecification specification) {
    return (null == specification.getCollaboration());
  }

  private NewSpecification retrieveApiAndStoreUpdatedSpecification(NewSpecification specification) {
    String resolvedApi;
    String unresolvedApi;
    try {
      resolvedApi = swaggerHubServiceImpl.getSpecVersionByUrl(specification.getProperties().getUrl(), true);
    } catch (Exception e) {
      resolvedApi = "Error on retrieving the resolved specification from SwaggerHub";
    }
    unresolvedApi = swaggerHubServiceImpl.getSpecVersionByUrl(specification.getProperties().getUrl(), false);

    specification.setOpenApiDocument(new NewOpenApiDocument(resolvedApi, unresolvedApi));

    return specificationRepository.save(specification);
  }

  private ApiDto buildApiDto(NewSpecification specification) throws Exception {
    ApiDto apiDto = new ApiDto();
    apiDto.setCollaboration(new CollaborationDto());

    apiDto.setName(specification.getName());
    apiDto.setType(StringUtils.capitalize(specification.getProperties().getType().toLowerCase()));
    apiDto.setVersion(specification.getProperties().getVersion());

    String document = specification.getOpenApiDocument().getResolved();
    apiDto.setDocument(document);

    if ("API".equalsIgnoreCase(specification.getProperties().getType())) {
      try {
        OpenAPI openAPI = swhMapper.parseOpenApi(document);
        apiDto.setSecurityDefinitions(getClxApiOauth2SecurityDefinition(openAPI).orElse(new ClxApiOauth2SecurityDefinitionDto()));
      } catch (SwaggerParseResultException e) {
        log.error(e.getMessage());
        e.getErrors().forEach(log::error);
        apiDto.setSecurityDefinitions(new ClxApiOauth2SecurityDefinitionDto());
      }
    } else {
      apiDto.setSecurityDefinitions(new ClxApiOauth2SecurityDefinitionDto());
    }

    NewCollaboration collaborationModel = specification.getCollaboration();
    if (null != collaborationModel) {
      apiDto.setCollaboration(modelMapper.collaborationModelToCollaborationDto(collaborationModel));
    }

    return apiDto;
  }


  private Optional<ClxApiOauth2SecurityDefinitionDto> getClxApiOauth2SecurityDefinition(OpenAPI api) {
    ClxApiOauth2SecurityDefinitionDto securities = new ClxApiOauth2SecurityDefinitionDto();
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

  public ProjectsReportDto getProjectsReport() {
    ProjectsReportDto reportDto = new ProjectsReportDto();

    if (projectRepository.count() == 0) {
      initializerService.retrieveAllOwnedProjectsAndMembers();
    }

    Iterable<Project> dbProjects = projectRepository.findAllByOrderByNameAsc();
    dbProjects.iterator().forEachRemaining(dbp -> reportDto.addProject(modelMapper.projectModelToDto(dbp)));
    reportDto.setTotal(reportDto.getProjects().size());

    return reportDto;
  }

  public ProjectParticipantsReportDto getParticipantsReport() {
    ProjectParticipantsReportDto reportDto = new ProjectParticipantsReportDto();

    if (projectRepository.count() == 0) {
      initializerService.retrieveAllOwnedProjectsAndMembers();
    }

    Iterable<Project> dbProjects = projectRepository.findAllByOrderByNameAsc();
    dbProjects.iterator().forEachRemaining(dbp -> {
      ParticipantReportDto pprDto = new ParticipantReportDto();
      pprDto.setProject(dbp.getName());
      dbp.getParticipants()
          .forEach(p -> pprDto.addParticipant(modelMapper.projectModelToParticipantDto(p)));
      reportDto.addParticipant(pprDto);
    });

    return reportDto;
  }

  public WrongReferenceReportDto getWrongReferencedApis() {
    WrongReferenceReportDto reportDto = new WrongReferenceReportDto();
    AtomicLong accumulated = new AtomicLong();
    Iterable<NewSpecification> specs = specificationRepository.findAll();
    specs.forEach(s -> {
      WrongReferenceSpecDto ws = new WrongReferenceSpecDto();
      try {
        if (!s.hasApi()) {
          s = retrieveApiAndStoreUpdatedSpecification(s);
        }
        String unresolved = s.getOpenApiDocument().getUnresolved();

        Set<String> errors = new HashSet<>();
        errors.addAll(specValidator.wrongReferences(unresolved));
        if ("API".equalsIgnoreCase(s.getProperties().getType())) {
          errors.addAll(specValidator.wrongOauthFlow(unresolved));
        }
        if (!errors.isEmpty()) {
          long specErrorSize = addWrongReferenceErrors(reportDto, s, ws, errors);
          accumulated.set(accumulated.get() + specErrorSize);
        }
      } catch (Exception e) {
        log.error("Error retrieving the specification:", e);
        addWrongReferenceException(reportDto, s, ws, e);
        accumulated.set(accumulated.get() + 1);
      }

    });

    reportDto.setTotal(accumulated.get());

    return reportDto;
  }

  private int addWrongReferenceErrors(WrongReferenceReportDto reportDto, NewSpecification s, WrongReferenceSpecDto ws, Set<String> errors) {
    int specErrorSize = errors.size();
    ws.setErrors(errors);
    ws.setNumErrors(specErrorSize);
    ws.setTitle(s.getTitle());
    ws.setName(s.getName());
    ws.setType(s.getProperties().getType());
    reportDto.getWrongspecs().add(ws);
    return specErrorSize;
  }

  private void addWrongReferenceException(WrongReferenceReportDto reportDto, NewSpecification s, WrongReferenceSpecDto ws, Exception e) {
    Set<String> errorSet = new HashSet<>();
    errorSet.add(String.format("Error retrieving the specification: %s", e.getMessage()));
    addWrongReferenceErrors(reportDto, s, ws, errorSet);

//        ws.addError(String.format("Error retrieving the specification: %s", e.getMessage()));
//        ws.setNumErrors(1);
//        ws.setName(s.getName());
//        ws.setTitle(s.getTitle());
//        ws.setType(s.getProperties().getType());
//        reportDto.getWrongspecs().add(ws);
  }


}
