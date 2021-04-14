package org.jfo.swaggerhub.swhreporter.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.jfo.swaggerhub.swhreporter.dto.MyAdminDto;
import org.jfo.swaggerhub.swhreporter.mappers.SwhMapper;
import org.jfo.swaggerhub.swhreporter.model.db.OpenApiDocument;
import org.jfo.swaggerhub.swhreporter.model.db.Project;
import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJson;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectMember;
import org.jfo.swaggerhub.swhreporter.model.swh.ProjectsJson;
import org.jfo.swaggerhub.swhreporter.repository.ProjectRepository;
import org.jfo.swaggerhub.swhreporter.repository.SpecificationRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * This service is responsible execute the required calls to SwaggerHub to fill the information in the repository.
 * - Retrieve all the owned specs
 * - For each spec retrieve the definition
 * - For each spec retrieve the collaboration (teams & members)
 * - Retrieve all the projects
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class InitializerService {

  private final SpecificationRepository specificationRepository;
  private final ProjectRepository projectRepository;

  private final SwaggerHubService swaggerHubService;
  private final AdminService adminService;

  private final SwhMapper swhMapper;

  public String initMyAdmin(MyAdminDto myAdminDto) {
    String owner = "";
    String apiKey = "";
    if (null!=myAdminDto){
      owner = myAdminDto.getOwner();
      apiKey = myAdminDto.getApikey();
    }
    return adminService.createAdmin(owner, apiKey).getUser(); 
  }
  
  public Long initAllOwnedSpecs() {
    MyAdminDto admin = adminService.getMyAdmin();
    if (admin.getPendingToUpdate()) {
      List<Specification> specsFromSwh = getSpecsFromSwh(admin.getOwner());
      
      AtomicLong updated = new AtomicLong(0);
      specificationRepository.saveOrUpdateAll(specsFromSwh)
          .subscribe(r ->
              log.info("Inserted spec {} - {} - {}", updated.incrementAndGet(), r.getId(), r.getName())
          );
      
      deleteSpecsNotReceived(specsFromSwh);
      
      return updated.get();
    }
    return -1L;
  }

  /**
   * Calls swaggerHubService to retrieve all the specs and maps them to Specification.
   * @param owner The owner returned by the AdminService
   * @return The list of specifications
   */
  private List<Specification> getSpecsFromSwh(String owner){
    List<ApisJsonApi> jsonApiList = new ArrayList<>();
    swaggerHubService
        .getAllOwnerSpecs(owner)
        .map(ApisJson::getApis)
        .all(jsonApiList::addAll)
        .block();

    return jsonApiList.stream()
        .map(swhMapper::apisJsonApiToSpecModel)
        .collect(Collectors.toList());
  }
  
  /**
   * Delete in the DB the ones deleted in SWH
   * Receives the specs from SwaggerHub
   * Get all specs in the DB
   * Removes from the list the ones received, so we keep in the list the ones that are not received
   * and have to be deleted
   * @param specsFromSwh List of specs from SwaggerHub
   */
  private void deleteSpecsNotReceived(List<Specification> specsFromSwh) {
    Set<String> specsFromSwhNameVersion = specsFromSwh
        .stream()
        .map(s->s.getName()+"::"+s.getSpecificationProperties().getDefaultVersion())
        .collect(Collectors.toSet());

    List<Specification> allDbSpecs = specificationRepository
        .findAll()
        .collectList()
        .block();

    if (null!=allDbSpecs && !allDbSpecs.isEmpty()) {
      Set<Specification> specsToDelete = allDbSpecs.stream()
          .filter(s -> !specsFromSwhNameVersion.contains(s.getName() + "::" + s.getSpecificationProperties().getDefaultVersion()))
          .collect(Collectors.toSet());
      specsToDelete.forEach(s -> {
        log.info("Delete {} - {}", s.getSpecificationProperties().getType(), s.getName());
        specificationRepository.deleteById(s.getId()).block();
      });
    }
  }

  public Long retrieveCollaborationAndUpdateSpecification(String specUuid) {
    MyAdminDto admin = adminService.getMyAdmin();
    if (null != admin.getOwner()) {
      Specification spec = specificationRepository.findById(specUuid).block();
      if (null != spec && null != spec.getSpecificationProperties() && null != spec.getSpecificationProperties().getUrl()) {
        Collaboration swhCollaboration = swaggerHubService.getCollaboration(
            admin.getOwner(),
            spec.getSpecificationProperties().getUrl())
            .block();
        spec.setCollaboration(swhMapper.collaborationSwhToModel(swhCollaboration));
        specificationRepository.save(spec).block();
        return 1L;
      }
    }
    return -1L;
  }

  public Long retrieveDocumentationAndUpdateSpecification(String specUuid) {
    MyAdminDto admin = adminService.getMyAdmin();
    if (null != admin.getOwner()) {
      Specification spec = specificationRepository.findById(specUuid).block();
      if (null != spec &&
          null != spec.getSpecificationProperties() &&
          null != spec.getSpecificationProperties().getUrl()) {

        Pair<String, String> pair = swaggerHubService
            .getResolvedUnresolvedSpec(spec.getSpecificationProperties().getUrl())
            .blockOptional()
            .orElse(Pair.of(null, null));

        OpenApiDocument newOAS = new OpenApiDocument(pair.getLeft(), pair.getRight());

        if (null == spec.getOpenApiDocument() || !spec.getOpenApiDocument().equals(newOAS)) {
          spec.setOpenApiDocument(newOAS);
          specificationRepository.save(spec).block();
          return 1L;
        }
      }
    }
    return -1L;
  }

  /**
   * Retrieves all the owned projects and save or updates them in the repository.
   *
   * @return All the projects with it's members.
   */
  public Long rxInitAllOwnedProjects() {
    MyAdminDto admin = adminService.getMyAdmin();
    AtomicLong updated = new AtomicLong(0);
    if (null != admin.getOwner()) {
      List<org.jfo.swaggerhub.swhreporter.model.swh.Project> shwProjectList = new ArrayList<>();
      swaggerHubService
          .getProjects(admin.getOwner())
          .map(ProjectsJson::getProjects)
          .all(shwProjectList::addAll)
          .block();

      Set<Project> mappedSwhProjectSet = shwProjectList.stream()
          .map(swhMapper::projectSwhToModel)
          .collect(Collectors.toSet());
      log.info("Received {} projects from SwaggerHub", mappedSwhProjectSet.size());

      log.debug("Calling SwaggerHub to get the projects members");
      mappedSwhProjectSet.forEach(project -> {
        Flux<ProjectMember> memberFlux = swaggerHubService.getProjectMembers(admin.getOwner(), project.getName());
        if (null != memberFlux) {
          project.getParticipants()
              .addAll(
                  memberFlux.map(swhMapper::memberShwToParticipants)
                      .collect(Collectors.toSet())
                      .blockOptional()
                      .orElse(Collections.emptySet())
              );
        }
      });

      mappedSwhProjectSet.forEach(incoming -> {
        Project existent = projectRepository.findByName(incoming.getName()).block();

        if (null == existent) {
          projectRepository.save(incoming).block();
          log.info("{} - Saved new project {} - {}", updated.incrementAndGet(), incoming.getId(), incoming.getName());
        } else {
          if (!existent.equals(incoming)) {
            existent.setDescription(incoming.getDescription());
            existent.setApis(incoming.getApis());
            existent.setDomains(incoming.getDomains());
            projectRepository.save(existent).block();
            log.info("{} - Updated project {} - {}", updated.incrementAndGet(), existent.getId(), existent.getName());
          }else
            log.info("Received unmodified project {} - {}", existent.getId(), existent.getName());
        }
      });

    }
    return updated.get();
  }

  public Long updateProjectSpecs(){
    AtomicLong projectUpdated = new AtomicLong(0);
    AtomicLong updated = new AtomicLong(0);
    Set<Project> projects = projectRepository.findAll().collect(Collectors.toSet()).block();
    if (null!=projects){
      projects.forEach(p -> {
        
        p.getApis().forEach(a -> {
          Specification spec = specificationRepository.findByName(a.getName()).block();
          if (null!=spec && a.getId()==null){
            a.setId(spec.getId());
            updated.incrementAndGet();
          }
        });
        
        p.getDomains().forEach(d -> {
          Specification spec = specificationRepository.findByName(d.getName()).block();
          if (null!=spec && d.getId()==null){
            d.setId(spec.getId());
            updated.incrementAndGet();
          }
        });
        
        if (updated.get()>0){
          projectRepository.save(p).block();
          log.info("{} SpecId updated from project {}",projectUpdated.incrementAndGet(),p.getName());
          updated.set(0);
        }

      });
    }
    return projectUpdated.get();
  }

}
