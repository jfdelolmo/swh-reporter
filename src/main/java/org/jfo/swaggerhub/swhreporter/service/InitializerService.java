package org.jfo.swaggerhub.swhreporter.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.jfo.swaggerhub.swhreporter.mappers.SwhMapper;
import org.jfo.swaggerhub.swhreporter.model.db.NewSpecification;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.jfo.swaggerhub.swhreporter.repository.NewSpecificationRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InitializerService {
  
  private final AdminService adminService;
  private final SwaggerHubService swaggerHubService;
  private final SwhMapper swhMapper;
  private final NewSpecificationRepository specificationRepository;

  public InitializerService(
          AdminService adminService,
          SwaggerHubService swaggerHubService,
          SwhMapper swhMapper,
          NewSpecificationRepository specificationRepository) {
    this.adminService = adminService;
    this.swaggerHubService = swaggerHubService;
    this.swhMapper = swhMapper;
    this.specificationRepository = specificationRepository;
  }
  
  public int retrieveAllSpecs(){
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
  
  public void retrieveAllApis(){
    log.info("Retrieving all the apis for initialization");
    List<ApisJsonApi> allApis = swaggerHubService.getAllOwnerApis(adminService.getUserOwner());
    log.info("Mapping the {} apis received", allApis.size());
    List<NewSpecification> allApisModel = allApis.stream().map(swhMapper::apisJsonApiToSpecModel).collect(Collectors.toList());
    log.info("Saving the {} apis mapped", allApis.size());
//    List<Specification> result = specificationRepository.saveAll(allApisModel);
//    log.info("{} apis stored in the database", result.size());
  }
  

}
