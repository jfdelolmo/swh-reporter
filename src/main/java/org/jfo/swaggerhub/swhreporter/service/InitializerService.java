package org.jfo.swaggerhub.swhreporter.service;

import java.util.HashSet;
import java.util.Set;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * This service is responsible execute the required calls to SwaggerHub to fill the information in the repository.
 * - Retrieve all the owned specs
 * - For each spec retrieve the definition
 * - For each spec retrieve the collaboration (teams & members)
 * - Retrieve all the projects
 */
public class InitializerService {

  private final AdminService adminService;
  private final SwaggerHubService swaggerHubService;
  private final SwhMapper swhMapper;
  private final NewSpecificationRepository specificationRepository;
  private final ProjectRepository projectRepository;

  public void retrieveALl() {
//    retrieveAllOwnedSpecs();
    retrieveAllCollaborationsAndUpdateSpecification();
//    retrieveAllOwnedProjectsAndMembers();
  }

  /**
   * Retrieves all the owned specifications and save or updates them in the repository.
   *
   * @return Set of specifications
   */
  public Set<NewSpecification> retrieveAllOwnedSpecs() {
    log.info("Retrieving all the specs for initialization");
    Set<ApisJsonApi> allApis = swaggerHubService.getAllOwnerSpecs(adminService.getUserOwner());
    log.info("Mapping the {} specs received", allApis.size());
    Set<NewSpecification> allApisModel = allApis.stream().map(swhMapper::apisJsonApiToSpecModel).collect(Collectors.toSet());
    log.info("Saving the {} specs mapped", allApis.size());
    Set<NewSpecification> result = specificationRepository.saveOrUpdateAll(allApisModel);
    log.info("{} specs stored in the database", result.size());
    return result;
  }
  
  /**
   * For each of the specifications stored in the repository retrieve it's collaboration and updates the specification.
   * @return Set of specifications with the collaboration updated
   */
  public Set<NewSpecification> retrieveAllCollaborationsAndUpdateSpecification() {
    Set<NewSpecification> allSpecs = new HashSet<>();
    specificationRepository.findAll().forEach(s -> {
      Collaboration collaboration = swaggerHubService.getCollaboration(s.getProperties().getUrl());
      s.updateCollaboration(swhMapper.collaborationSwhToModel(collaboration));
      allSpecs.add(s);
    });
    return allSpecs;
  }

  /**
   * Retrieves all the owned projects and save or updates them in the repository.
   * @return All the projects with it's members.
   */
  public Set<Project> retrieveAllOwnedProjectsAndMembers() {
    Set<Project> projects = new HashSet<>();
    
    log.debug("Calling SwaggerHub to get the projects");
    ProjectsJson swhProjects = swaggerHubService.getProjects(adminService.getUserOwner());
    log.debug("Received {} projects from SwaggerHub", swhProjects.getProjects().size());

    log.debug("Calling SwaggerHub to get the projects members");
    swhProjects.getProjects().forEach(project -> {
      Project dbProject = swhMapper.projectSwhToModel(project);
      Set<ProjectMember> members = swaggerHubService.getProjectMembers(adminService.getUserOwner(), project.getName());
      members.forEach(m -> dbProject.addParticipant(swhMapper.memberShwToParticipants(m)));
      projects.add(projectRepository.saveOrUpdate(dbProject));
    });
    
    return projects;
  }

}
