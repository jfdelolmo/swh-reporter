package org.jfo.swaggerhub.swhreporter.service;

import java.util.HashSet;
import java.util.Set;

import org.jfo.swaggerhub.swhreporter.model.db.Collaboration;
import org.jfo.swaggerhub.swhreporter.model.db.OpenApiDocument;
import org.jfo.swaggerhub.swhreporter.model.db.Specification;
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
  
//  private final NewSpecificationRepository specificationRepository;
  private final InitializerService initializerService;
  
  public Set<Specification> getAllSpecifications(){
    Set<Specification> specifications = new HashSet<>();
//    if (specificationRepository.count()==0){
//      initializerService.retrieveAllOwnedSpecs();
//    }
//    specificationRepository.findAll().iterator().forEachRemaining(specifications::add);
    return specifications;
  }

  public Page<Specification> getSpecificationsPage(Pageable page){
//    if (specificationRepository.count()==0){
//      initializerService.retrieveAllOwnedSpecs();
//    }
//    return specificationRepository.findAll(page);
    return null;
  }
  
  public OpenApiDocument getOpenApiDocument(Specification specification){
    OpenApiDocument output = new OpenApiDocument("Not defined", "Not defined");
//    Optional<NewSpecification> dbSpec = specificationRepository.findById(specification.getId());
//    if (dbSpec.isPresent()){
//      if (dbSpec.map(NewSpecification::getOpenApiDocument).isEmpty()){
//        NewSpecification updated = initializerService.retrieveApiAndStoreUpdatedSpecification(dbSpec.get());
//        output = updated.getOpenApiDocument();
//      }else{
//        output = dbSpec.get().getOpenApiDocument();
//      }
//    }
    return output;
  }

  public Collaboration getCollaboration(Specification specification) {
    Collaboration output = new Collaboration();
    
//    Optional<NewSpecification> dbSpec = specificationRepository.findById(specification.getId());
//    if (dbSpec.isPresent()) {
//      if (dbSpec.map(NewSpecification::getCollaboration).isEmpty()) {
//        NewSpecification updated = initializerService.retrieveCollaborationAndStoreUpdatedSpecification(dbSpec.get());
//        output = updated.getCollaboration();
//      } else {
//        output = dbSpec.get().getCollaboration();
//      }
//    }
    return output;
  }
  
  
}
