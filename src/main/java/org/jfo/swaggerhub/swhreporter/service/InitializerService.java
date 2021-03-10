package org.jfo.swaggerhub.swhreporter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.jfo.swaggerhub.swhreporter.mappers.SwhMapper;
import org.jfo.swaggerhub.swhreporter.model.db.Specification;
import org.jfo.swaggerhub.swhreporter.model.swh.ApisJsonApi;
import org.jfo.swaggerhub.swhreporter.repository.SpecRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InitializerService {
  
  private final AdminService adminService;
  private final SwaggerHubService swaggerHubService;
  private final SwhMapper swhMapper;
  private final SpecRepository specRepository;
  
  
  public InitializerService(AdminService adminService, SwaggerHubService swaggerHubService, SwhMapper swhMapper, SpecRepository specRepository) {
    this.adminService = adminService;
    this.swaggerHubService = swaggerHubService;
    this.swhMapper = swhMapper;
    this.specRepository = specRepository;
  }
  
  public int retrieveAllSpecs(){
    log.info("Retrieving all the specs for initialization");
    List<ApisJsonApi> allApis = swaggerHubService.getAllOwnerSpecs(adminService.getUserOwner());
    log.info("Mapping the {} specs received", allApis.size());
    List<Specification> allApisModel = allApis.stream().map(swhMapper::apisJsonApiToSpecModel).collect(Collectors.toList());
    log.info("Saving the {} specs mapped", allApis.size());
    List<Specification> result = specRepository.saveOrUpdateAll(allApisModel);
    log.info("{} specs stored in the database", result.size());
    return result.size();
  }
  
  public void retrieveAllApis(){
    log.info("Retrieving all the apis for initialization");
    List<ApisJsonApi> allApis = swaggerHubService.getAllOwnerApis(adminService.getUserOwner());
    log.info("Mapping the {} apis received", allApis.size());
    List<Specification> allApisModel = allApis.stream().map(swhMapper::apisJsonApiToSpecModel).collect(Collectors.toList());
    log.info("Saving the {} apis mapped", allApis.size());
    List<Specification> result = specRepository.saveAll(allApisModel);
    log.info("{} apis stored in the database", result.size());
  }
  

}
