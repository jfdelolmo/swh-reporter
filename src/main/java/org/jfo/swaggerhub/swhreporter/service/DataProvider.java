package org.jfo.swaggerhub.swhreporter.service;

import java.util.HashSet;
import java.util.Set;

import org.jfo.swaggerhub.swhreporter.model.db.NewSpecification;
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
  
  
  
}
