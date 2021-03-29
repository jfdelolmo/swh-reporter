package org.jfo.swaggerhub.swhreporter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ReporterService {

//    private final InitializerService initializerService;
//    private final NewSpecificationRepository specificationRepository;
//    private final ProjectRepository projectRepository;
//    private final ModelMapper modelMapper;
//    private final SpecValidator specValidator;
//    private final DataProvider dataProvider;
//    private final OASExtractor oasExtractor;

//    private static final int DEFAULT_PAGE_SIZE = 200;

//  public SpecsDto getSpecs() {
//    log.info("Entering service getApis method");
//
//    Page<NewSpecification> result = specificationRepository.findAll(PageRequest.of(0, DEFAULT_PAGE_SIZE, Sort.by("name").ascending()));
//
//    if (result.getTotalElements() == 0) {
//      initializerService.retrieveAllOwnedSpecs();
//      result = specificationRepository.findAll(PageRequest.of(0, DEFAULT_PAGE_SIZE, Sort.by("name").ascending()));
//    }
//
//    SpecsDto specsDto = new SpecsDto();
//    specsDto.setNumberOfSpec(result.getTotalElements());
//
//    List<SpecInfoDto> specList = new ArrayList<>();
//    result.getContent()
//        .forEach(c -> specList.add(modelMapper.specModelToSpecPropertiesDto(c)));
//
//    specsDto.setSpecs(specList);
//
//    return specsDto;
//  }

//  public ApiDto getApiDetails(Long id) throws InvalidSpecificationException {
//    log.info("Entering service getDefaultApi for {}", id);
//
//    NewSpecification specification = specificationRepository
//        .findById(id)
//        .orElseThrow(() -> new InvalidSpecificationException(String.format("The id %s is not valid", id)));
//
//    NewOpenApiDocument document = dataProvider.getOpenApiDocument(specification);
//    NewCollaboration collaboration = dataProvider.getCollaboration(specification);
//
//    oasExtractor.init(document.getResolved());
//
//    return modelMapper.buildApiDto(specification,
//        document,
//        collaboration,
//        oasExtractor.getScopes(),
//        oasExtractor.getRoles(),
//        oasExtractor.getAudiences());
//  }

//  public ProjectsReportDto getProjectsReport() {
//    ProjectsReportDto reportDto = new ProjectsReportDto();
//
//    if (projectRepository.count() == 0) {
//      initializerService.retrieveAllOwnedProjectsAndMembers();
//    }
//
//    Iterable<Project> dbProjects = projectRepository.findAllByOrderByNameAsc();
//    dbProjects.iterator().forEachRemaining(dbp -> reportDto.addProject(modelMapper.projectModelToDto(dbp)));
//    reportDto.setTotal(reportDto.getProjects().size());
//
//    return reportDto;
//  }

//  public ProjectParticipantsReportDto getParticipantsReport() {
//    ProjectParticipantsReportDto reportDto = new ProjectParticipantsReportDto();
//
//    if (projectRepository.count() == 0) {
//      initializerService.retrieveAllOwnedProjectsAndMembers();
//    }
//
//    Iterable<Project> dbProjects = projectRepository.findAllByOrderByNameAsc();
//    dbProjects.iterator().forEachRemaining(dbp -> {
//      ParticipantReportDto pprDto = new ParticipantReportDto();
//      pprDto.setProject(dbp.getName());
//      dbp.getParticipants()
//          .forEach(p -> pprDto.addParticipant(modelMapper.projectModelToParticipantDto(p)));
//      reportDto.addParticipant(pprDto);
//    });
//
//    return reportDto;
//  }

//  public WrongReferenceReportDto getWrongReferencedApis() {
//    WrongReferenceReportDto reportDto = new WrongReferenceReportDto();
//    AtomicLong accumulated = new AtomicLong();
//    Iterable<NewSpecification> specs = specificationRepository.findAll();
//    specs.forEach(s -> {
//      WrongReferenceSpecDto ws = new WrongReferenceSpecDto();
//      try {
//        if (!s.hasApi()) {
//          s = initializerService.retrieveApiAndStoreUpdatedSpecification(s);
//        }
//        String unresolved = s.getOpenApiDocument().getUnresolved();
//
//        Set<String> errors = new HashSet<>(specValidator.wrongReferences(unresolved));
//        if (CommonConcepts.TYPE_API.equalsIgnoreCase(s.getProperties().getType())) {
//          errors.addAll(specValidator.wrongOauthFlow(unresolved));
//        }
//        if (!errors.isEmpty()) {
//          long specErrorSize = addWrongReferenceErrors(reportDto, s, ws, errors);
//          accumulated.set(accumulated.get() + specErrorSize);
//        }
//      } catch (Exception e) {
//        log.error("Error retrieving the specification:", e);
//        addWrongReferenceException(reportDto, s, ws, e);
//        accumulated.set(accumulated.get() + 1);
//      }
//
//    });
//
//    reportDto.setTotal(accumulated.get());
//
//    return reportDto;
//  }

//  private int addWrongReferenceErrors(WrongReferenceReportDto reportDto, NewSpecification s, WrongReferenceSpecDto ws, Set<String> errors) {
//    int specErrorSize = errors.size();
//    ws.setErrors(errors);
//    ws.setNumErrors(specErrorSize);
//    ws.setTitle(s.getTitle());
//    ws.setName(s.getName());
//    ws.setType(s.getProperties().getType());
//    reportDto.getWrongspecs().add(ws);
//    return specErrorSize;
//  }

//  private void addWrongReferenceException(WrongReferenceReportDto reportDto, NewSpecification s, WrongReferenceSpecDto ws, Exception e) {
//    Set<String> errorSet = new HashSet<>();
//    errorSet.add(String.format("Error retrieving the specification: %s", e.getMessage()));
//    addWrongReferenceErrors(reportDto, s, ws, errorSet);
//  }

}
