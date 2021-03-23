package org.jfo.swaggerhub.swhreporter.controller;

import org.jfo.swaggerhub.swhreporter.service.InitializerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

  public static final String ELAPSED_TIME_LOG = "Elapsed time: {}";
  public static final String INDEX_VIEW = "index";

  private final InitializerService initializerService;

  @GetMapping("/load/specs")
  public String loadSpecs() {
    long start = System.currentTimeMillis();
    initializerService.retrieveAllOwnedSpecs();
    log.info(ELAPSED_TIME_LOG, (System.currentTimeMillis() - start));
    return INDEX_VIEW;
  }

  @GetMapping("/load/collaborations")
  public String loadCollaboration() {
    long start = System.currentTimeMillis();
    initializerService.retrieveAllCollaborationsAndUpdateSpecification();
    log.info(ELAPSED_TIME_LOG, (System.currentTimeMillis() - start));
    return INDEX_VIEW;
  }
  
  @GetMapping("/load/projects")
  public String loadProjects() {
    long start = System.currentTimeMillis();
    initializerService.retrieveAllOwnedProjectsAndMembers();
    log.info(ELAPSED_TIME_LOG, (System.currentTimeMillis() - start));
    return INDEX_VIEW;
  }

}
