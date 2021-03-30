package org.jfo.swaggerhub.swhreporter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfo.swaggerhub.swhreporter.mappers.SwhMapper;
import org.jfo.swaggerhub.swhreporter.model.db.Admin;
import org.jfo.swaggerhub.swhreporter.model.db.Project;
import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJson;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectsJson;
import org.jfo.swaggerhub.swhreporter.repository.ProjectReactiveRepository;
import org.jfo.swaggerhub.swhreporter.repository.SpecificationReactiveRepository;
import org.jfo.swaggerhub.swhreporter.service.reactive.RxAdminService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    private final SpecificationReactiveRepository specificationReactiveRepository;
    private final ProjectReactiveRepository projectReactiveRepository;

    private final RxSwaggerHubService rxSwaggerHubService;
    private final RxAdminService adminService;

    private final SwhMapper swhMapper;

    public String initDummyAdmin(){
        return adminService.createAdmin("","").getId().toString();
    }

    public Long rxInitAllOwnedSpecs() {
        Admin admin = adminService.getMyAdmin().blockOptional().orElse(null);
        if (null != admin && admin.getPendingToUpdate()) {
            List<ApisJsonApi> jsonApiList = new ArrayList<>();
            rxSwaggerHubService
                    .getAllOwnerSpecs(admin.getOwner())
                    .map(ApisJson::getApis)
                    .all(jsonApiList::addAll)
                    .block();

            List<Specification> specifications = jsonApiList.stream()
                    .map(swhMapper::apisJsonApiToSpecModel)
                    .collect(Collectors.toList());

            specificationReactiveRepository.insert(specifications)
                    .subscribe(r -> log.info("Inseerted spec {} - {}", r.getId(), r.getName()));

            return specificationReactiveRepository.count().block();
        }
        return -1L;
    }

    /**
     * Retrieves all the owned projects and save or updates them in the repository.
     *
     * @return All the projects with it's members.
     */
    public Long rxInitAllOwnedProjects() {
        Admin admin = adminService.getMyAdmin().blockOptional().orElse(null);
        if (null != admin && null!=admin.getOwner()) {
            List<org.jfo.swaggerhub.swhreporter.model.swh.Project> jsonProjectList = new ArrayList<>();
            rxSwaggerHubService
                    .getProjects(admin.getOwner())
                    .map(ProjectsJson::getProjects)
                    .all(jsonProjectList::addAll)
                    .block();

            Set<Project> projectSet = jsonProjectList.stream()
                    .map(swhMapper::projectSwhToModel)
                    .collect(Collectors.toSet());

            projectReactiveRepository.saveAll(projectSet)
                    .subscribe(p -> log.info("Saved project {} - {}", p.getId(), p.getName()));
        }
        return -1L;
    }
//  public Set<Project> retrieveAllOwnedProjectsAndMembers() {
//    Set<Project> projects = new HashSet<>();
//
//    log.debug("Calling SwaggerHub to get the projects");
//    ProjectsJson swhProjects = swaggerHubServiceImpl.getProjects(adminService.getUserOwner());
//    log.debug("Received {} projects from SwaggerHub", swhProjects.getProjects().size());
//
//    log.debug("Calling SwaggerHub to get the projects members");
//    swhProjects.getProjects().forEach(project -> {
//      Project dbProject = swhMapper.projectSwhToModel(project);
//      Set<ProjectMember> members = swaggerHubServiceImpl.getProjectMembers(adminService.getUserOwner(), project.getName());
//      members.forEach(m -> dbProject.addParticipant(swhMapper.memberShwToParticipants(m)));
//      projects.add(projectRepository.saveOrUpdate(dbProject));
//    });
//
//    return projects;
//  }





    /**
     * Retrieves all the owned specifications and save or updates them in the repository.
     *
     * @return Set of specifications
     */
    public Set<Specification> retrieveAllOwnedSpecs() {
        log.info("Retrieving all the specs for initialization");
//    Set<ApisJsonApi> allApis = swaggerHubServiceImpl.getAllOwnerSpecs(adminService.getUserOwner());
        Flux<ApisJson> flux;
        rxSwaggerHubService
                .getAllOwnerSpecs(adminService.getMyUserOwner().block())
                .map(ApisJson::getApis)
                .collect(Collectors.toSet());


//    log.info("Mapping the {} specs received", allApis.size());
//    Set<Specification> allApisModel = allApis.stream().map(swhMapper::apisJsonApiToSpecModel).collect(Collectors.toSet());
//    log.info("Saving the {} specs mapped", allApis.size());
//    Set<NewSpecification> result = specificationRepository.saveOrUpdateAll(allApisModel);
//    log.info("{} specs stored in the database", result.size());
//    return result;
        return null;
    }

    /**
     * For each of the specifications stored in the repository retrieve it's collaboration and updates the specification.
     * @return Set of specifications with the collaboration updated
     */
//  public Set<NewSpecification> retrieveAllCollaborationsAndUpdateSpecification() {
//    Set<NewSpecification> allSpecs = new HashSet<>();
//    specificationRepository.findAll().forEach(s -> {
//      Collaboration collaboration = swaggerHubServiceImpl.getCollaboration(s.getProperties().getUrl());
//      s.updateCollaboration(swhMapper.collaborationSwhToModel(collaboration));
//      allSpecs.add(s);
//    });
//    return allSpecs;
//  }


    public Specification retrieveApiAndStoreUpdatedSpecification(Specification specification) {
//    Pair<String, String> specsPair = swaggerHubServiceImpl.getResolvedUnresolvedSpec(specification.getSpecificationProperties().getUrl());
//
//    specification.setOpenApiDocument(new OpenApiDocument(specsPair.getLeft(), specsPair.getRight()));

//    return specificationRepository.save(specification);
        return null;
    }

    public Specification retrieveCollaborationAndStoreUpdatedSpecification(Specification specification) {
//    Collaboration collaboration = swaggerHubServiceImpl.getCollaboration(specification.getSpecificationProperties().getUrl());
//    specification.setCollaboration(swhMapper.collaborationSwhToModel(collaboration));
//    return specificationRepository.save(specification);
        return null;
    }



}
