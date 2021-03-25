package org.jfo.swaggerhub.swhreporter.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.jfo.swaggerhub.swhreporter.model.db.NewCollaboration;
import org.jfo.swaggerhub.swhreporter.model.db.NewOpenApiDocument;
import org.jfo.swaggerhub.swhreporter.model.db.NewSpecification;
import org.jfo.swaggerhub.swhreporter.model.swh.Collaboration;
import org.jfo.swaggerhub.swhreporter.repository.NewSpecificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
/**
 * This service is responsible to safely get the information by checking if it's in the repository. 
 */

public class DataProvider {
  
  private final NewSpecificationRepository specificationRepository;
  private final InitializerService initializerService;
  
  public Set<NewSpecification> getAllSpecifications(){
    Set<NewSpecification> specifications = new HashSet<>();
    if (specificationRepository.count()==0){
      initializerService.retrieveAllOwnedSpecs();
    }
    specificationRepository.findAll().iterator().forEachRemaining(specifications::add);
    return specifications;
  }

  public Page<NewSpecification> getSpecificationsPage(Pageable page){
    if (specificationRepository.count()==0){
      initializerService.retrieveAllOwnedSpecs();
    }
    return specificationRepository.findAll(page);
  }
  
  public NewOpenApiDocument getOpenApiDocument(NewSpecification specification){
    NewOpenApiDocument output = new NewOpenApiDocument("Not defined", "Not defined");
    Optional<NewSpecification> dbSpec = specificationRepository.findById(specification.getId());
    if (dbSpec.isPresent()){
      if (dbSpec.map(NewSpecification::getOpenApiDocument).isEmpty()){
        NewSpecification updated = initializerService.retrieveApiAndStoreUpdatedSpecification(dbSpec.get());
        output = updated.getOpenApiDocument();
      }else{
        output = dbSpec.get().getOpenApiDocument();
      }
    }
    return output;
  }

  public NewCollaboration getCollaboration(NewSpecification specification) {
    NewCollaboration output = new NewCollaboration();
    
    Optional<NewSpecification> dbSpec = specificationRepository.findById(specification.getId());
    if (dbSpec.isPresent()) {
      if (dbSpec.map(NewSpecification::getCollaboration).isEmpty()) {
        NewSpecification updated = initializerService.retrieveCollaborationAndStoreUpdatedSpecification(dbSpec.get());
        output = updated.getCollaboration();
      } else {
        output = dbSpec.get().getCollaboration();
      }
    }
    return output;
  }
  
  
}
