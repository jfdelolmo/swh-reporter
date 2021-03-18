package org.jfo.swaggerhub.swhreporter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.jfo.swaggerhub.swhreporter.mappers.SwhMapper;
import org.jfo.swaggerhub.swhreporter.model.db.NewSpecification;
import org.jfo.swaggerhub.swhreporter.model.db.Project;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectMember;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectsJson;
import org.jfo.swaggerhub.swhreporter.repository.NewSpecificationRepository;
import org.jfo.swaggerhub.swhreporter.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InitializerService {

  private final AdminService adminService;
  private final SwaggerHubService swaggerHubService;
  private final SwhMapper swhMapper;
  private final NewSpecificationRepository specificationRepository;
  private final ProjectRepository projectRepository;

  public InitializerService(
      AdminService adminService,
      SwaggerHubService swaggerHubService,
      SwhMapper swhMapper,
      NewSpecificationRepository specificationRepository,
      ProjectRepository projectRepository) {
    this.adminService = adminService;
    this.swaggerHubService = swaggerHubService;
    this.swhMapper = swhMapper;
    this.specificationRepository = specificationRepository;
    this.projectRepository = projectRepository;
  }

  public int retrieveAllSpecs() {
    log.info("Retrieving all the specs for initialization");
    List<ApisJsonApi> allApis = swaggerHubService.getAllOwnerSpecs(adminService.getUserOwner());
    log.info("Mapping the {} specs received", allApis.size());
    List<NewSpecification> allApisModel = allApis.stream().map(swhMapper::apisJsonApiToSpecModel).collect(Collectors.toList());
    log.info("Saving the {} specs mapped", allApis.size());

    List<NewSpecification> result = specificationRepository.saveOrUpdateAll(allApisModel);
//    Iterator<NewSpecification> iterator = specificationRepository.saveAll(allApisModel).iterator();
//    List<NewSpecification> result = new ArrayList<>();
//    iterator.forEachRemaining(result::add);
    log.info("{} specs stored in the database", result.size());
    return result.size();
  }

  public void retrieveAllApis() {
    log.info("Retrieving all the apis for initialization");
    List<ApisJsonApi> allApis = swaggerHubService.getAllOwnerApis(adminService.getUserOwner());
    log.info("Mapping the {} apis received", allApis.size());
    List<NewSpecification> allApisModel = allApis.stream().map(swhMapper::apisJsonApiToSpecModel).collect(Collectors.toList());
    log.info("Saving the {} apis mapped", allApis.size());
//    List<Specification> result = specificationRepository.saveAll(allApisModel);
//    log.info("{} apis stored in the database", result.size());
  }

  public void retrieveAllCollaborations() {
    Iterable<NewSpecification> allSpecs = specificationRepository.findAll();
    allSpecs.iterator().forEachRemaining(s -> {
      Collaboration collaboration = swaggerHubService.getCollaboration(s.getProperties().getUrl());
      s.updateCollaboration(swhMapper.collaborationSwhToModel(collaboration));
    });
  }
  
  public void retrieveAllProjects() {
    ProjectsJson swhProjects = swaggerHubService.getProjects(adminService.getUserOwner());
    swhProjects.getProjects().forEach(project -> {
      Project dbProject = swhMapper.projectSwhToModel(project);
      List<ProjectMember> members = swaggerHubService.getProjectMembers(adminService.getUserOwner(), project.getName());
      members.forEach( m -> dbProject.addParticipant(swhMapper.memberShwToParticipants(m)));
      projectRepository.save(dbProject);
    });
  }

}
